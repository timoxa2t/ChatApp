package com.example.alex.test.implementation;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.alex.test.chating.Message;
import com.example.alex.test.contact.ContactItem;
import com.example.alex.test.model.ChatModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;


/**
 * Created by alex on 08.05.17.
 */

public class ChatModelImpl implements ChatModel, ValueEventListener {
    private DatabaseReference mContactMessages;
    private List<Message> mMessagesList = new ArrayList<>();
    public ContactItem mContact;

    public void getMessagesList(@NonNull final String contactId, @NonNull final ChatModel.ChatModelCallback callback) {

        getFirebaseMessagesList(contactId);
        new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    callback.onFinished(mContact, mMessagesList, mContactMessages);
                }
            },3000);

        }


    public void getFirebaseMessagesList(String contactId) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mContactMessages = database.getReference("contacts").child(userId).child("contact_messages").child(contactId);
        database.getReference("contacts").child(contactId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue(String.class);
                mContact = new ContactItem(name);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mContactMessages.addListenerForSingleValueEvent(this);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        if(!dataSnapshot.hasChildren()){
            return;
        }
        List<Message> list = new ArrayList<>();
        Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();
        do{
            DataSnapshot itemData = iterator.next();
            Message msg = itemData.getValue(Message.class);
            list.add(msg);
        }
        while (iterator.hasNext());
        Log.d("incomeMessage", "new message");
        mMessagesList = (ArrayList)list;
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
