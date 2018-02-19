package com.example.alex.test.presenter;

import com.example.alex.test.contact.ContactItem;

import java.util.List;

/**
 * Created by alex on 28.03.2017.
 */

public interface ConatactPresenter {
    public void onResume();

    public void onDestroy();

    public void generateContactsList();
    public void searchContacts(String text);

    public void synchronizeWithFirebase(List<ContactItem> list);
}
