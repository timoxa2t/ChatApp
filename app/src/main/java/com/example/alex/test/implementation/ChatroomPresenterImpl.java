package com.example.alex.test.implementation;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.Toast;

import com.example.alex.test.chatroom.ChatroomItem;
import com.example.alex.test.contact.ContactItem;
import com.example.alex.test.model.ChatroomModel;
import com.example.alex.test.presenter.ChatroomPresenter;
import com.example.alex.test.view.MvpChatroomView;

import java.util.List;

/**
 * Created by alex on 30.03.2017.
 */

public class ChatroomPresenterImpl implements ChatroomPresenter {
    private MvpChatroomView mView;
    private Context mContext;
    private ChatroomModelImpl mModel;
    private static String mSearchText;
    private static boolean refreshingCount = false;

    public ChatroomPresenterImpl(Context context, MvpChatroomView view){
        mView = view;
        mContext = context;
        mModel = new ChatroomModelImpl(context);
        generateChatroomsList();
    }


    @Override
    public void onResume() {
        generateChatroomsList();
    }

    @Override
    public void onDestroy() {
        mView = null;
    }

    @Override
    public void generateChatroomsList() {
        if (mView == null) return;
        mView.showProgress();
        mModel.getContactsList(new ChatroomModel.ChatroomModelCallback(){

            @Override
            public void onFinished(@Nullable List<ChatroomItem> chatrList) {
                if(refreshingCount){
                    chatrList = listSearcher(chatrList);
                    refreshingCount = false;
                }

                if (chatrList.isEmpty()) {
                    Toast.makeText(mContext, "No avalable chatrooms",Toast.LENGTH_SHORT).show();
                }
                mView.setChatroomsList(chatrList);
                mView.hideProgress();
            }
        });
    }

    @Override
    public void searchChatrooms(String chatroomName){
        mSearchText = chatroomName;
        refreshingCount = true;
    }

    public List<ChatroomItem> listSearcher(List<ChatroomItem> chatrList){
        if(!TextUtils.isEmpty(mSearchText)) {
            for (int i = 0; i < chatrList.size(); i++) {
                ChatroomItem item = chatrList.get(i);
                if (item.chatroomName.contains(mSearchText)) continue;
                chatrList.remove(i);
                i--;
            }
        }
        return chatrList;
    }
}
