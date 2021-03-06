package com.mark.streamproject;

import android.accounts.Account;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.api.client.util.DateTime;
import com.mark.streamproject.base.BasePresenter;
import com.mark.streamproject.base.BaseView;
import com.mark.streamproject.data.Room;
import com.mark.streamproject.data.User;

/**
 * Created by Mark Ho on April 2019.
 *
 * This specifies the contract between the view and the presenter.
 */
public interface MainContract {
    interface View extends BaseView<Presenter> {

        GoogleSignInAccount getAccountIntent();

        void openHotsUi();

        void openCategoryUi();

        void openFollowUi();

        void openStreamUi();

        void openAlertDialogUi(User user);

        void openDescriptionUi();

        void showUserUi(User user);

        void openRoomUi(Room room);

        void showBottomNavigationUi();

        void hideBottomNavigationUi();

        void showProfileUi();

        void hideProfileUi();

        void moveTaskToBack();

    }

    interface Presenter extends BasePresenter {

        GoogleSignInAccount getAccount();

        void openHots();

        void openCategory();

        void openFollow();

        void setUserData();

        void getUserData();

        void updateUserData();

        void setRoomData(String title, String tag, String image, String watchId, long publishTime);

        void closeRoom();

        void cleanMessage();

        void removeStreamer(User user);

        void changeStatus(int status);

        void showDescriptionDialog();

        void goToDeskTop();

    }
}
