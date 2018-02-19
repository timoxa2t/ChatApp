package com.example.alex.test.autorization;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alex.test.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private SharedPreferences mSharedPref;
    private EditText mEmail;
    private EditText mPassword;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mSharedPref = this.getSharedPreferences("UserData", MODE_PRIVATE);
        setFirebaseAuthorization();

        findViewById(R.id.btnRegister).setOnClickListener(this);
        findViewById(R.id.btnSingIn).setOnClickListener(this);

        mEmail = (EditText)findViewById(R.id.email);
        mPassword = (EditText)findViewById(R.id.password);

        loadPreviousUser();

    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    private void setFirebaseAuthorization(){

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("TAG", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d("TAG", "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.btnRegister:
                Intent intent = new Intent(this, RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.btnSingIn:
                checkUser();
                break;
        }

    }

    private void checkUser(){
        final String email = mEmail.getText().toString();
        final String password = mPassword.getText().toString();
        if(TextUtils.isEmpty(email)|| TextUtils.isEmpty(password)){
            Toast.makeText(this, "Complete the fields", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("TAG", "signInWithEmail:onComplete:" + task.isSuccessful());

                        if (!task.isSuccessful()) {
                            Log.w("TAG", "signInWithEmail:failed", task.getException());
                            Toast.makeText(LoginActivity.this, R.string.auth_failed,
                                    Toast.LENGTH_SHORT).show();
                        }else{
                            SharedPreferences.Editor editor = mSharedPref.edit();
                            editor.putString("email", email);
                            editor.putString("password", password);
                            editor.commit();
                            Intent intent = new Intent();
                            intent.putExtra("sign in", true);
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    }
                });
    }

//    private void checkUser(){
//
//        //TODO check user from database
//        if(TextUtils.isEmpty(mEmail.getText()) || TextUtils.isEmpty(mPassword.getText())) return;
//            SharedPreferences.Editor editor = mSharedPref.edit();
//            editor.putString("email", mEmail.getText() + "");
//            editor.putString("password", mPassword.getText() + "");
//            editor.putBoolean("login", true);
//            editor.commit();
//            Intent intent = new Intent();
//            intent.putExtra("sign in", true);
//            setResult(RESULT_OK, intent);
//            finish();
//
//
//    }

    private void loadPreviousUser() {
        String email = mSharedPref.getString("email", "");
        Log.d("TAG", "Password: "  + mSharedPref.getString("password", ""));
        mEmail.setText(email);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}

