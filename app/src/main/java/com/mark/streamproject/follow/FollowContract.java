package com.mark.streamproject.follow;

import com.mark.streamproject.base.BasePresenter;
import com.mark.streamproject.base.BaseView;
import com.mark.streamproject.data.User;

import java.util.ArrayList;

/**
 * Created by Mark Ho on April 2019.
 *
 * This specifies the contract between the view and the presenter.
 */
public interface FollowContract {

    interface View extends BaseView<Presenter> {

        boolean isActive();

        void showFollowUi(ArrayList<User> users);
    }

    interface Presenter extends BasePresenter {

        void loadFollowData();

        void setFollowData(ArrayList<User> users);

        void openRoomByUserId(User user);

        void removeStreamer(User user);
    }
}