package com.example.alex.test.implementation;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.example.alex.test.chating.Message;
import com.example.alex.test.chatroom.ChatroomMessageItem;
import com.example.alex.test.model.ChatroomMessageModel;
import com.example.alex.test.presenter.ChatroomMessagePresenter;
import com.example.alex.test.view.MvpChatroomMessageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

/**
 * Created by alex on 16.05.17.
 */

public class ChatroomMessagePresenterImpl implements ChatroomMessagePresenter{
    private MvpChatroomMessageView mView;
    private Context mContext;
    private ChatroomMessageModelImpl mModel;
    private String mChatroomId;

    public ChatroomMessagePresenterImpl(MvpChatroomMessageView view, String chatroomId){
        mView = view;
        mContext = (Context)view;
        mModel = new ChatroomMessageModelImpl();
        mChatroomId = chatroomId;
    }

    @Override
    public void generateMessageList() {
        if (mView == null) return;
        mModel.getMessagesList(mChatroomId, new ChatroomMessageModel.ChatroomMessageModelCallback(){

            @Override
            public void onFinished(@Nullable List<ChatroomMessageItem> mesgList) {

                if (mesgList.isEmpty()) {
                    Toast.makeText(mContext, "Message list is empty",Toast.LENGTH_SHORT).show();
                }
                mView.setMessagesList(mesgList);
            }
        });
    }
    public void addMessageToFirebase(int position, ChatroomMessageItem message){//add outcome message to firebae and activity
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String userId = ChatroomMessageItem.USER_ID;
        DatabaseReference myRef = database.getReference("chatrooms").child(mChatroomId).child("chatroom_messages").child("" + position);
        myRef.setValue(message);
    }
}
