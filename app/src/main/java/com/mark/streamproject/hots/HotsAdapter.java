package com.mark.streamproject.hots;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mark.streamproject.R;
import com.mark.streamproject.data.Room;
import com.mark.streamproject.data.User;

import java.util.ArrayList;

public class HotsAdapter extends RecyclerView.Adapter {
    private HotsContract.Presenter mPresenter;

    private ArrayList<Room> mHotsDataList = new ArrayList<>();

    public HotsAdapter(HotsContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        return new HotsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hots, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {
        if (holder instanceof HotsViewHolder) {
            bindHotsViewHolder((HotsViewHolder) holder, mHotsDataList.get(i));
        }

    }

    private void bindHotsViewHolder(HotsViewHolder holder, Room room) {
        holder.getTextTitle().setText(room.getTitle());
    }


    @Override
    public int getItemCount() {
        return mHotsDataList.size();
    }

    private class HotsViewHolder extends RecyclerView.ViewHolder {
        private TextView mTextName;
        private TextView mTextTitle;

        public HotsViewHolder(View itemView) {
            super(itemView);
            mTextName = itemView.findViewById(R.id.text_hots_name);
            mTextTitle = itemView.findViewById(R.id.text_hots_room_title);

            itemView.findViewById(R.id.layout_hots).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPresenter.openRoom();
                }
            });
        }

        public TextView getTextName() {
            return mTextName;
        }

        public TextView getTextTitle() {
            return mTextTitle;
        }
    }

    public void updateData(ArrayList<Room> hotsDataList) {
        mHotsDataList = hotsDataList;
        notifyDataSetChanged();
    }
}
