package com.mark.streamproject.dialog;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mark.streamproject.R;
import com.mark.streamproject.StreamProject;
import com.mark.streamproject.component.RoundAndStrokeTransformation;
import com.mark.streamproject.component.RoundTransformation;
import com.mark.streamproject.data.Room;
import com.squareup.picasso.Picasso;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DescriptionAdapter extends RecyclerView.Adapter{

    private static final int TYPE_FIRST     = 0x01;
    private static final int TYPE_SECOND      = 0x02;

    private String firstUrl = "https://firebasestorage.googleapis.com/v0/b/streamdemo-236309.appspot.com/o/01.png" +
            "?alt=media&token=daaf4990-87d2-413c-950b-4670b70964bd";
    private String secondUrl = "https://firebasestorage.googleapis.com/v0/b/streamdemo-236309.appspot.com/o/02.png" +
            "?alt=media&token=3a9105f4-2cb5-4c28-a93b-b4c8fcc4f18f";


    public DescriptionAdapter() {

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_FIRST) {
            return new FirstViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_description_first, parent, false));
        } else {
            return new SecondViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_description_second, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof FirstViewHolder) {
            bindFirstViewHolder((FirstViewHolder) holder);
        } else {
            bindSecondViewHolder((SecondViewHolder) holder);
        }

    }

    private void bindFirstViewHolder(FirstViewHolder holder) {
        holder.getTextView().setText(StreamProject.getAppContext().getString(R.string.description_01));

        Picasso.get()
                .load(firstUrl)
                .fit()
                .into(holder.getImageView());
    }

    private void bindSecondViewHolder(SecondViewHolder holder) {
        holder.getTextView().setText(StreamProject.getAppContext().getString(R.string.description_02));

        Picasso.get()
                .load(secondUrl)
                .fit()
                .into(holder.getImageView());
    }


    @Override
    public int getItemCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_FIRST;
        } else {
            return TYPE_SECOND;
        }
    }

    private class FirstViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImageView;
        private TextView mTextView;


        public FirstViewHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.image_description_first);
            mTextView = itemView.findViewById(R.id.text_description_first);
        }

        public ImageView getImageView() {
            return mImageView;
        }

        public TextView getTextView() {
            return mTextView;
        }
    }

    private class SecondViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImageView;
        private TextView mTextView;


        public SecondViewHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.image_description_second);
            mTextView = itemView.findViewById(R.id.text_description_second);
        }

        public ImageView getImageView() {
            return mImageView;
        }

        public TextView getTextView() {
            return mTextView;
        }
    }

}
