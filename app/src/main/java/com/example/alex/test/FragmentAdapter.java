package com.example.alex.test;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.example.alex.test.chatroom.ChatroomsFragment;
import com.example.alex.test.contact.ContactsFragment;
import com.example.alex.test.message.MessagesFragment;

import java.util.ArrayList;

/**
 * Created by alex on 16.03.2017.
 */

public class FragmentAdapter extends FragmentPagerAdapter {
    private ArrayList<android.support.v4.app.Fragment> mFragments = new ArrayList<>();

    public FragmentAdapter(FragmentManager fm) {
        super(fm);
        mFragments.add(new ContactsFragment());
        mFragments.add(new ChatroomsFragment());
        mFragments.add(new MessagesFragment());

    }

    @Override
    public android.support.v4.app.Fragment getItem(int position) {
        if(position > mFragments.size())return null;

        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

     @Override
     public CharSequence getPageTitle(int position) {
        String[] tabs = new String[]{"Contacts", "Chatrooms", "Messages"};
        return tabs[position];
    }
}
