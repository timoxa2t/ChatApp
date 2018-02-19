package com.example.alex.test.implementation;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.example.alex.test.R;
import com.example.alex.test.chatroom.ChatroomItem;
import com.example.alex.test.contact.ContactItem;
import com.example.alex.test.model.ChatroomModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by alex on 30.03.2017.
 */

public class ChatroomModelImpl implements ChatroomModel, ValueEventListener{
List<ChatroomItem> mChatroomList = new ArrayList<>();
Context mContext;

public ChatroomModelImpl(Context context){mContext = context;}

    public void getContactsList(@NonNull final ChatroomModel.ChatroomModelCallback callback) {
    getFirebaseData();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                callback.onFinished(mChatroomList);
            }
        },3000);
    }


    public void getFirebaseData() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference contactRef = database.getReference("chatrooms");
        Log.d("TAGA", "getFirebaseChatroomData");
        contactRef.addValueEventListener(this);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        Log.d("TAGA", "onDataChange");
        if(!dataSnapshot.hasChildren()) return;
        Log.d("TAGA", "hasChildren");
        List<ChatroomItem> list = new ArrayList<>();
        Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        do{
            DataSnapshot itemData = iterator.next();
            String id = itemData.getKey();
            if(TextUtils.equals(id, user.getUid())) continue;
            String name = itemData.child("name").getValue(String.class);
            int peopleCount = itemData.child("peopleCount").getValue(Integer.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                list.add(new ChatroomItem(id, name,peopleCount));
            }else{
                list.add(new ChatroomItem(id, name,peopleCount));
            }
        }
        while (iterator.hasNext());
        mChatroomList = (ArrayList)list;
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        Toast.makeText(mContext, "Error",Toast.LENGTH_SHORT).show();
    }
}

