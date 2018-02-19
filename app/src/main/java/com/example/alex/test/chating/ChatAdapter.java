package com.example.alex.test.chating;

import android.content.Context;
import android.support.annotation.IntegerRes;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.alex.test.R;
import com.example.alex.test.TimeParser;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by alex on 27.04.17.
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MessageViewHolder> {
    private List<Message> messagesList = new ArrayList<>();
    private Context mContext;

    public ChatAdapter(Context context) {
        mContext = context;
    }

    public List<Message> getMessagesList(){
        return messagesList;
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType){
            case 0:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.outcome_message_item, parent, false);
                return new MessageViewHolder(view, false);
            case 1:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.income_message_item, parent, false);
                return new MessageViewHolder(view, true);
        }
        Log.d("outcome message", "createViewHolder");
        return null;
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {
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

    public void refreshMessagesList(List<Message> list){
        if(messagesList.size() > 0) {
            messagesList.clear();
        }
        Log.d("outcome message", "Refreshing list");
        messagesList = list;
        notifyDataSetChanged();
    }

    public void addMessage(Message message){
        messagesList.add(message);
        notifyItemInserted(messagesList.size()-1);
    }

    @Override
    public int getItemViewType(int position) {
        boolean type = messagesList.get(position).type;
        if(type) return 1;
        else return 0;
    }

    class MessageViewHolder extends RecyclerView.ViewHolder {
        private TextView message;
        private TextView time;

        public MessageViewHolder(View itemView, boolean type) {
            super(itemView);
            if(type) {
                message = (TextView) itemView.findViewById(R.id.tv_income_message);
            }
            else {
                message = (TextView) itemView.findViewById(R.id.tv_outcome_message);
            }
            time = (TextView) itemView.findViewById(R.id.tv_time);
            Log.d("outcome message", "ViewHolder");
        }

        public void bind(Message item){
            String timeText = TimeParser.parseTime(item.time);
            time.setText(timeText);
            message.setText(item.message);
            message.setMaxWidth((int) mContext.getResources().getDimension(R.dimen.message_width));

        }
    }
}
