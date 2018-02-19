package com.example.alex.test.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.alex.test.message.MessageItem;

import java.util.List;

/**
 * Created by alex on 30.03.2017.
 */

public interface MessageModel {
    interface MessageModelCallback{
        void onFinished(@Nullable List<MessageItem> contList);
    }

    void getMessagesList(@NonNull MessageModel.MessageModelCallback callback);
}

