package com.mark.streamproject.room;

import com.mark.streamproject.base.BasePresenter;
import com.mark.streamproject.base.BaseView;
import com.mark.streamproject.data.Message;
import com.mark.streamproject.data.Room;
import com.mark.streamproject.data.User;

import java.util.ArrayList;

public interface RoomContract {
    interface View extends BaseView<Presenter> {
        boolean isActive();

        void showRoomUi(Room room);

        void showMessageUi(ArrayList<Message> messages);

        void showAudienceUi(ArrayList<User> users);

        void inLikeListUi();

        void inDislikeListUi();

        void inFollowListUi();
    }

    interface Presenter extends BasePresenter {

        void hideProfileAndBottomNavigation();

        void showProfileAndBottomNavigation();

        void loadRoomData();

        void setRoomData(Room room);

        void enterRoom();

        void exitRoom();

        void sendMessage(String text, long time);

        void loadMessageData();

        void setMessageData(ArrayList<Message> messages);

        void refreshHotsData();

        void getRoomAudienceNumber();

        void add2LikeList();

        void removeFromLikeList();

        void add2DislikeList();

        void removeFromDisLikeList();

        void add2FollowList();

        void removeFromFollowList();

        void inLikeList();

        void inDislikeList();

        void inFollowList();

        void updateUserData();

        void updateLikeData(boolean hasChanged, boolean isAdded);

        void updateDislikeData(boolean hasChanged, boolean isAdded);

    }
}
