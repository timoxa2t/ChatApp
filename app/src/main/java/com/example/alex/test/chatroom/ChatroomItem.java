package com.example.alex.test.chatroom;

import android.graphics.drawable.Drawable;

/**
 * Created by alex on 30.03.2017.
 */

public class ChatroomItem {
    public String chatroomId;
    public String chatroomName;
    public int peopleCount = 0;
    public String description;
    public Drawable picture;

    public ChatroomItem(String chatroomId, String name, int peopleCount){
        chatroomName = name;
        this.peopleCount = peopleCount;
        this.chatroomId = chatroomId;
    }
}
