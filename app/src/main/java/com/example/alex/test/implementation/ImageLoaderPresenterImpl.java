package com.example.alex.test.implementation;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.example.alex.test.contact.ContactItem;
import com.example.alex.test.model.ConatactModel;
import com.example.alex.test.model.ImageLoaderModel;
import com.example.alex.test.presenter.ImageLoaderPresenter;
import com.example.alex.test.view.MvpContactView;

import java.util.List;
import java.util.Map;

/**
 * Created by alex on 07.02.2018.
 */

public class ImageLoaderPresenterImpl implements ImageLoaderPresenter {
    private MvpContactView mView;
    private Context mContext;
    private ImageLoaderModelImpl mModel;

    public ImageLoaderPresenterImpl(Context context, MvpContactView view, List<String> idList) {
        mView = view;
        mContext = context;
        mModel = new ImageLoaderModelImpl(context, idList);
        getImageList();
    }
    @Override
    public void onResume() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void getImageList() {
        if (mView == null) return;

        mModel.getContactsImagesList(new ImageLoaderModel.ImageModelCallback() {
            @Override
            public void onFinished(@Nullable Map<String, Bitmap> imgList) {
                mView.refreshIcons(imgList);
            }
        });
    }

    @Override
    public void searchContactsImages(String text) {

    }

    @Override
    public void synchronizeWithContactsList(List<ContactItem> list) {

    }
}
