package com.example.alex.test.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.alex.test.contact.ContactItem;

import java.util.List;

/**
 * Created by alex on 28.03.2017.
 */

public interface ConatactModel {
    interface ContactModelCallback{
        void onFinished(@Nullable List<ContactItem> contList);
    }

    void getContactsList(@NonNull ConatactModel.ContactModelCallback callback);
}
