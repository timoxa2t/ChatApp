package com.example.alex.test.presenter;

/**
 * Created by alex on 30.03.2017.
 */

public interface ChatroomPresenter {
    public void onResume();

    public void onDestroy();

    public void generateChatroomsList();

    public void searchChatrooms(String text);
}
