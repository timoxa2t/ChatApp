package com.example.alex.test.chating;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.example.alex.test.R;
import com.example.alex.test.implementation.ChatPresenterImpl;
import com.example.alex.test.presenter.ChatPresenter;
import com.example.alex.test.view.MvpChatView;

import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.List;

public class ChatActivity extends AppCompatActivity implements MvpChatView, View.OnClickListener {
    private RecyclerView mRecView;
    private ChatAdapter mChatAdapter;
    private Button btnSendMessage;
    private EditText etInputMessage;
    private LinearLayoutManager mLinLayManager;
    private ChatPresenter mChatPresenter;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initializeViews();

        if(mToolbar != null) {
            setSupportActionBar(mToolbar);
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            });
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mLinLayManager = new LinearLayoutManager(this);
        mRecView.setLayoutManager(mLinLayManager);
        mChatAdapter = new ChatAdapter(this);
        mRecView.setAdapter(mChatAdapter);
        btnSendMessage.setOnClickListener(this);

        Intent intent = getIntent();
        String contactId = intent.getStringExtra("ContactId");
        String name = intent.getStringExtra("name");
        byte[] bArray = intent.getByteArrayExtra("image");
        Bitmap icon = BitmapFactory.decodeByteArray(bArray, 0, bArray.length);
        setToolbarData(name, icon);
        mChatPresenter = new ChatPresenterImpl(this, contactId);
        mChatPresenter.generateMessageList();

    }

    private void initializeViews() {
        mRecView = (RecyclerView)findViewById(R.id.chat_rec_view);
        btnSendMessage = (Button) findViewById(R.id.btn_send_message);
        etInputMessage = (EditText) findViewById(R.id.et_input_message);
        mToolbar = (Toolbar) findViewById(R.id.chat_toolbar);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btn_send_message){
            if(TextUtils.isEmpty(etInputMessage.getText().toString())) return;
            String message = etInputMessage.getText().toString();
            long time = new Date().getTime();
            Message item = new Message(message, false, time);
            mChatAdapter.addMessage(item);
            mChatPresenter.addMessageToFirebase(mChatAdapter.getItemCount(), item);
            mLinLayManager.scrollToPosition(mChatAdapter.getItemCount());
            etInputMessage.setText("");
        }
    }

    @Override
    public void setMessagesList(List<Message> msgItem) {
        mChatAdapter.refreshMessagesList(msgItem);
        mLinLayManager.scrollToPosition(mChatAdapter.getItemCount());
    }

    @Override
    public void setToolbarData(String name, Bitmap image) {
        TextView cName = (TextView)findViewById(R.id.toolbar_contact_name);
        final ImageView icon = (ImageView) findViewById(R.id.toolbar_contact_icon);
        final Context context = getApplicationContext();
        ByteArrayOutputStream bs = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 10, bs);
        Glide.with(context)
                .load(bs.toByteArray())
                .asBitmap()
                .centerCrop()
                .into(new BitmapImageViewTarget(icon) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        icon.setImageDrawable(circularBitmapDrawable);
                    }
                });
        Log.d("lagTag", "mvpView");
        cName.setText(name);
    }

    @Override
    public void addMessage(Message msg) {
        mChatAdapter.addMessage(msg);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
