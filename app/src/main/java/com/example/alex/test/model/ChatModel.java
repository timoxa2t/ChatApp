package com.example.alex.test.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.alex.test.chating.Message;
import com.example.alex.test.contact.ContactItem;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

/**
 * Created by alex on 07.05.17.
 */

public interface ChatModel {

    interface ChatModelCallback{
        void onFinished(@NonNull ContactItem contact, @Nullable List<Message> contList, @NonNull DatabaseReference ref);
    }

    void getMessagesList(@NonNull String contactId, @NonNull ChatModel.ChatModelCallback callback);

}
