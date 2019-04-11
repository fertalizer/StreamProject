package com.mark.streamproject.follow;

import android.support.annotation.NonNull;

import static com.google.common.base.Preconditions.checkNotNull;

public class FollowPresenter implements FollowContract.Presenter {
    private final FollowContract.View mFollowView;

    public FollowPresenter(@NonNull FollowContract.View followView) {
        mFollowView = checkNotNull(followView, "hotsView cannot be null!");
        mFollowView.setPresenter(this);
    }

    @Override
    public void start() {

    }
}
