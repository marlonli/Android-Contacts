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
        DetailsFragment df = (DetailsFragment) getFragmentManager().findFragmentById(R.id.fragment_details);

        Intent intent = getIntent();

        // Communication with fragment
        int size = intent.getIntExtra("size", 0);
        for (int i = 0; i < size; i++) {
            Contact person = (Contact) intent.getSerializableExtra("person" + i);
            df.list.add(person);
        }

        if (intent.getStringExtra("name") != null) {
            EditText name = (EditText)findViewById(R.id.editText_name);
            name.setText(intent.getStringExtra("name"));
        }
        if (intent.getStringExtra("phone") != null) {
            EditText phone = (EditText)findViewById(R.id.editText_phone);
            phone.setText(intent.getStringExtra("phone"));
        }
//        df.showDetails(list);

        Log.v("activity status", "DetailsActivity onCreate");
    }

    @Override
    protected void onStart() {
        super.onStart();


        Log.v("activity status", "DetailsActivity onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();

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
        DetailsFragment df = (DetailsFragment) getFragmentManager().findFragmentById(R.id.fragment_details);
        // Save image name
        outState.putString("image", df.imageName);

        // Save list
        List<Contact> listToBeSaved = df.getList();
        outState.putInt("size", listToBeSaved.size());
        for (int i = 0; i < listToBeSaved.size(); i++) {
            outState.putSerializable("list" + i, listToBeSaved.get(i));
        }
    }

    @Override
    public void onRestoreInstanceState(Bundle inState) {
        super.onRestoreInstanceState(inState);
        Log.v("Activity State", "Details Activity Restoring");
        // Restore list
        int size = inState.getInt("size");
        DetailsFragment df = (DetailsFragment) getFragmentManager().findFragmentById(R.id.fragment_details);
        df.list.clear();
        List<Contact> restoreList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            restoreList.add((Contact) inState.getSerializable("list" + i));
        }
        df.list.addAll(restoreList);

        // Restore image
        df.imageName = inState.getString("image");
        df.photoOutputUri = Uri.parse("file:////sdcard/" + df.imageName + ".jpg");
        df.showImage();
    }



    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
