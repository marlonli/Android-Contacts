package com.example.jingyuan.contacts;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements ContactsFragment.OnFragmentInteractionListener, DetailsFragment.OnFragmentInteractionListener, ProfileFragment.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public void onFragmentInteraction(Uri uri) {
//        DetailsFragment df = (DetailsFragment) getFragmentManager().findFragmentById(R.id.fragment_details);
//
//        if (df == null) {
//            Intent i = new Intent(this, DetailsActivity.class);
//
//        }
    }
}
