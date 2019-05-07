package com.mark.streamproject.dialog;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mark.streamproject.R;
import com.mark.streamproject.StreamProject;
import com.squareup.picasso.Picasso;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TutorialAdapter extends RecyclerView.Adapter {

    private static final int TYPE_FIRST     = 0x01;
    private static final int TYPE_SECOND      = 0x02;

    private static final String firstStepUrl = "https://firebasestorage.googleapis.com/v0/b/streamdemo-236309.appspot.com/o/01.png"
            + "?alt=media&token=daaf4990-87d2-413c-950b-4670b70964bd";
    private static final String secondStepUrl = "https://firebasestorage.googleapis.com/v0/b/streamdemo-236309.appspot.com/o/02.png"
            + "?alt=media&token=3a9105f4-2cb5-4c28-a93b-b4c8fcc4f18f";


    public TutorialAdapter() {

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_FIRST) {
            return new FirstStepViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_description_first, parent, false));
        } else {
            return new SecondStepViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_description_second, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof FirstStepViewHolder) {
            bindFirstViewHolder((FirstStepViewHolder) holder);
        } else {
            bindSecondViewHolder((SecondStepViewHolder) holder);
        }

    }

    private void bindFirstViewHolder(FirstStepViewHolder holder) {
        holder.getTextView().setText(StreamProject.getAppContext().getString(R.string.description_01));

        Picasso.get()
                .load(firstStepUrl)
                .fit()
                .into(holder.getImageView());
    }

    private void bindSecondViewHolder(SecondStepViewHolder holder) {
        holder.getTextView().setText(StreamProject.getAppContext().getString(R.string.description_02));

        Picasso.get()
                .load(secondStepUrl)
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

    private class FirstStepViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImageView;
        private TextView mTextView;


        public FirstStepViewHolder(View itemView) {
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

    private class SecondStepViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImageView;
        private TextView mTextView;


        public SecondStepViewHolder(View itemView) {
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
