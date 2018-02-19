package com.example.alex.test.message;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.example.alex.test.R;
import com.example.alex.test.chating.ChatActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alex on 04.03.2017.
 */

public class PrivateMessagesAdapter extends RecyclerView.Adapter<PrivateMessagesAdapter.MessageViewHoder> {

    private List<MessageItem> mMessagesList = new ArrayList<>();
    private Context mContext;

    @Override
    public PrivateMessagesAdapter.MessageViewHoder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_private_message, parent, false);
        return new PrivateMessagesAdapter.MessageViewHoder(view);
    }

    @Override
    public void onBindViewHolder(PrivateMessagesAdapter.MessageViewHoder holder, int position) {
        if(position < mMessagesList.size()){
            holder.bind(mMessagesList.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return mMessagesList.size();
    }

    public void refreshMessagesList(List<MessageItem> list, Context context){
        if(mMessagesList.size() > 0) {
            mMessagesList.clear();
        }
        mMessagesList = list;
        mContext = context;
        notifyDataSetChanged();
    }

    class MessageViewHoder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView mTvContactName;
        private TextView mTvMessage;
        private TextView mTvTime;
        private ImageView mIvIcon;

        public MessageViewHoder(View itemView) {
            super(itemView);
            mTvContactName = (TextView) itemView.findViewById(R.id.tv_contact_name);
            mTvMessage = (TextView)itemView.findViewById(R.id.tv_private_message);
            mIvIcon = (ImageView) itemView.findViewById(R.id.iv_icon);
            mTvTime = (TextView) itemView.findViewById(R.id.tv_date);
            itemView.setOnClickListener(this);
        }

        public void bind(MessageItem item){
            int iconId;
            if(item.icon != null){
                iconId = R.drawable.icon;
            }else{
                iconId = R.drawable.ic_launcher;
            }
            Glide.with(mContext)
                    .load(iconId)
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
                mTvMessage.setText("Clear");
            }else{
                mTvMessage.setText(item.message);
            }
            if(TextUtils.isEmpty(item.time)) {
                mTvTime.setText(item.time);
            }
        }

        public int getItemPosition(){
            return getAdapterPosition();
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(mContext, ChatActivity.class);
            intent.putExtra("CotactId", mMessagesList.get(getItemPosition()).id);
            mContext.startActivity(intent);
        }
    }
}

