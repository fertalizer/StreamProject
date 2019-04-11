package com.mark.streamproject.follow;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mark.streamproject.R;


public class FollowFragment extends Fragment implements FollowContract.View {

    FollowContract.Presenter mPresenter;

    public FollowFragment() {}

    public static FollowFragment newInstance() {
        return new FollowFragment();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_follow, container, false);
    }

    @Override
    public void setPresenter(FollowContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public boolean isActive() {
        return !isHidden();
    }
}
