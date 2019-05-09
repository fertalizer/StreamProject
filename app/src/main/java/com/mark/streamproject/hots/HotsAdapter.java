package com.mark.streamproject.hots;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.mark.streamproject.R;
import com.mark.streamproject.component.RoundAndStrokeTransformation;
import com.mark.streamproject.component.RoundTransformation;
import com.mark.streamproject.component.SizeTransformation;
import com.mark.streamproject.data.Room;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HotsAdapter extends RecyclerView.Adapter {
    private HotsContract.Presenter mPresenter;

    private ArrayList<Room> mRoomList;
    private ArrayList<Integer> mNumberList;

    public HotsAdapter(HotsContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        return new HotsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hots, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HotsViewHolder) {
            bindHotsViewHolder((HotsViewHolder) holder, mRoomList.get(position));
        }

    }

    private void bindHotsViewHolder(HotsViewHolder holder, Room room) {
        holder.getTextTitle().setText(room.getTitle());
        holder.getTextName().setText(room.getStreamerName());
        String like = Integer.toString(room.getLike());
        holder.getTextLike().setText(like);
        String dislike = Integer.toString(room.getDislike());
        holder.getTextDislike().setText(dislike);
//        String audience = Integer.toString(audienceNumber);
//        holder.getTextAudience().setText(audience);

        if (!room.getStreamerImage().equals("")) {
            Picasso.get()
                    .load(room.getStreamerImage())
                    .fit()
                    .centerCrop()
                    .into(holder.getImage());
        }

        Picasso.get()
                .load(room.getImage())
                .transform(new RoundAndStrokeTransformation(35))
                .fit()
                .centerCrop()
                .into(holder.getImageScreen());

    }


    @Override
    public int getItemCount() {
        if (mRoomList == null) {
            return 0;
        } else {
            return mRoomList.size();
        }
    }

    private class HotsViewHolder extends RecyclerView.ViewHolder {
        private TextView mTextName;
        private TextView mTextTitle;
        private ImageView mImage;
        private ImageView mImageScreen;
        private TextView mTextLike;
        private TextView mTextDislike;
        private TextView mTextAudience;

        public HotsViewHolder(View itemView) {
            super(itemView);
            mTextName = itemView.findViewById(R.id.text_hots_name);
            mTextTitle = itemView.findViewById(R.id.text_hots_room_title);
            mImage = itemView.findViewById(R.id.image_hots_user);
            mTextLike = itemView.findViewById(R.id.text_hots_like);
            mTextDislike = itemView.findViewById(R.id.text_hots_dislike);
            mTextAudience = itemView.findViewById(R.id.text_hots_audience_number);
            mImageScreen = itemView.findViewById(R.id.image_hots_screen);

            itemView.findViewById(R.id.layout_hots).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!mPresenter.isHotsRefreshing()) {
                        mPresenter.openRoom((Room) mRoomList.get(getAdapterPosition()));
                    }
                }
            });
        }

        public TextView getTextName() {
            return mTextName;
        }

        public TextView getTextTitle() {
            return mTextTitle;
        }

        public ImageView getImage() {
            return mImage;
        }

        public TextView getTextLike() {
            return mTextLike;
        }

        public TextView getTextDislike() {
            return mTextDislike;
        }

        public TextView getTextAudience() {
            return mTextAudience;
        }

        public ImageView getImageScreen() {
            return mImageScreen;
        }
    }

    public void updateData(ArrayList<Room> roomList, ArrayList<Integer> numberList) {
        mRoomList = roomList;
        mNumberList = numberList;
        notifyDataSetChanged();
    }

}
