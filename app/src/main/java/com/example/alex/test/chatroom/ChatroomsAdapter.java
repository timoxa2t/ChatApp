package com.example.alex.test.chatroom;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.alex.test.R;
import com.example.alex.test.chating.ChatActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alex on 04.03.2017.
 */

public class ChatroomsAdapter extends RecyclerView.Adapter<ChatroomsAdapter.ChatroomViewHoder> {

    private List<ChatroomItem> mChatroomsList = new ArrayList<>();
    private Context mContext;

    @Override
    public ChatroomsAdapter.ChatroomViewHoder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chatroom, parent, false);
        return new ChatroomsAdapter.ChatroomViewHoder(view);
    }

    @Override
    public void onBindViewHolder(ChatroomsAdapter.ChatroomViewHoder holder, int position) {
        if (position < mChatroomsList.size()) {
            holder.bind(mChatroomsList.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return mChatroomsList.size();
    }

    public void refreshChatroomsList(List<ChatroomItem> list, Context context) {
        if (mChatroomsList.size() > 0) {
            mChatroomsList.clear();
        }
        mChatroomsList = list;
        mContext = context;
        notifyDataSetChanged();
    }

    class ChatroomViewHoder extends RecyclerView.ViewHolder implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {
        private TextView mTvChatroomName;
        private TextView mTvChatroomPeople;
        private TextView mTvDescription;
        private ImageView mIvPicture;
        private Switch mSwitch;


        public ChatroomViewHoder(View itemView) {
            super(itemView);
            mTvChatroomName = (TextView) itemView.findViewById(R.id.tv_chatroom_name);
            mTvChatroomPeople = (TextView) itemView.findViewById(R.id.tv_people_in_room);
            mTvDescription = (TextView) itemView.findViewById(R.id.tv_description);
            mIvPicture = (ImageView) itemView.findViewById(R.id.iv_image);
            mSwitch = (Switch) itemView.findViewById(R.id.switch1);
            mSwitch.setOnCheckedChangeListener(this);
            itemView.setOnClickListener(this);
        }

        public void bind(@Nullable ChatroomItem item) {
            if (item == null) return;
            int iconId;
            if (item.picture != null) {
                iconId = R.drawable.chatrooms_img;
            } else {
                iconId = R.drawable.chatrooms_img;
            }
            Glide.with(mContext)
                    .load(iconId)
                    .asBitmap()
                    .centerCrop()
                    .into(mIvPicture);

            if (TextUtils.isEmpty(item.chatroomName)) {
                mTvChatroomName.setText("NoName");
            } else {
                mTvChatroomName.setText(item.chatroomName);
            }
            mTvChatroomPeople.setText("" + item.peopleCount);
            if (TextUtils.isEmpty(item.description)) {
                mTvDescription.setText("This is description of current chatroom for users to navigate and found chatroom they are looked for");
            } else {
                mTvDescription.setText(item.description);
            }
            mSwitch.setChecked(false);
            mTvDescription.setVisibility(View.GONE);

        }

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if (b) {
                mTvDescription.setVisibility(View.VISIBLE);
            } else {
                mTvDescription.setVisibility(View.GONE);
            }
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(mContext, ChatroomActivity.class);
            ChatroomItem item = mChatroomsList.get(getAdapterPosition());
            intent.putExtra("ChatroomId", item.chatroomId);
            mContext.startActivity(intent);
        }
    }
}
