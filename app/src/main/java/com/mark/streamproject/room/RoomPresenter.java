package com.mark.streamproject.room;

import androidx.annotation.NonNull;

import static com.google.common.base.Preconditions.checkNotNull;

public class RoomPresenter implements RoomContract.Presenter{

    private final RoomContract.View mRoomView;

    public RoomPresenter(@NonNull RoomContract.View roomView) {
        mRoomView = checkNotNull(roomView, "roomView cannot be null!");
        mRoomView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void hideProfileAndBottomNavigation() {

    }

    @Override
    public void showProfileAndBottomNavigation() {

    }
}
