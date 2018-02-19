package com.example.alex.test.chatroom;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;

import com.example.alex.test.MainActivity;
import com.example.alex.test.ProfileActivity;
import com.example.alex.test.R;
import com.example.alex.test.autorization.LoginActivity;
import com.example.alex.test.implementation.ChatroomPresenterImpl;
import com.example.alex.test.presenter.ChatroomPresenter;
import com.example.alex.test.view.MvpChatroomView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alex on 15.03.2017.
 */

public class ChatroomsFragment extends android.support.v4.app.Fragment implements
        MvpChatroomView, SwipeRefreshLayout.OnRefreshListener, View.OnClickListener, SearchView.OnQueryTextListener {
    private RecyclerView mRecView;
    private ChatroomsAdapter mChatroomsAdapter;
    private ChatroomPresenter mChatroomsPresenter;
    private SwipeRefreshLayout pullToRefresh;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.chatrooms_fragment_layout, container, false);
        mRecView = (RecyclerView) view.findViewById(R.id.recycler_view);
        setHasOptionsMenu(true);

        mRecView.setLayoutManager(new LinearLayoutManager(getContext()));
        pullToRefresh = (SwipeRefreshLayout) view.findViewById(R.id.pull_to_refresh);
        pullToRefresh.setOnRefreshListener(this);
        mChatroomsPresenter = new ChatroomPresenterImpl(getContext(), this);
        mChatroomsAdapter = new ChatroomsAdapter();
        mRecView.setAdapter(mChatroomsAdapter);
        onRefresh();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.menu_main, menu);
        SearchManager searchManager =
                (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.item_search).getActionView();
        SearchableInfo searchableInfo = searchManager.getSearchableInfo(new ComponentName(getActivity().getApplicationContext(), MainActivity.class));
        searchView.setSearchableInfo(searchableInfo);
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnSearchClickListener(this);
        searchView.setOnQueryTextListener(this);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.item_logout:
                SharedPreferences sp = getActivity().getSharedPreferences("UserData", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putBoolean("Sign in", false);
                editor.commit();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setChatroomsList(List<ChatroomItem> chatItem) {
        mChatroomsAdapter.refreshChatroomsList(chatItem, getContext());
    }
    @Override
    public void showProgress() {
        pullToRefresh.setRefreshing(true);
    }

    @Override
    public void hideProgress() {
        pullToRefresh.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        mChatroomsPresenter.generateChatroomsList();
    }

    @Override
    public void onClick(View view) {
        getActivity().invalidateOptionsMenu();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        mChatroomsPresenter.searchChatrooms(query);
        onRefresh();
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}

