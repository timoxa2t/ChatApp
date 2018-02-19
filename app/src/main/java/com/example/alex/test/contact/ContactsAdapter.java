package com.example.alex.test.contact;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.example.alex.test.R;
import com.example.alex.test.TimeParser;
import com.example.alex.test.chating.ChatActivity;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

/**
 * Created by alex on 03.03.2017.
 */

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactViewHoder> {

    private List<ContactItem> mContactsList = new ArrayList<>();
    private Context mContext;
    private int mPosition;
    private String mUserId;

    @Override
    public ContactViewHoder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact, parent, false);
        Log.d("contacts", "onCreate");
        return new ContactViewHoder(view);
    }

    public void setUserId(String userId){
        mUserId = userId;
    }

    @Override
    public void onBindViewHolder(ContactViewHoder holder, int position) {
        Log.d("contacts", "onBind");
        if(position < mContactsList.size()){
            holder.bind(mContactsList.get(position));
        }
    }

    @Override
    public int getItemCount() {
        Log.d("contacts", "size " + mContactsList.size());
        return mContactsList.size();
    }

    @Override
    public long getItemId(int position) {
        Log.d("contacts", "position " + position);
        return super.getItemId(position);
    }

    public void refreshContactsList(List<ContactItem> list, Context context){
        if(mContactsList.size() > 0) {
            mContactsList.clear();
        }
        mContactsList = list;
        mContext = context;
        notifyDataSetChanged();
    }

    public void deleteContact(){
        mContactsList.remove(mPosition);
        notifyItemRemoved(mPosition);
    }

    public Intent putProfileData(Intent intent){
        ContactItem item = mContactsList.get(mPosition);
        Bitmap bitmap = item.icon;
        ByteArrayOutputStream bs = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bs);
        intent.putExtra("image", bs.toByteArray());
        intent.putExtra("name", item.name);
        return intent;
    }


    public List<ContactItem> getList() {
        return mContactsList;
    }

    public void refreshContactsIcons(Map<String, Bitmap> imgList) {
        for(Map.Entry<String, Bitmap> contactItem: imgList.entrySet()){
            String id = contactItem.getKey();
            for(int i = 0; i < mContactsList.size(); i++){
                ContactItem item = mContactsList.get(i);
                if(id.equals(item.contactId)){
                    item.icon = contactItem.getValue();
                }
            }
        }
        notifyDataSetChanged();
    }


    class ContactViewHoder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener, View.OnClickListener {
        private TextView mTvContactName;
        private TextView mTvContactMessage;
        private TextView mTvTime;
        private ImageView mIvIcon;


        public ContactViewHoder(View itemView) {
            super(itemView);
            Log.d("contacts", "ViewHolder");
            itemView.setOnCreateContextMenuListener(this);
            mTvContactName = (TextView) itemView.findViewById(R.id.tv_contact_name);
            mTvContactMessage = (TextView)itemView.findViewById(R.id.tv_contact_message);
            mIvIcon = (ImageView) itemView.findViewById(R.id.iv_icon);
            mTvTime = (TextView) itemView.findViewById(R.id.tv_date);
            itemView.setOnClickListener(this);
        }

        public void bind(ContactItem item){
            Bitmap icon;
            if(item.icon != null){
                icon = item.icon;
            }else{
                icon = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_launcher);
            }
            ByteArrayOutputStream bs = new ByteArrayOutputStream();
            icon.compress(Bitmap.CompressFormat.JPEG, 10, bs);
            Glide.with(mContext)
                    .load(bs.toByteArray())
                    .asBitmap()
                    .centerCrop()
                    .into(new BitmapImageViewTarget(mIvIcon) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            mIvIcon.setImageDrawable(circularBitmapDrawable);
                        }
                    })
            ;

            if(TextUtils.isEmpty(item.name)){
                mTvContactName.setText("NoName");
            } else {
                mTvContactName.setText(item.name);
            }
            if(TextUtils.isEmpty(item.message)){
                mTvContactMessage.setText("Clear");
            }else{
                mTvContactMessage.setText(item.message);
            }
            String timeText = TimeParser.parseContactTime(item.time);
            mTvTime.setText(timeText);

        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            new MenuInflater(mContext).inflate(R.menu.contact_context_menu, contextMenu);
            mPosition = getAdapterPosition();
        }

        @Override
        public void onClick(View view) {
            Intent intent = putProfileData(new Intent(mContext, ChatActivity.class));
            intent.putExtra("ContactId", mContactsList.get(getAdapterPosition()).contactId);
            intent.putExtra("UserId", mUserId);
            mContext.startActivity(intent);
        }
    }
}
