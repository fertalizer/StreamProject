package com.mark.streamproject;

import android.accounts.Account;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.mark.streamproject.base.BasePresenter;
import com.mark.streamproject.base.BaseView;

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



    }

    interface Presenter extends BasePresenter {

        GoogleSignInAccount getAccount();

        void openHots();

        void openCategory();

        void openFollow();

        void setUserData();

    }
}
