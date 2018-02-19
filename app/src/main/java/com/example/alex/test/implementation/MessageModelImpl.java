package com.example.alex.test.implementation;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;

import com.example.alex.test.message.MessageItem;
import com.example.alex.test.model.MessageModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alex on 30.03.2017.
 */

public class MessageModelImpl implements MessageModel {
    Context mContext;

    public MessageModelImpl(Context context) {
        mContext = context;
    }

    public void getMessagesList(@NonNull final MessageModel.MessageModelCallback callback) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                callback.onFinished(createMessagesList());
            }
        },1000);

    }
    public List<MessageItem> createMessagesList(){
        List<MessageItem> simpleMessagesList = new ArrayList<>();

        for(int i = 0; i < 20; i++) {
            simpleMessagesList.add(new MessageItem("pesron " + i, "Message", i));
        }

        return simpleMessagesList;
    }

}

