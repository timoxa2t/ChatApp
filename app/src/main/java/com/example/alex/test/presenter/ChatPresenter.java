package com.example.alex.test.presenter;

import com.example.alex.test.chating.Message;

import java.util.List;

/**
 * Created by alex on 07.05.17.
 */

public interface ChatPresenter {

    public void generateMessageList();
    public void addMessageToFirebase(int position, Message message);
}
