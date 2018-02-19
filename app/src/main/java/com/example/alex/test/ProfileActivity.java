package com.example.alex.test;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;


public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ImageView profileImage = (ImageView) findViewById(R.id.profile_image);

        if(getIntent().hasExtra("image")) {
            byte[] byteArray = getIntent().getByteArrayExtra("image");
            Glide.with(this)
                    .load(byteArray)
                    .asBitmap()
                    .centerCrop()
                    .into(profileImage);
        }
        TextView profileName = (TextView)findViewById(R.id.profile_name);
        profileName.setText(getIntent().getExtras().getString("name"));
    }
}
