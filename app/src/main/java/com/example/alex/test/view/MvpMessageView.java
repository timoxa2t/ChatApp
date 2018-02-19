package com.example.alex.test.view;

import com.example.alex.test.message.MessageItem;

import java.util.List;

/**
 * Created by alex on 30.03.2017.
 */

public interface MvpMessageView {
    void setMessagesList(List<MessageItem> msgItem);
    void showProgress();
    void hideProgress();
}
