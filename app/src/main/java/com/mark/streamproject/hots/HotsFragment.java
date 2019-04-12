package com.mark.streamproject.hots;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mark.streamproject.R;
import com.mark.streamproject.dialog.StreamDialog;


public class HotsFragment extends Fragment implements HotsContract.View, View.OnClickListener {
    HotsContract.Presenter mPresenter;

    StreamDialog mStreamDialog;

    public HotsFragment() {}

    public static HotsFragment newInstance() {
        return new HotsFragment();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mStreamDialog = new StreamDialog();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_hots, container, false);
        root.findViewById(R.id.button_stream).setOnClickListener(this);
        root.findViewById(R.id.button_record).setOnClickListener(this);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_stream:
                mStreamDialog.show(getFragmentManager(), "Stream");
                break;
            case R.id.button_record:
                break;
            default:
        }
    }
}
