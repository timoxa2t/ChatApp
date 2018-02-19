package com.example.alex.test.chating;

import java.util.Date;

/**
 * Created by alex on 27.04.17.
 */

public class Message {
    public String message;
    public boolean type;
    public long time;

    //boolean variable "type" stands for type of the message(true = income, false = outcome)

    public Message(String message, boolean type, long time){
        this.message = message;
        this.type = type;
        this.time = time;
    }
    public Message(){}
}
