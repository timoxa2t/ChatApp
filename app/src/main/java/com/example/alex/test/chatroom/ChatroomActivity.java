package com.example.alex.test.chatroom;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.alex.test.R;
import com.example.alex.test.contact.ContactItem;
import com.example.alex.test.implementation.ChatroomMessagePresenterImpl;
import com.example.alex.test.presenter.ChatroomMessagePresenter;
import com.example.alex.test.view.MvpChatroomMessageView;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class ChatroomActivity extends AppCompatActivity implements MvpChatroomMessageView, View.OnClickListener{

    private RecyclerView mRecView;
    private ChatroomMessageAdapter mChatroomMessageAdapter;
    private Button btnSendMessage;
    private EditText etInputMessage;
    private LinearLayoutManager mLinLayManager;
    private ChatroomMessagePresenter mChatroomMessagePresenter;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initializeViews();
        setSupportActionBar(mToolbar);

        mLinLayManager = new LinearLayoutManager(this);
        mRecView.setLayoutManager(mLinLayManager);
        mChatroomMessageAdapter = new ChatroomMessageAdapter(this);
        mRecView.setAdapter(mChatroomMessageAdapter);
        btnSendMessage.setOnClickListener(this);
        Intent intent = getIntent();
        String chatroomId = intent.getStringExtra("ChatroomId");
        mChatroomMessagePresenter = new ChatroomMessagePresenterImpl(this, chatroomId);
        mChatroomMessagePresenter.generateMessageList();
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
            mChatroomMessageAdapter.addMessage(new ChatroomMessageItem(message, time, ChatroomMessageItem.USER_ID));
            mLinLayManager.scrollToPosition(mChatroomMessageAdapter.getItemCount()-1);
            etInputMessage.setText("");
        }
    }

    @Override
    public void setMessagesList(List<ChatroomMessageItem> msgItem) {
        mChatroomMessageAdapter.refreshMessagesList(msgItem);
    }
}
