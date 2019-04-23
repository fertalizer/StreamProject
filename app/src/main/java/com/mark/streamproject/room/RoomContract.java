package com.mark.streamproject.room;

import com.mark.streamproject.base.BasePresenter;
import com.mark.streamproject.base.BaseView;
import com.mark.streamproject.data.Room;

public interface RoomContract {
    interface View extends BaseView<Presenter> {
        boolean isActive();

        void showRoomUi(Room room);
    }

    interface Presenter extends BasePresenter {

        void hideProfileAndBottomNavigation();

        void showProfileAndBottomNavigation();

        void loadRoomData();

        void setRoomData(Room room);

        void exitRoom();

        void sendMessage(String text);
    }
}
