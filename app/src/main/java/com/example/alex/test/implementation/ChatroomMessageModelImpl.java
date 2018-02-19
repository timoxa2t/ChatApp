package com.example.alex.test.implementation;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.alex.test.chating.Message;
import com.example.alex.test.chatroom.ChatroomItem;
import com.example.alex.test.chatroom.ChatroomMessageItem;
import com.example.alex.test.contact.ContactItem;
import com.example.alex.test.model.ChatroomMessageModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

/**
 * Created by alex on 16.05.17.
 */

public class ChatroomMessageModelImpl implements ChatroomMessageModel, ValueEventListener {
    private DatabaseReference mChatroomMessages;
    List<ChatroomMessageItem> mChatroomMessageList = new ArrayList<>();


    public void getMessagesList(final String chatroomId, @NonNull final ChatroomMessageModel.ChatroomMessageModelCallback callback) {
    getFirebaseMessageList(chatroomId);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                callback.onFinished(mChatroomMessageList);
            }
        },3000);
    }


    public void getFirebaseMessageList(String chatroomId) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mChatroomMessages = database.getReference("chatrooms").child(chatroomId).child("chatroom_messages");
        database.getReference("chatrooms").child(chatroomId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mChatroomMessages.addListenerForSingleValueEvent(this);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        if(!dataSnapshot.hasChildren()){
            return;
        }
        List<ChatroomMessageItem> list = new ArrayList<>();
        Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();
        do{
            DataSnapshot itemData = iterator.next();
            ChatroomMessageItem msg = itemData.getValue(ChatroomMessageItem.class);
            list.add(msg);
        }
        while (iterator.hasNext());
        mChatroomMessageList = (ArrayList)list;
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}