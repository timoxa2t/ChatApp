package com.example.alex.test.presenter;

/**
 * Created by alex on 30.03.2017.
 */

public interface MessagePresenter {
    public void onResume();

    public void onDestroy();

    public void generateMessageList();

    public void searchMessage(String text);
}
