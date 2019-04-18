package com.mark.streamproject.hots;

import com.mark.streamproject.base.BasePresenter;
import com.mark.streamproject.base.BaseView;
import com.mark.streamproject.data.User;

import java.util.ArrayList;

/**
 * Created by Mark Ho on April 2019.
 *
 * This specifies the contract between the view and the presenter.
 */
public interface HotsContract {

    interface View extends BaseView<Presenter> {
        boolean isActive();

        void showHotsUI(ArrayList<User> users);
    }

    interface Presenter extends BasePresenter {

        void showStreamDialog();

        void loadHotsData();

        void setHotsData(ArrayList<User> users);

        void openRoom();

    }
}
