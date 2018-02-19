package com.example.alex.test.chatroom;

import com.example.alex.test.contact.ContactItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.GregorianCalendar;

/**
 * Created by alex on 16.05.17.
 */

public class ChatroomMessageItem {
    public static final String USER_ID = setUserID();
    public String message;
    public boolean type;
    public long time;
    public String contactId;

    //boolean variable "type" stands for type of the message(true = income, false = outcome)

    public ChatroomMessageItem(String message, long time, String contactId){
        this.message = message;
        this.time = time;
        contactId = contactId;
        if (contactId != USER_ID){
            type = true;
        }
    }
    public ChatroomMessageItem(){}

    public static final String setUserID(){
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();
        return userId;
    }

}


