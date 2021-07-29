package sg.edu.rp.id19037610.c302_p11_firebaseinventoryapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;

public class AddEditItemActivity extends AppCompatActivity {

    EditText etName, etCost;
    Button btnSave;
    Phones phone;
    String action;

    FirebaseFirestore db;
    CollectionReference colRef;
    DocumentReference docRef;

    ArrayList<String> alCheckboxItems;
    LinearLayout cbContainer;
    CheckBox[] checkBoxes;
    ArrayList<String> alChecked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_item);

        etName = findViewById(R.id.etName);
        etCost = findViewById(R.id.etCost);
        btnSave = findViewById(R.id.btnSave);
        cbContainer = findViewById(R.id.cbContainer);
        alCheckboxItems = new ArrayList<String>();
        alChecked = new ArrayList<String>();

        alCheckboxItems.add("Apple Digital AV Adapter");
        alCheckboxItems.add("Smart Keyboard");
        alCheckboxItems.add("Silicone Case");
        alCheckboxItems.add("Smart Case");

        Intent intent = getIntent();
        phone = (Phones) intent.getSerializableExtra("phone");
        action = intent.getStringExtra("action");

        db = FirebaseFirestore.getInstance();
        colRef = db.collection("phoneList");


        if (action.equalsIgnoreCase("edit")) {
            etName.setText(phone.getName());
            etCost.setText(String.valueOf(phone.getCost()));

            alChecked = phone.getOptions();

        }

        createCheckboxes();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = etName.getText().toString();
                int cost = Integer.parseInt(etCost.getText().toString());
                Phones p = new Phones(name, cost, alChecked);

                String msg = "";
                if (action.equalsIgnoreCase("add")) {
                    colRef.add(p);
                    msg = "Successfully added item";
                } else if (action.equalsIgnoreCase("edit")) {
                    docRef = colRef.document(phone.getId());
                    docRef.set(p);
                    msg = "Successfully updated item";
                }

                Toast.makeText(AddEditItemActivity.this, msg, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    public void createCheckboxes() {
        checkBoxes = new CheckBox[alCheckboxItems.size()];

        for (int i = 0; i < alCheckboxItems.size(); i++) {
            checkBoxes[i] = new CheckBox(AddEditItemActivity.this);

            LinearLayout checkbox = new LinearLayout(cbContainer.getContext());
            checkBoxes[i].setText(alCheckboxItems.get(i));

            LinearLayout.LayoutParams cbParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1.0f
            );
            checkBoxes[i].setLayoutParams(cbParams);

            checkbox.addView(checkBoxes[i]);
            cbContainer.addView(checkbox);

            for (int a = 0; a < alChecked.size(); a++) {
                if (alChecked.get(a).contains(checkBoxes[i].getText())) {
                    checkBoxes[i].setChecked(true);
                }
            }

            int index = i;

            checkBoxes[i].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    String item = alCheckboxItems.get(index);

                    if (b) {
                        alChecked.add(item);
                    } else {
                        alChecked.remove(item);
                    }
                }
            });
        }
    }
}