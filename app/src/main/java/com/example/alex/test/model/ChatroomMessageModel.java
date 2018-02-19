package com.example.alex.test.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.alex.test.chatroom.ChatroomMessageItem;

import java.util.List;

/**
 * Created by alex on 16.05.17.
 */

public interface ChatroomMessageModel {
    interface ChatroomMessageModelCallback{
        void onFinished(@Nullable List<ChatroomMessageItem> msgList);
    }

    void getMessagesList(String chatroomId, @NonNull ChatroomMessageModel.ChatroomMessageModelCallback callback);
}

