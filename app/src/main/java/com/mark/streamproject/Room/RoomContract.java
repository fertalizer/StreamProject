package com.mark.streamproject.Room;

import com.mark.streamproject.base.BasePresenter;
import com.mark.streamproject.base.BaseView;

public interface RoomContract {
    interface View extends BaseView<Presenter> {
        boolean isActive();
    }

    interface Presenter extends BasePresenter {

        void hideProfileAndBottomNavigation();

        void showProfileAndBottomNavigation();

    }
}
