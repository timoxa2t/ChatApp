package com.example.alex.test;

import java.util.Date;

/**
 * Created by alex on 19.05.17.
 */

public class TimeParser {
    public static String parseTime(long milis){
        Date later = new Date();
        later.setTime(milis);
        String time;
        Date now = new Date();
        if(now.getYear() > later.getYear()){
            time = later.getMonth() + 1 + "." + later.getYear();
        }else if(now.getMonth() > later.getMonth()){
            time = later.getDate() + "." + (later.getMonth() + 1);
        }else if(now.getDate() > later.getDate()){
            time = later.getDate() + "." + ((later.getMonth() + 1) < 10 ? "0" + (later.getMonth() + 1): (later.getMonth() + 1))+ "." + (later.getYear() + 1900);
        }else {
            if(later.getMinutes() < 10){
                time = later.getHours() + ":0" + later.getMinutes();

            }
            else {
                time = later.getHours() + ":" + later.getMinutes();
            }
        }
        return time;
    }

    public static String parseContactTime(long time) {
        Date later = new Date();
        later.setTime(time);
        Date now = new Date();
        if (now.getTime() - later.getTime() < 10000) {
            return "online";
        }
        else{
            return parseTime(later.getTime());
        }
    }
}
