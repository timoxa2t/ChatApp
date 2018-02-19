package com.example.alex.test;

import android.app.SearchManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alex.test.autorization.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends ActionBarActivity {
    private static final int SIGN_IN = 0;
    private static final String TAG = "TAG";

    private FragmentAdapter mFragmentAdapter;
    private TabLayout mTabLayout;
    private SharedPreferences mSharedPreferences;
    FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String mUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    mUserId = user.getUid();
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
        isLoggedIn();
        Log.d("refresh", "onCreate main");

        setTabs();
        mFragmentAdapter = new FragmentAdapter(getSupportFragmentManager());
        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPager.setAdapter(mFragmentAdapter);

        mTabLayout.setupWithViewPager(viewPager);

        handleIntent(getIntent());
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    private void setTabs() {
        mTabLayout = (TabLayout) findViewById(R.id.tv_tab_layout);
        mTabLayout.addTab(mTabLayout.newTab().setText("Contacts"));
        mTabLayout.addTab(mTabLayout.newTab().setText("Chat Rooms"));
        mTabLayout.addTab(mTabLayout.newTab().setText("Messages"));
    }

    public void isLoggedIn(){
        mSharedPreferences = this.getSharedPreferences("UserData", MODE_PRIVATE);
        boolean singIn = mSharedPreferences.getBoolean("Sign in", false);
        mAuth = FirebaseAuth.getInstance();
        if(singIn ){
            String email = mSharedPreferences.getString("email", "");
            String password = mSharedPreferences.getString("password", "");
            Log.d("TAG", "email " + email + "    password " + password);
            if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
                final Intent intent = new Intent(this, LoginActivity.class);
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Log.d("TAG", "signInWithEmail:onComplete:" + task.isSuccessful());
                                if (!task.isSuccessful()) {
                                    Log.w("TAG", "signInWithEmail:failed", task.getException());
                                    Toast.makeText(MainActivity.this, R.string.auth_failed,
                                            Toast.LENGTH_SHORT).show();
                                    startActivityForResult(intent, SIGN_IN);
                                }
                            }
                        });
            }
            return;
        }
            Intent intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent, SIGN_IN);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) return;
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        boolean login = data.getBooleanExtra("sign in", false);
        editor.putBoolean("Sign in", login);
        editor.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
        }
    }

    public String getCurrentUserId(){
        return mUserId;
    }

}
