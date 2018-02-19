package com.example.alex.test.implementation;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.Toast;

import com.example.alex.test.contact.ContactItem;
import com.example.alex.test.message.MessageItem;
import com.example.alex.test.model.MessageModel;
import com.example.alex.test.presenter.MessagePresenter;
import com.example.alex.test.view.MvpMessageView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

/**
 * Created by alex on 30.03.2017.
 */

public class MessagePresenterImpl implements MessagePresenter{

    private MvpMessageView mView;
    private Context mContext;
    private MessageModelImpl mModel;
    private static String mSearchText;
    private static boolean refreshingCount = false;

    public MessagePresenterImpl(Context context, MvpMessageView view){
        mView = view;
        mContext = context;
        mModel = new MessageModelImpl(context);
    }


    @Override
    public void onResume() {
        generateMessageList();
    }

    @Override
    public void onDestroy() {
        mView = null;
    }

    @Override
    public void generateMessageList() {
        if (mView == null) return;
        mView.showProgress();
        mModel.getMessagesList(new MessageModel.MessageModelCallback(){

            @Override
            public void onFinished(@Nullable List<MessageItem> msgList) {
                if(refreshingCount){
                    msgList = listSearcher(msgList);
                    refreshingCount = false;
                }
                if (msgList.isEmpty()) {
                    Toast.makeText(mContext, "no income messages", Toast.LENGTH_SHORT).show();
                }
                mView.setMessagesList(msgList);
                mView.hideProgress();
            }
        });
    }

    @Override
    public void searchMessage(String text) {
        mSearchText = text;
        refreshingCount = true;
    }

    public List<MessageItem> listSearcher(List<MessageItem> contList){
        if(!TextUtils.isEmpty(mSearchText)) {
            for (int i = 0; i < contList.size(); i++) {
                MessageItem item = contList.get(i);
                if (item.name.contains(mSearchText)) continue;
                contList.remove(i);
                i--;
            }
        }
        syncronizeWithFirebase(contList);
        return contList;
    }

    public void syncronizeWithFirebase(List<MessageItem> contactsList){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("contacts");
        for (int i = 0; i < contactsList.size(); i++) {
            MessageItem item = contactsList.get(i);
            myRef.child(item.id + "").child("name").setValue(item.name);
            myRef.child(item.id + "").child("time").setValue(item.time);
            myRef.child(item.id + "").child("message").setValue(item.message);
        }
    }
}
