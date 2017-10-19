package com.example.jingyuan.contacts;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

public class ShowImageActivity extends AppCompatActivity {

    private ImageView iv;
    private Uri photoOutputUri = null;
    public static final String TRANSIT_PIC = "picture";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image);

        iv = (ImageView) findViewById(R.id.imageView);
        parseIntent();
        ViewCompat.setTransitionName(iv, TRANSIT_PIC);
        setImage();
    }

    private void setImage() {
        File file = new File(photoOutputUri.getPath());
        if(file.exists()) {
            Log.v("capture", "onActivityResult" + photoOutputUri.getPath());
            Bitmap bp = BitmapFactory.decodeFile(photoOutputUri.getPath());
            iv.setImageBitmap(bp);

        } else {
            Toast.makeText(this, "Image not found!", Toast.LENGTH_SHORT).show();
        }
    }

    public static Intent newIntent(Context context, String imageName) {
        Intent intent = new Intent(context, ShowImageActivity.class);
        intent.putExtra("image", imageName);
        return intent;
    }


    private void parseIntent() {
        String imageName = getIntent().getStringExtra("image");
        photoOutputUri = Uri.parse("file:////sdcard/" + imageName + ".jpg");
    }
}
