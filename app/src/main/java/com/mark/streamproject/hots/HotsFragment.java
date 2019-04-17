package com.mark.streamproject.hots;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mark.streamproject.R;
import com.mark.streamproject.data.User;
import com.mark.streamproject.dialog.StreamDialog;

import java.util.ArrayList;


public class HotsFragment extends Fragment implements HotsContract.View, View.OnClickListener {
    HotsContract.Presenter mPresenter;
    private HotsAdapter mHotsAdapter;

    StreamDialog mStreamDialog;

    public HotsFragment() {}

    public static HotsFragment newInstance() {
        return new HotsFragment();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHotsAdapter = new HotsAdapter(mPresenter);
        mStreamDialog = new StreamDialog();
        mStreamDialog.setCancelable(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_hots, container, false);
        RecyclerView recyclerView = root.findViewById(R.id.recycler_hots);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(mHotsAdapter);

        root.findViewById(R.id.button_stream).setOnClickListener(this);
        root.findViewById(R.id.button_record).setOnClickListener(this);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.loadHotsData();
    }

    @Override
    public void setPresenter(HotsContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public boolean isActive() {
        return !isHidden();
    }

    @Override
    public void showHotsUI(ArrayList<User> users) {
        mHotsAdapter.updateData(users);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_stream:
                mPresenter.showStreamDialog();
                break;
            case R.id.button_record:
                break;
            default:
        }
    }
}
