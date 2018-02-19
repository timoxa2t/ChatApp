package com.example.alex.test.view;

import com.example.alex.test.chatroom.ChatroomMessageItem;

import java.util.List;

/**
 * Created by alex on 16.05.17.
 */

public interface MvpChatroomMessageView {
        void setMessagesList(List<ChatroomMessageItem> msgItem);
}
