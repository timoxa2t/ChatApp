package com.example.alex.test.model;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.alex.test.contact.ContactItem;

import java.util.List;
import java.util.Map;

/**
 * Created by alex on 07.02.2018.
 */

public interface ImageLoaderModel {
    interface ImageModelCallback{
        void onFinished(@Nullable Map<String, Bitmap> contList);
    }

    void getContactsImagesList(@NonNull ImageLoaderModel.ImageModelCallback callback);
}
