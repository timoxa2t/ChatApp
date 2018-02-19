package com.example.alex.test.contact;


import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.util.Date;
import java.util.GregorianCalendar;


/**
 * Created by alex on 28.03.2017.
 */

public class ContactItem {
    public String contactId;
    public String name;
    public long time;
    public String message;
    public Bitmap icon;

    public ContactItem(@NonNull String id, @NonNull String name, long time, String message){
        contactId = id;
        this.name = name;
        this.time = time;
        if(TextUtils.isEmpty(message)) {
            this.message = message;
        }
        if(icon != null) {
            this.icon = icon;
        }
    }

    public ContactItem(String contact) {
        name = contact;
    }

    public ContactItem(String contact, Bitmap icon) {
        name = contact;
        this.icon = icon;
    }
    public ContactItem(){}
}
