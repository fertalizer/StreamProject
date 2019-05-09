package com.mark.streamproject.follow;

import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.mark.streamproject.R;
import com.mark.streamproject.StreamProject;
import com.mark.streamproject.data.User;
import com.mark.streamproject.util.Constants;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class FollowAdapter extends RecyclerView.Adapter {

    private FollowContract.Presenter mPresenter;

    private ArrayList<User> mUserList;

    public FollowAdapter(FollowContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        return new FollowViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_follow, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof FollowViewHolder) {
            bindFollowViewHolder((FollowViewHolder) holder, mUserList.get(position));
        }

    }

    private void bindFollowViewHolder(FollowViewHolder holder, User user) {
        holder.getTextName().setText(user.getName());
        if (user.getStatus() == Constants.STREAMING) {
            holder.getStatus().setText(StreamProject.getAppContext().getString(R.string.streaming));
            holder.getStatus().setTextColor(StreamProject.getAppContext().getColor(R.color.yellow));
        } else if (user.getStatus() == Constants.ONLINE) {
            holder.getStatus().setText(StreamProject.getAppContext().getString(R.string.online));
            holder.getStatus().setTextColor(StreamProject.getAppContext().getColor(R.color.white));
        } else {
            holder.getStatus().setText(StreamProject.getAppContext().getString(R.string.offline));
            holder.getStatus().setTextColor(StreamProject.getAppContext().getColor(R.color.gray));
        }

        if (!"".equals(user.getImage())) {
            Picasso.get()
                    .load(user.getImage())
                    .fit()
                    .centerCrop()
                    .into(holder.getImage());
        }


    }


    @Override
    public int getItemCount() {
        if (mUserList == null) {
            return 0;
        } else {
            return mUserList.size();
        }
    }

    private class FollowViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mTextName;
        private ImageView mImage;
        private TextView mStatus;
        private TextView mRemove;

        public FollowViewHolder(View itemView) {
            super(itemView);
            mTextName = itemView.findViewById(R.id.text_follow_name);
            mImage = itemView.findViewById(R.id.image_follow_user);
            mStatus = itemView.findViewById(R.id.text_follow_status);
            mRemove = itemView.findViewById(R.id.text_follow_remove);
            mRemove.setTypeface(Typeface.createFromAsset(StreamProject.getAppContext().getAssets(), Constants.FONTS_PATH));

            mRemove.setOnClickListener(this);
            itemView.findViewById(R.id.layout_follow).setOnClickListener(this);
        }

        public TextView getTextName() {
            return mTextName;
        }

        public ImageView getImage() {
            return mImage;
        }

        public TextView getStatus() {
            return mStatus;
        }

        public TextView getRemove() {
            return mRemove;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.text_follow_remove:
//                    mPresenter.removeStreamer(mUserList.get(getAdapterPosition()));
                    mPresenter.showAlertDialog(mUserList.get(getAdapterPosition()));
                    break;
                case R.id.layout_follow:
                    if (mUserList.get(getAdapterPosition()).getStatus() == Constants.STREAMING) {
                        mPresenter.openRoomByUserId(mUserList.get(getAdapterPosition()));
                        Log.d(Constants.TAG, "Open Room By Id");
                    }
                    break;
                default:
            }
        }
    }

    public void updateData(ArrayList<User> userList) {
        mUserList = userList;
        notifyDataSetChanged();
    }
}
