package com.example.jingyuan.contacts;

import android.net.Uri;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jingyuan on 10/11/17.
 */

public class Contact implements Serializable {
    private String name;
    private String phone;
    private boolean checked;
    private List<Contact> relationship;
    private List<String> relationshipString;

    public Contact(String name, String phone) {
        this.name = name;
        this.phone = phone;
        this.checked = false;
        this.relationship = new ArrayList<>();
        this.relationshipString = new ArrayList<>();
    }


    public List<String> getRelationshipString() {
        return this.relationshipString;
    }

    public void setRelationship(Contact person) {
        relationship.add(person);
        relationshipString.add(person.getName());
    }

    public void deleteRelationship(String name) {
        if (relationshipString.contains(name)) {
            List<String> deleteString = new ArrayList<>();
            List<Contact> delete = new ArrayList<>();
            for (Contact c : relationship)
                if (name.equals(c.getName())) {
                    deleteString.add(c.getName());
                    delete.add(c);
                }
            relationshipString.removeAll(deleteString);
            relationship.removeAll(delete);
        }

    }

    public String getName() {
        return this.name;
    }

    public String getPhone() {
        return this.phone;
    }

    public boolean getChecked() {
        return this.checked;
    }

    public void setChecked(boolean state) {
        this.checked = state;
    }
}
