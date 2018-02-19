package com.example.alex.test.view;

import com.example.alex.test.chatroom.ChatroomItem;

import java.util.List;

/**
 * Created by alex on 30.03.2017.
 */

public interface MvpChatroomView {
    void setChatroomsList(List<ChatroomItem> chatrItem);
    void showProgress();
    void hideProgress();
}
