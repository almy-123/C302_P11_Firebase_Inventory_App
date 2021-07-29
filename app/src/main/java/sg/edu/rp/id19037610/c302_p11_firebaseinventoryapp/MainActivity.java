package sg.edu.rp.id19037610.c302_p11_firebaseinventoryapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
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

public class MainActivity extends AppCompatActivity {

    ListView lvPhones;
    ArrayList<Phones> alPhones;
    ArrayAdapter<Phones> aaPhones;

    FirebaseFirestore db;
    CollectionReference colRef;
    DocumentReference docRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvPhones = findViewById(R.id.lvPhones);
        alPhones = new ArrayList<Phones>();

        db = FirebaseFirestore.getInstance();
        colRef = db.collection("phoneList");

        aaPhones = new ArrayAdapter<Phones>(getApplicationContext(), android.R.layout.simple_list_item_1, alPhones) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                LayoutInflater inflater = (LayoutInflater) getApplicationContext()
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View rowView = inflater.inflate(R.layout.lv_row, parent, false);

                Phones curr = alPhones.get(position);

                TextView tv = rowView.findViewById(R.id.tvName);
                tv.setText(String.valueOf(curr.getName()));

                ImageView btnEdit = rowView.findViewById(R.id.btnEdit);
                btnEdit.setColorFilter(Color.BLACK, PorterDuff.Mode.MULTIPLY);

                btnEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Phones phone = alPhones.get(position);
                        Intent intent = new Intent(MainActivity.this, AddEditItemActivity.class);
                        intent.putExtra("phone", phone);
                        intent.putExtra("action", "edit");
                        startActivityForResult(intent, 1);
                    }
                });

                ImageView btnDelete = rowView.findViewById(R.id.btnDelete);
                btnDelete.setColorFilter(Color.BLACK, PorterDuff.Mode.MULTIPLY);

                btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        docRef = colRef.document(curr.getId());
                        docRef
                                .delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(getApplicationContext(), "Phone record deleted successfully", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(Exception e) {
                                        Toast.makeText(getApplicationContext(), "Failed to delete phone record", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });

                return rowView;
            }
        };
        lvPhones.setAdapter(aaPhones);

        colRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot value, FirebaseFirestoreException error) {
                if (error != null) {
                    return;
                }

                alPhones.clear();
                for (DocumentSnapshot doc : value) {
                    if (doc.get("name") != null && doc.get("cost") != null && doc.exists()) {
                        String name = doc.getString("name");

                        String a = String.valueOf(doc.getLong("cost"));
                        int cost = Integer.parseInt(a);
                        ArrayList<String> alAdditional = (ArrayList<String>) doc.get("options");

                        String docID = doc.getId();

                        Phones phone = new Phones(name, cost);
                        phone.setId(docID);
                        phone.setOptions(alAdditional);
                        alPhones.add(phone);
                    }
                }
                aaPhones.notifyDataSetChanged();
            }
        });

        lvPhones.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Phones phone = alPhones.get(i);
                Intent intent = new Intent(MainActivity.this, AddEditItemActivity.class);
                intent.putExtra("phone", phone);
                intent.putExtra("action", "edit");
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        // Handle action bar item clicks here.
        int id = item.getItemId();

        if (id == R.id.addItem) {

            Intent intent = new Intent(getApplicationContext(), AddEditItemActivity.class);
            intent.putExtra("action", "add");
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}