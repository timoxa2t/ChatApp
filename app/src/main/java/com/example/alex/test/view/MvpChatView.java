package com.example.alex.test.view;

import android.graphics.Bitmap;

import com.example.alex.test.chating.Message;

import java.util.List;

/**
 * Created by alex on 07.05.17.
 */

public interface MvpChatView {
    void setMessagesList(List<Message> contItem);
    void setToolbarData(String name, Bitmap icon);

    void addMessage(Message msg);
}
