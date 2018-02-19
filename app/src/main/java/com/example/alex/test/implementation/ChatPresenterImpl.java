package com.example.alex.test.implementation;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.example.alex.test.chating.ChatActivity;
import com.example.alex.test.chating.Message;
import com.example.alex.test.contact.ContactItem;
import com.example.alex.test.model.ChatModel;
import com.example.alex.test.presenter.ChatPresenter;
import com.example.alex.test.view.MvpChatView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by alex on 08.05.17.
 */

public class ChatPresenterImpl implements ChatPresenter, ValueEventListener {

    private MvpChatView mView;
    private Context mContext;
    private ChatModelImpl mModel;
    public String mContactId;
    private DatabaseReference mReference;

    public ChatPresenterImpl(MvpChatView view, String contactId){
        mView = view;
        mContext = (Context)view;
        mModel = new ChatModelImpl();
        mContactId = contactId;
    }


    @Override
    public void generateMessageList() {
        if (mView == null) return;
        mModel.getMessagesList(mContactId, new ChatModel.ChatModelCallback(){

            @Override
            public void onFinished(@NonNull ContactItem contact, @Nullable List<Message> mesgList, DatabaseReference ref) {// get firebase messages list

                if (mesgList.isEmpty()) {
                    Toast.makeText(mContext, "Message list is empty",Toast.LENGTH_SHORT).show();
                }
                mReference = ref;
                addListener();
                mView.setMessagesList(mesgList);
            }
        });
    }

    private void addListener(){
        mReference.addValueEventListener(this);
    }

    public void addMessageToFirebase(int position, Message message){//add outcome message to firebae and activity
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();
        DatabaseReference myRef = database.getReference("contacts").child(userId).child("contact_messages").child(mContactId).child("" + position);
        DatabaseReference contactRef = database.getReference("contacts").child(mContactId).child("contact_messages").child(userId).child("" + position);
        myRef.setValue(message);
        Message msg = new Message(message.message, true, message.time);
        contactRef.setValue(msg);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) { //add new income message
        Iterator<DataSnapshot> reference = dataSnapshot.getChildren().iterator();
        DataSnapshot snapshot;
        if(!reference.hasNext()) return;
        do {
            snapshot = reference.next();
        } while (reference.hasNext());
        boolean type = snapshot.child("type").getValue(Boolean.class);
        if(!type) return;
        Message msg = snapshot.getValue(Message.class);
        mView.addMessage(msg);

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
