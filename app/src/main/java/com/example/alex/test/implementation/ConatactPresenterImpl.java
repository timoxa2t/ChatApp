package com.example.alex.test.implementation;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.example.alex.test.contact.ContactItem;
import com.example.alex.test.contact.ContactsDatabase;
import com.example.alex.test.model.ConatactModel;
import com.example.alex.test.view.MvpContactView;
import com.example.alex.test.presenter.ConatactPresenter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by alex on 28.03.2017.
 */

public class ConatactPresenterImpl implements ConatactPresenter {

    private MvpContactView mView;
    private Context mContext;
    private ConatactModelImpl mModel;
    private static String mSearchText;
    private static boolean refreshingCount = false;

    public ConatactPresenterImpl(Context context, MvpContactView view){
        mView = view;
        mContext = context;
        mModel = new ConatactModelImpl(context);
    }


    @Override
    public void onResume() { }

    @Override
    public void onDestroy() {
        mView = null;
    }

    @Override
    public void generateContactsList() {
        if (mView == null) return;
        mView.showProgress();
        mModel.getContactsList(new ConatactModel.ContactModelCallback(){

            @Override
            public void onFinished(@Nullable List<ContactItem> contList) {
                if(refreshingCount){
                    contList = listSearcher(contList);
                    refreshingCount = false;
                }

                if (contList.isEmpty()) {
                    Toast.makeText(mContext, "No contacts found", Toast.LENGTH_SHORT).show();
                    return;
                }
                mView.setContactsList(contList);
                mView.hideProgress();
                loadImages(contList);


                Log.d("refresh", "getContactsList ContactsPresenter");
            }
        });
    }

    private void loadImages(List<ContactItem> contList) {
        List<String> idList = new ArrayList<>();
        for (ContactItem contactItem: contList){
            idList.add(contactItem.contactId);
        }
        new ImageLoaderPresenterImpl(mContext, mView, idList);
    }

    @Override
    public void searchContacts(String contactName){
        mSearchText = contactName;
        refreshingCount = true;
    }

    @Override
    public void synchronizeWithFirebase(List<ContactItem> contactsList) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("contacts");

        for (int i = 0; i < contactsList.size(); i++) {
            ContactItem item = contactsList.get(i);
            myRef.child(item.contactId).child("name").setValue(item.name);
            myRef.child(item.contactId).child("time").setValue(item.time);
            myRef.child(item.contactId).child("message").setValue(item.message);
        }
    }

    public List<ContactItem> listSearcher(List<ContactItem> contList){
        if(!TextUtils.isEmpty(mSearchText)) {
            for (int i = 0; i < contList.size(); i++) {
                ContactItem item = contList.get(i);
                if (item.name.contains(mSearchText)) continue;
                contList.remove(i);
                i--;
            }
        }
        return contList;
    }
}
