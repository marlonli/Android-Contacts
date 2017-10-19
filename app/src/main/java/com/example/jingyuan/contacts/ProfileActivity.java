package com.example.jingyuan.contacts;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity implements ProfileFragment.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        int size = intent.getIntExtra("listsize", 1);
        int show = intent.getIntExtra("show", 0);
        List<Contact> cs = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            Contact c = (Contact) intent.getSerializableExtra("listitem" + i);
            cs.add(c);
        }
        Contact person = cs.get(show);
        ProfileFragment pf = (ProfileFragment) getFragmentManager().findFragmentById(R.id.fragment_profile);
        pf.showProfile(person);
        pf.contacts.addAll(cs);


    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
