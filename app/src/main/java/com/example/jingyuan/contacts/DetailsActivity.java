package com.example.jingyuan.contacts;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

public class DetailsActivity extends AppCompatActivity implements DetailsFragment.OnFragmentInteractionListener{

    private static final int RESULT_SUCCESS = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
    }

    @Override
    protected void onResume() {
        super.onResume();
        List<Contact> list = new ArrayList<>();
        Intent intent = getIntent();

        // Communication with fragment
        int size = intent.getIntExtra("size", 0);
        for (int i = 0; i < size; i++) {
            Contact person = (Contact) intent.getSerializableExtra("person" + i);
            list.add(person);
        }

        DetailsFragment df = (DetailsFragment) getFragmentManager().findFragmentById(R.id.fragment_details);
        df.showDetails(list);
        Log.v("activity status", "DetailsActivity onResume");
    }

    @Override
    protected void onStop() {
        super.onStop();

        Log.v("activity status", "Details Activity Stopped");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.v("Activity State", " DetailsActivity Saving...");

        // Save list
        DetailsFragment df = (DetailsFragment) getFragmentManager().findFragmentById(R.id.fragment_details);
        List<Contact> listToBeSaved = df.getList();
        for (int i = 0; i < listToBeSaved.size(); i++) {
            outState.putSerializable("list" + i, listToBeSaved.get(i));
        }

        // Save EditText
        outState.putInt("size", listToBeSaved.size());
        outState.putString("name", df.getName());
        outState.putString("phone", df.getPhone());
    }

    @Override
    public void onRestoreInstanceState(Bundle inState) {
        super.onRestoreInstanceState(inState);
        Log.v("Activity State", "Details Activity Restoring");
        DetailsFragment df = (DetailsFragment) getFragmentManager().findFragmentById(R.id.fragment_details);

        // Restore text
        EditText name = (EditText) findViewById(R.id.editText_name);
        name.setText(inState.getString("name"));
        EditText phone = (EditText) findViewById(R.id.editText_phone);
        phone.setText(inState.getString("phone"));
//        adapter.notifyDataSetChanged();
    }



    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
