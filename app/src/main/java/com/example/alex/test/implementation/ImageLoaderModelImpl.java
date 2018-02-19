package com.example.alex.test.implementation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.alex.test.model.ConatactModel;
import com.example.alex.test.model.ImageLoaderModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by alex on 07.02.2018.
 */

public class ImageLoaderModelImpl implements ImageLoaderModel {
    private Context mContext;
    private Map<String, Bitmap> mImageList = new HashMap<>();
    private List<String> mIds = new ArrayList<>();

    public ImageLoaderModelImpl(Context context, List<String> idList) {
        mContext = context;
        mIds = idList;
    }

    @Override
    public void getContactsImagesList(@NonNull final ImageModelCallback callback) {
        getImages();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                callback.onFinished(mImageList);
            }
        },5000);
    }

    public void getImages() {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("ChatApp");
        final Bitmap[] bm = new Bitmap[mIds.size()];
        for(int i = 0; i < mIds.size(); i++) {

            final String id = mIds.get(i);
            final int k = i;
            storageRef.child(id + "_small.jpg").getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    bm[k] = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    Log.d("taga", "success download " + id);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("taga", e.toString());
                }
            }).addOnCompleteListener(new OnCompleteListener<byte[]>() {
                @Override
                public void onComplete(@NonNull Task<byte[]> task) {
                    Log.d("taga", "colmplete download " + id);
                    putToMap(bm[k], id);
                }
            });

        }
    }

    private void putToMap(Bitmap bitmap, String id) {
        mImageList.put(id, bitmap);
    }
}
