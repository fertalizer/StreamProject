package com.mark.streamproject.room;

import com.mark.streamproject.base.BasePresenter;
import com.mark.streamproject.base.BaseView;
import com.mark.streamproject.data.Message;
import com.mark.streamproject.data.Room;

import java.util.ArrayList;

public interface RoomContract {
    interface View extends BaseView<Presenter> {
        boolean isActive();

        void showRoomUi(Room room);

        void showMessageUi(ArrayList<Message> messages);
    }

    interface Presenter extends BasePresenter {

        void hideProfileAndBottomNavigation();

        void showProfileAndBottomNavigation();

        void loadRoomData();

        void setRoomData(Room room);

        void exitRoom();

        void sendMessage(String text);

        void loadMessageData();

        void setMessageData(ArrayList<Message> messages);
    }
}
