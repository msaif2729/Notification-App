package com.example.notification;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    private MaterialToolbar topbar;
    private Boolean theme = false;
    private SessionMaintain sessionMaintain;
    private TextInputEditText title,mssg;
    private Button notify;
    private ImageView img;
    Uri filename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1);
            }
        }

        NotificationHelper.createChannel(MainActivity.this);
        sessionMaintain = new SessionMaintain(MainActivity.this);
        if(sessionMaintain.getTheme("theme"))
        {
            setTheme(R.style.DarkTheme);
        }
        else {
            setTheme(R.style.LightTheme);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        if(getIntent().hasExtra("title"))
            Toast.makeText(this, "has", Toast.LENGTH_SHORT).show();



        topbar = (MaterialToolbar) findViewById(R.id.topbar);
        title = (TextInputEditText) findViewById(R.id.title);
        mssg = (TextInputEditText) findViewById(R.id.mssg);
        notify = (Button) findViewById(R.id.notify);
        img = (ImageView) findViewById(R.id.img);

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,2729);
            }
        });



        notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!title.getText().equals("") && !mssg.getText().equals("") && !filename.equals(""))
                {
                    Toast.makeText(MainActivity.this, "Notification Sended", Toast.LENGTH_SHORT).show();
                    NotificationHelper.sendNotification(MainActivity.this,title.getText().toString(),mssg.getText().toString(),filename);
                }
                else {
                    Toast.makeText(MainActivity.this, "Plz enter the data", Toast.LENGTH_SHORT).show();
                }
            }
        });

        setSupportActionBar(topbar);

        topbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                if(!sessionMaintain.getTheme("theme"))
                {
                    setTheme(R.style.DarkTheme);
                    sessionMaintain.changeTheme(true);
                    item.setIcon(MainActivity.this.getResources().getIdentifier("light","drawable",MainActivity.this.getPackageName()));
                }
                else {
                    setTheme(R.style.LightTheme);
                    sessionMaintain.changeTheme(false);
                    item.setIcon(MainActivity.this.getResources().getIdentifier("dark","drawable",MainActivity.this.getPackageName()));
                }

                Intent intent = getIntent();
                finish();
                startActivity(intent);
                Animatoo.INSTANCE.animateSlideRight(MainActivity.this);
                return true;
            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        MenuItem themeToggle = menu.findItem(R.id.changetheme);
        if (sessionMaintain.getTheme("theme")) {
            themeToggle.setIcon(R.drawable.light);
        } else {
            themeToggle.setIcon(R.drawable.dark);
        }
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2729 && resultCode == RESULT_OK && data != null) {
           filename = data.getData();
            Log.d("Mohammed Saif",String.valueOf(filename));
            try (InputStream inputStream = getContentResolver().openInputStream(filename)) {
                Bitmap bitmap =  BitmapFactory.decodeStream(inputStream);
                img.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            try (Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (nameIndex >= 0) {
                        result = cursor.getString(nameIndex);
                    }
                }
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

}