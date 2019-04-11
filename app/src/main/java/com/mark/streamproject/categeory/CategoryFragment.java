package com.mark.streamproject.categeory;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mark.streamproject.R;
import com.mark.streamproject.hots.HotsContract;


public class CategoryFragment extends Fragment implements CategoryContract.View {

    CategoryContract.Presenter mPresenter;

    public CategoryFragment() {}

    public static CategoryFragment newInstance() {
        return new CategoryFragment();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_category, container, false);
    }

    @Override
    public void setPresenter(CategoryContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public boolean isActive() {
        return !isHidden();
    }
}
