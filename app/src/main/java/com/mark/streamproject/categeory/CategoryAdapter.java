package com.mark.streamproject.categeory;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mark.streamproject.R;
import com.mark.streamproject.data.Room;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CategoryAdapter extends RecyclerView.Adapter {
    private CategoryContract.Presenter mPresenter;

    private ArrayList<Room> mRoomList;

    public CategoryAdapter(CategoryContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        return new CategoryViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CategoryViewHolder) {
            bindCategoryViewHolder((CategoryViewHolder) holder, mRoomList.get(position));
        }

    }

    private void bindCategoryViewHolder(CategoryViewHolder holder, Room room) {
        holder.getTextTitle().setText(room.getTitle());
        holder.getTextName().setText(room.getStreamerName());
        String like = Integer.toString(room.getLike());
        holder.getTextLike().setText(like);
        String dislike = Integer.toString(room.getDislike());
        holder.getTextDislike().setText(dislike);

        if (!room.getStreamerImage().equals("")) {
            Picasso.get()
                    .load(room.getStreamerImage())
                    .fit()
                    .centerCrop()
                    .into(holder.getImage());
        }

        Picasso.get()
                .load(room.getImage())
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

    private class CategoryViewHolder extends RecyclerView.ViewHolder {
        private TextView mTextName;
        private TextView mTextTitle;
        private ImageView mImage;
        private ImageView mImageScreen;
        private TextView mTextLike;
        private TextView mTextDislike;

        public CategoryViewHolder(View itemView) {
            super(itemView);
            mTextName = itemView.findViewById(R.id.text_category_name);
            mTextTitle = itemView.findViewById(R.id.text_category_title);
            mImage = itemView.findViewById(R.id.image_category_user);
            mImageScreen = itemView.findViewById(R.id.image_category_screen);
            mTextLike = itemView.findViewById(R.id.text_category_like);
            mTextDislike = itemView.findViewById(R.id.text_category_dislike);
            itemView.findViewById(R.id.layout_category).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!mPresenter.isCategoryRefreshing()) {
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

        public ImageView getImageScreen() {
            return mImageScreen;
        }

        public TextView getTextLike() {
            return mTextLike;
        }

        public TextView getTextDislike() {
            return mTextDislike;
        }
    }

    public void updateData(ArrayList<Room> roomList) {
        mRoomList = roomList;
        notifyDataSetChanged();
    }
}
