package com.example.alex.test.presenter;

import com.example.alex.test.contact.ContactItem;

import java.util.List;

/**
 * Created by alex on 07.02.2018.
 */

public interface ImageLoaderPresenter {
    public void onResume();

    public void onDestroy();

    public void getImageList();
    public void searchContactsImages(String text);

    public void synchronizeWithContactsList(List<ContactItem> list);
}
