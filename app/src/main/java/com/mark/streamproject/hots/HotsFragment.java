package com.mark.streamproject.hots;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mark.streamproject.R;


public class HotsFragment extends Fragment implements HotsContract.View {
    HotsContract.Presenter mPresenter;

    public HotsFragment() {}

    public static HotsFragment newInstance() {
        return new HotsFragment();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_hots, container, false);
    }

    @Override
    public void setPresenter(HotsContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public boolean isActive() {
        return !isHidden();
    }
}
