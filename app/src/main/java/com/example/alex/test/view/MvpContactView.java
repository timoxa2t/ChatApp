package com.example.alex.test.view;

import android.graphics.Bitmap;

import com.example.alex.test.contact.ContactItem;

import java.util.List;
import java.util.Map;

/**
 * Created by alex on 26.03.2017.
 */

public interface MvpContactView {
    void setContactsList(List<ContactItem> contItem);
    public String getUserId();
    void showProgress();
    void hideProgress();

    void refreshIcons(Map<String, Bitmap> imgList);
}
