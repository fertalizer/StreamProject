package com.mark.streamproject.room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mark.streamproject.R;
import com.mark.streamproject.data.Message;
import com.mark.streamproject.data.Room;

import java.util.ArrayList;

public class RoomAdapter extends RecyclerView.Adapter {
    private static final int TYPE_LOADING = 0;
    private static final int TYPE_DEFAULT = 0x01;
    private static final int TYPE_MESSAGE = 0x02;

    private RoomContract.Presenter mPresenter;

    private ArrayList<Message> mMessageList;


    public RoomAdapter(RoomContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_DEFAULT) {
            return new DefaultViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_room_default, parent, false));
        } else {
            return new MessageViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_room_message, parent, false));
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MessageViewHolder) {
            bindMessageViewHolder((MessageViewHolder) holder, mMessageList.get(position - 1));
        }
    }

    private void bindMessageViewHolder(MessageViewHolder holder, Message message) {
        holder.getName().setText(message.getName());
        holder.getContent().setText(message.getContent());
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_DEFAULT;
        } else {
            return TYPE_MESSAGE;
        }
    }

    @Override
    public int getItemCount() {
        if (mMessageList == null) {
            return 0;
        } else {
            return mMessageList.size() + 1;
        }
    }



    private class DefaultViewHolder extends RecyclerView.ViewHolder {

        public DefaultViewHolder(View itemView) {
            super(itemView);
        }

    }

    private class MessageViewHolder extends RecyclerView.ViewHolder {
        private TextView mName;
        private TextView mContent;

        public MessageViewHolder(View itemView) {
            super(itemView);
            mName = itemView.findViewById(R.id.text_message_name);
            mContent = itemView.findViewById(R.id.text_message_text);
        }

        public TextView getName() {
            return mName;
        }

        public TextView getContent() {
            return mContent;
        }
    }

    public void updateData(ArrayList<Message> messageListList) {
        mMessageList = messageListList;
        notifyDataSetChanged();
    }
}
