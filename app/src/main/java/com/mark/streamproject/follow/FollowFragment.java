package com.mark.streamproject.follow;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mark.streamproject.R;
import com.mark.streamproject.StreamProject;


public class FollowFragment extends Fragment implements FollowContract.View {

    private FollowContract.Presenter mPresenter;

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
        View root = inflater.inflate(R.layout.fragment_follow, container, false);
        TextView textView = root.findViewById(R.id.text_follow);
        textView.setTypeface(Typeface.createFromAsset(StreamProject.getAppContext().getAssets(), "fonts/Minecraftia-Regular.ttf"));
        return root;
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
