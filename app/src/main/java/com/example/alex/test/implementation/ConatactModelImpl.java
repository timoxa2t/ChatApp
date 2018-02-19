package com.example.alex.test.implementation;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.example.alex.test.R;
import com.example.alex.test.contact.ContactItem;
import com.example.alex.test.contact.ContactsDatabase;
import com.example.alex.test.model.ConatactModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

/**
 * Created by alex on 28.03.2017.
 */

public class ConatactModelImpl implements ConatactModel, ValueEventListener {
    Context mContext;
    ArrayList<ContactItem> mList = new ArrayList<>();

    public ConatactModelImpl(Context context) {
        mContext = context;
    }

    public void getContactsList(@NonNull final ConatactModel.ContactModelCallback callback){
        getFirebaseData();
        Log.d("TAGA", "getContactlist before handler");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d("TAGA", "getContactlist after handler");
                callback.onFinished(mList);
            }
        },3000);


    }



    public List<ContactItem> getFirebaseData()  {// download contacts and images list
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference contactRef = database.getReference("contacts");
        contactRef.addValueEventListener(this);
        return mList;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        if(!dataSnapshot.hasChildren()) return;
        List<ContactItem> list = new ArrayList<>();
        Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        do{
            DataSnapshot itemData = iterator.next();
            String id = itemData.getKey();

            if(TextUtils.equals(id, user.getUid())) continue;
            String name = itemData.child("name").getValue(String.class);
            long time = itemData.child("time").getValue(Long.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                list.add(new ContactItem(id, name, time, "message"));
            }else{
                list.add(new ContactItem(id, name, time, "message"));
            }
        }
        while (iterator.hasNext());
        mList = (ArrayList)list;
        Log.d("TAGA", "list size " + mList.size());
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        Toast.makeText(mContext, "Error: " + databaseError.toString(), Toast.LENGTH_SHORT).show();
    }


}
