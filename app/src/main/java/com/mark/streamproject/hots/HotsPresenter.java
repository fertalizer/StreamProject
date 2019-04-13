package com.mark.streamproject.hots;

import android.support.annotation.NonNull;

import static com.google.common.base.Preconditions.checkNotNull;

public class HotsPresenter implements HotsContract.Presenter {

    private final HotsContract.View mHotsView;

    public HotsPresenter(@NonNull HotsContract.View hotsView) {
        mHotsView = checkNotNull(hotsView, "hotsView cannot be null!");
        mHotsView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void showStreamDialog() {

    }
}
