package com.example.notification;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;


public class MainActivity2 extends AppCompatActivity {

    private TextInputEditText title,mssg;
    private MaterialToolbar topbar;
    private ImageView img;
    private SessionMaintain sessionMaintain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        sessionMaintain = new SessionMaintain(MainActivity2.this);
        if(sessionMaintain.getTheme("theme"))
        {
            setTheme(R.style.DarkTheme);
        }
        else {
            setTheme(R.style.LightTheme);
        }        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        title = (TextInputEditText) findViewById(R.id.title);
        mssg = (TextInputEditText) findViewById(R.id.mssg);
        img = (ImageView) findViewById(R.id.img);
        topbar = (MaterialToolbar) findViewById(R.id.topbar);

        topbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        title.setText(getIntent().getStringExtra("title"));
        mssg.setText(getIntent().getStringExtra("mssg"));


//        byte[] decodedBytes = Base64.decode(getIntent().getStringExtra("image"), Base64.DEFAULT);
//        Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
//        img.setImageBitmap(bitmap);

        String uriString = getIntent().getStringExtra("image");
        if (uriString != null) {
            Uri uri = Uri.parse(uriString);
            img.setImageURI(uri);
        } else {
            Log.d("MainActivity2", "Image URI is null");
        }



    }
}