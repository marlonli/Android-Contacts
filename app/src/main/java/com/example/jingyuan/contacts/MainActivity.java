package com.example.jingyuan.contacts;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.EditText;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ContactsFragment.OnFragmentInteractionListener, DetailsFragment.OnFragmentInteractionListener, ProfileFragment.OnFragmentInteractionListener {

    private static final int RESULT_SUCCESS = 0;
    List<Contact> contacts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.v("activity Status", "onCreate");

        SharedPreferences sp = getSharedPreferences("contacts", MODE_PRIVATE);
        int size = sp.getInt("size", 0);
        Log.v("activity Status", "contacts size" + size);
        contacts = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            String contactString = sp.getString("contact" + i, "");
            byte[] base64Contact = Base64.decode(contactString, Base64.DEFAULT);
            ByteArrayInputStream bais = new ByteArrayInputStream(base64Contact);
            ObjectInputStream ois = null;
            try {
                ois = new ObjectInputStream(bais);
                Contact c;
                try {
                    c = (Contact) ois.readObject();
                    contacts.add(c);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                bais.close();
                ois.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        ContactsFragment cf = (ContactsFragment) getFragmentManager().findFragmentById(R.id.fragment_contacts);
        cf.list.addAll(contacts);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v("activity Status", "onResume");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.v("activity Status", "onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.v("activity Status", "onStop");
        SharedPreferences sp = getSharedPreferences("contacts", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        // save contacts
        ContactsFragment cf = (ContactsFragment) getFragmentManager().findFragmentById(R.id.fragment_contacts);
        List<Contact> contacts = cf.list;
        editor.putInt("size", contacts.size());
        for (int i = 0; i < contacts.size(); i++) {
            Contact c = contacts.get(i);
            editor.putString("contactName" + i, c.getName());
            editor.putString("contactPhone" + i, c.getPhone());
            List<String> relation = c.getRelationshipString();
            editor.putInt("contact" + i + "RelaSize", relation.size());
            for (int j = 0; j < relation.size(); j++) {
                editor.putString("contact" + i + "Rela" + j, relation.get(j));
            }
        }

        for (int i = 0; i < contacts.size(); i++) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos;
            try {
                oos = new ObjectOutputStream(baos);
                oos.writeObject(contacts.get(i));
                String base64Contact = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
                editor.putString("contact" + i, base64Contact);
                baos.close();
                oos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        editor.commit();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

//        //save list
//        ContactsFragment cf = (ContactsFragment) getFragmentManager().findFragmentById(R.id.fragment_contacts);
//        List<Contact> listToBeSaved = cf.list;
//        outState.putInt("size", listToBeSaved.size());
//        for (int i = 0; i < listToBeSaved.size(); i++) {
//            outState.putSerializable("savedContact" + i, listToBeSaved.get(i));
//        }
//        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            // store which fragment is active when in landscape
//            DetailsFragment df = (DetailsFragment) getFragmentManager().findFragmentByTag("Details");
//            ProfileFragment pf = (ProfileFragment) getFragmentManager().findFragmentByTag("Profile");
//            if (df != null && df.isVisible()) {
//                Log.v("fragment Contact status", "Details!!!");
//                outState.putInt("frags", 1);
//                EditText name = (EditText) findViewById(R.id.editText_name);
//                String names = name.getText().toString();
//                EditText phone = (EditText) findViewById(R.id.editText_phone);
//                String phones = phone.getText().toString();
//                outState.putString("name", names);
//                outState.putString("phone", phones);
//                outState.putInt("relation size", df.list.size());
//                for (int i = 0; i < df.list.size(); i++) {
//                    outState.putSerializable("relation" + i, df.list.get(i));
//                }
//            }
//            else if (pf != null && pf.isVisible()) {
//                Log.v("fragment Contact status", "Profile!!!!!!!");
//                outState.putInt("frags", 2);
//                // save contact list and which list to show profile
//                int show = 0;
//                for (int j = 0; j < cf.list.size(); j++) {
//                    if (cf.list.get(j).getName().equals(pf.contact.getName())){
//                        show = j;
//                        break;
//                    }
//                }
//                outState.putInt("listsize", cf.list.size());
//                outState.putInt("show", show);
//                for (int j = 0; j < cf.list.size(); j++) {
//                    outState.putSerializable("listitem" + j, cf.list.get(j));
//                    Log.v("profile act list item " + j, " " + cf.list.get(j).getName());
//                }
//            }
//            else {
//                outState.putInt("frags", 0);
//                Log.v("fragment Contact status", "Nothing!!!!");
//            }
//        }

    }

    @Override
    public void onRestoreInstanceState(Bundle inState) {
        super.onRestoreInstanceState(inState);
//        ContactsFragment cf = (ContactsFragment) getFragmentManager().findFragmentById(R.id.fragment_contacts);
//
//        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
//            int frags = inState.getInt("frags");
//            if (frags == 1) { // show details
//                Intent intent = new Intent(this, DetailsActivity.class);
//                int size = inState.getInt("relation size");
//                intent.putExtra("size", size);
//
//                intent.putExtra("name", inState.getString("name"));
//                intent.putExtra("phone", inState.getString("phone"));
//                for (int i = 0; i < size; i++)
//                    intent.putExtra("person" + i, inState.getSerializable("relation" + i));
//                startActivityForResult(intent, Activity.RESULT_FIRST_USER);
//            } else if (frags == 2) { // show profile
//                Intent intent = new Intent(this, ProfileActivity.class);
//                int size = inState.getInt("listsize");
//                intent.putExtra("listsize", size);
//                intent.putExtra("show", inState.getInt("show"));
//                for (int i = 0; i < size; i++) {
//                    intent.putExtra("listitem" + i, inState.getSerializable("listitem" + i));
//                }
//
//                startActivity(intent);
//            }
//        }
//
//        // Restore list
//        int size = inState.getInt("size");
//        List<Contact> listToBeRestored = new ArrayList<>();
//        for (int i = 0; i < size; i++) {
//            listToBeRestored.add((Contact) inState.getSerializable("savedContact" + i));
//        }
//        cf.list.addAll(listToBeRestored);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.v("fragment contact status", "onActivityResult");
        if (requestCode == Activity.RESULT_FIRST_USER && data != null) {

            if (resultCode == RESULT_SUCCESS) {
                ContactsFragment cf = (ContactsFragment) getFragmentManager().findFragmentById(R.id.fragment_contacts);
                Contact c = (Contact) data.getSerializableExtra("addPerson");
                cf.list.add(c);
                int size = data.getIntExtra("size", 0);
                for (int i = 0; i < size; i++) {
                    String contact = data.getStringExtra("addPerson" + i);
                    for (int j = 0; j < cf.list.size(); j++) {
                        if (cf.list.get(j).getName().equals(contact))
                            cf.list.get(j).setRelationship(c);
                    }

                }
                cf.madapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.v("activity Status", "onDestroy");

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
