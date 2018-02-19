package com.example.alex.test.autorization;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.graphics.BitmapCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.alex.test.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{
    private FirebaseAuth mAuth;
    private StorageReference mStorageRef;
    private EditText etName;
    private EditText etPassword;
    private EditText etEmail;
    private Button btnCreate;
    private ImageView ivPhoto;
    private Bitmap mBitmap;
    private static final int PICK_IMAGE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initializeViews();
        setImageParams();
    }

    private void setImageParams() {
        if(mBitmap != null) {
            ByteArrayOutputStream bs = new ByteArrayOutputStream();
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 50, bs);
            Glide.with(this)
                    .load(bs.toByteArray())
                    .asBitmap()
                    .centerCrop()
                    .into(ivPhoto);

        }else {
            BitmapDrawable bd = (BitmapDrawable) getResources().getDrawable(R.drawable.ic_launcher);
            Bitmap bitmap = bd.getBitmap();
            ByteArrayOutputStream bs = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bs);
            Glide.with(this)
                    .load(bs.toByteArray())
                    .asBitmap()
                    .fitCenter()
                    .into(ivPhoto);
        }

    }

    private void initializeViews() {
        etName = (EditText) findViewById(R.id.et_name);
        etEmail = (EditText) findViewById(R.id.et_email);
        etPassword = (EditText) findViewById(R.id.et_password);
        ivPhoto = (ImageView) findViewById(R.id.iv_photo);
        btnCreate = (Button) findViewById(R.id.btn_create);

        ivPhoto.setOnClickListener(this);
        btnCreate.setOnClickListener(this);
    }


    private void addUser(String email, String password, final String name){
        mAuth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference().child("ChatApp");
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("TAG", "createUserWithEmail:onComplete:" + task.isSuccessful());


                        if (!task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, R.string.auth_failed,
                                    Toast.LENGTH_SHORT).show();
                        }
                        else {
                            addDatabaseContactReference(name);
                        }

                        // ...
                    }
                });

    }

    private void addDatabaseContactReference(String name){
        FirebaseUser user = mAuth.getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference newUserRef = database.getReference("contacts").child(user.getUid());
        newUserRef.child("name").setValue(name);
        newUserRef.child("time").setValue(new Date().getTime());
        String id = user.getUid();
        StorageReference bigImgRef = mStorageRef.child(id + ".jpg");
        ByteArrayOutputStream bigStream = new ByteArrayOutputStream();
        mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bigStream);
        byte[] firstByteArray = bigStream.toByteArray();
        bigImgRef.putBytes(firstByteArray).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                onBackPressed();
            }
        });

        StorageReference smallImgRef = mStorageRef.child(id + "_small.jpg");
        ByteArrayOutputStream smallStream = new ByteArrayOutputStream();
        int size = BitmapCompat.getAllocationByteCount(mBitmap);
        int quality = 100000000 / size;
        mBitmap.compress(Bitmap.CompressFormat.JPEG, quality, smallStream);
        byte[] secondByteArray = smallStream.toByteArray();
        smallImgRef.putBytes(secondByteArray);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_photo:
                Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                getIntent.setType("image/*");

                Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickIntent.setType("image/*");

                Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

                startActivityForResult(chooserIntent, PICK_IMAGE);
                break;
            case R.id.btn_create:
                String name = etName.getText().toString();
                if(TextUtils.isEmpty(name)){
                    Toast.makeText(this, "Enter name", Toast.LENGTH_SHORT).show();
                    return;
                }
                String email = etEmail.getText().toString();
                if(TextUtils.isEmpty(email)){
                    Toast.makeText(this, "Enter eamil", Toast.LENGTH_SHORT).show();
                    return;
                }
                String password = etPassword.getText().toString();
                if(TextUtils.isEmpty(password)){
                    Toast.makeText(this, "Enter password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(password.length() < 6){
                    Toast.makeText(this, "Password must contain no less than six letters", Toast.LENGTH_SHORT).show();
                    return;
                }
                addUser(email, password, name);
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE && resultCode == RESULT_OK){
            try {
                InputStream imageInputStream = getContentResolver().openInputStream(data.getData());
                mBitmap = BitmapFactory.decodeStream(imageInputStream);
                ivPhoto.setImageBitmap(mBitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
