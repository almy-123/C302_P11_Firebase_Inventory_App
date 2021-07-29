package sg.edu.rp.id19037610.c302_p11_firebaseinventoryapp;

import com.google.firebase.firestore.Exclude;

import java.io.Serializable;
import java.util.ArrayList;

public class Phones implements Serializable {

    String name;
    int cost;
    String id;
    ArrayList<String> alAdditional;

    public Phones() {

    }

    public Phones(String name, int cost) {
        this.name = name;
        this.cost = cost;
    }

    public Phones(String name, int cost, ArrayList<String> alAdditional) {
        this.name = name;
        this.cost = cost;
        this.alAdditional = alAdditional;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public void setOptions(ArrayList<String> alAdditional) {
        this.alAdditional = alAdditional;
    }

    public ArrayList<String> getOptions() {
        return alAdditional;
    }

    @Exclude
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String toString() {
        return name;
    }
}
