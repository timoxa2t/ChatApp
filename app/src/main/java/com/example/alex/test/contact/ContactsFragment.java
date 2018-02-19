package com.example.alex.test.contact;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.SearchView;
import com.example.alex.test.MainActivity;
import com.example.alex.test.ProfileActivity;
import com.example.alex.test.R;
import com.example.alex.test.autorization.LoginActivity;
import com.example.alex.test.implementation.ConatactPresenterImpl;
import com.example.alex.test.presenter.ConatactPresenter;
import com.example.alex.test.view.MvpContactView;

import java.util.List;
import java.util.Map;

/**
 * Created by alex on 15.03.2017.
 */

public class ContactsFragment extends android.support.v4.app.Fragment implements MvpContactView,
        SwipeRefreshLayout.OnRefreshListener, View.OnClickListener, SearchView.OnQueryTextListener {

    private RecyclerView mRecView;
    private ContactsAdapter mContactsAdapter;
    private ConatactPresenter mConatactPresenter;
    private SwipeRefreshLayout pullToRefresh;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.contacts_fragment_layout, container, false);
        mRecView = (RecyclerView) view.findViewById(R.id.recycler_view);
        setHasOptionsMenu(true);

        mRecView.setLayoutManager(new LinearLayoutManager(getContext()));
        pullToRefresh = (SwipeRefreshLayout) view.findViewById(R.id.pull_to_refresh);
        pullToRefresh.setOnRefreshListener(this);
        mContactsAdapter = new ContactsAdapter();
        mContactsAdapter.setUserId(getUserId());
        mConatactPresenter = new ConatactPresenterImpl(getContext(), this);
        mRecView.setAdapter(mContactsAdapter);
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
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_contact:
                mContactsAdapter.deleteContact();
                break;
            case R.id.view_profile:
                Intent intent = new Intent(getActivity(), ProfileActivity.class);
                startActivity(mContactsAdapter.putProfileData(intent));
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void setContactsList(List<ContactItem> contItem) {
        mContactsAdapter.refreshContactsList(contItem, getContext());
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
    public void refreshIcons(Map<String, Bitmap> imgList) {
        mContactsAdapter.refreshContactsIcons(imgList);
    }

    @Override
    public void onRefresh() {
        mConatactPresenter.generateContactsList();
    }

    @Override
    public void onClick(View view) {
        getActivity().invalidateOptionsMenu();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        mConatactPresenter.searchContacts(query);

        onRefresh();
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public void onStop() {
        mConatactPresenter.synchronizeWithFirebase(mContactsAdapter.getList());
        super.onStop();
    }
    @Override
    public String getUserId(){
        return ((MainActivity)getActivity()).getCurrentUserId();
    }
}