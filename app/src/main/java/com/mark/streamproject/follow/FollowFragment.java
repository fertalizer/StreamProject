package com.mark.streamproject.follow;

import android.app.AlertDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mark.streamproject.R;
import com.mark.streamproject.StreamProject;
import com.mark.streamproject.data.User;

import java.util.ArrayList;


public class FollowFragment extends Fragment implements FollowContract.View {

    private FollowAdapter mFollowAdapter;

    private FollowContract.Presenter mPresenter;

    public FollowFragment() {}

    public static FollowFragment newInstance() {
        return new FollowFragment();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFollowAdapter = new FollowAdapter(mPresenter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_follow, container, false);
        RecyclerView recyclerView = root.findViewById(R.id.recycler_follow);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(mFollowAdapter);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.loadFollowData();
    }

    @Override
    public void setPresenter(FollowContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public boolean isActive() {
        return !isHidden();
    }

    @Override
    public void showFollowUi(ArrayList<User> users) {
        mFollowAdapter.updateData(users);
    }


}
