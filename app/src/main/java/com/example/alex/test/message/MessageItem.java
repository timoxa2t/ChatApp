package com.example.alex.test.message;

import android.graphics.drawable.Drawable;

/**
 * Created by alex on 30.03.2017.
 */

public class MessageItem {
    public int id;
    public String name;
    public String time;
    public String message;
    public Drawable icon;

    public MessageItem(String name, String message, int id){
        this.name = name;
        this.message = message;
        this.id = id;
    }
}
