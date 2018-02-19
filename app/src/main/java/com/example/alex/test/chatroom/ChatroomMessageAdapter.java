package com.example.alex.test.chatroom;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.alex.test.R;
import com.example.alex.test.TimeParser;
import com.example.alex.test.chating.Message;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by alex on 16.05.17.
 */

public class ChatroomMessageAdapter extends RecyclerView.Adapter<ChatroomMessageAdapter.ChatroomMessageViewHolder> {
    private List<ChatroomMessageItem> messagesList = new ArrayList<>();
    private Context mContext;

    public ChatroomMessageAdapter(Context context) {
        mContext = context;
    }

    @Override
    public ChatroomMessageAdapter.ChatroomMessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        Log.d("taga", "" + viewType);
        switch (viewType){
            case 0:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.outcome_message_item, parent, false);
                return new ChatroomMessageAdapter.ChatroomMessageViewHolder(view, false);
            case 1:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.income_chat_message_item, parent, false);
                return new ChatroomMessageAdapter.ChatroomMessageViewHolder(view, true);
        }
        Log.d("outcome message", "createViewHolder");
        return null;
    }

    @Override
    public void onBindViewHolder(ChatroomMessageAdapter.ChatroomMessageViewHolder holder, int position) {
        Log.d("outcome message", "onBind");
        if(position < messagesList.size()){
            holder.bind(messagesList.get(position));
        }
    }

    @Override
    public int getItemCount() {
        Log.d("outcome message", "list size " + messagesList.size());
        return messagesList.size();
    }

    @Override
    public long getItemId(int position) {
        Log.d("outcome message", "list size " + messagesList.get(position));
        return super.getItemId(position);
    }

    public void refreshMessagesList(List<ChatroomMessageItem> list){
        if(messagesList.size() > 0) {
            messagesList.clear();
        }
        Log.d("outcome message", "Refreshing list");
        messagesList = list;
        notifyDataSetChanged();
    }

    public void addMessage(ChatroomMessageItem message){
        messagesList.add(message);
        notifyItemInserted(messagesList.size()-1);
    }

    @Override
    public int getItemViewType(int position) {
        boolean type = messagesList.get(position).type;
        if(type) return 1;
        else return 0;
    }

    class ChatroomMessageViewHolder extends RecyclerView.ViewHolder {
        private TextView message;
        private TextView time;
        private TextView contact;

        public ChatroomMessageViewHolder(View itemView, boolean type) {
            super(itemView);
            if(type) {
                message = (TextView) itemView.findViewById(R.id.tv_chatroom_income_message);
                contact = (TextView) itemView.findViewById(R.id.tv_message_item_contact_name);
                Log.d("taga", contact.getText().toString());
            }
            else {
                message = (TextView) itemView.findViewById(R.id.tv_outcome_message);
            }
            time = (TextView) itemView.findViewById(R.id.tv_time);
        }

        public void bind(ChatroomMessageItem item){
            message.setText(item.message);
            message.setMaxWidth((int) mContext.getResources().getDimension(R.dimen.chatroom_message_width));
            if(contact != null) {
              //  contact.setText(item.getName());
            }
            time.setText(TimeParser.parseTime(item.time));
        }
    }
}
