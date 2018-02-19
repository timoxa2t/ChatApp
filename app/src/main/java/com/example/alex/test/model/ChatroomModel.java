package com.example.alex.test.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.alex.test.chatroom.ChatroomItem;

import java.util.List;

/**
 * Created by alex on 30.03.2017.
 */

public interface ChatroomModel {
    interface ChatroomModelCallback{
        void onFinished(@Nullable List<ChatroomItem> chatrList);
    }

    void getContactsList(@NonNull ChatroomModel.ChatroomModelCallback callback);
}
