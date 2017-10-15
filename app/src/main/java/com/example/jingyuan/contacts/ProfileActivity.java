package com.example.jingyuan.contacts;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

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
        Contact person = (Contact) intent.getSerializableExtra("person");
        ProfileFragment pf = (ProfileFragment) getFragmentManager().findFragmentById(R.id.fragment_profile);
        pf.showProfile(person);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
