package com.mark.streamproject;

import android.support.annotation.NonNull;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.mark.streamproject.categeory.CategoryContract;
import com.mark.streamproject.categeory.CategoryPresenter;
import com.mark.streamproject.follow.FollowContract;
import com.mark.streamproject.follow.FollowPresenter;
import com.mark.streamproject.hots.HotsContract;
import com.mark.streamproject.hots.HotsPresenter;

import static com.google.common.base.Preconditions.checkNotNull;

public class MainPresenter implements MainContract.Presenter, HotsContract.Presenter,
        CategoryContract.Presenter, FollowContract.Presenter {

    private MainContract.View mMainView;

    private HotsPresenter mHotsPresenter;
    private CategoryPresenter mCategoryPresenter;
    private FollowPresenter mFollowPresenter;


    public MainPresenter(@NonNull MainContract.View mainView) {
        mMainView = checkNotNull(mainView, "mainView cannot be null!");
        mMainView.setPresenter(this);
    }

    void setHotsPresenter(HotsPresenter hotsPresenter) {
        mHotsPresenter = checkNotNull(hotsPresenter);
    }

    void setCategoryPresenter(CategoryPresenter categoryPresenter) {
        mCategoryPresenter = checkNotNull(categoryPresenter);
    }

    void setFollowPresenter(FollowPresenter followPresenter) {
        mFollowPresenter = checkNotNull(followPresenter);
    }

    @Override
    public void start() {

    }

    @Override
    public GoogleSignInAccount getGoogleSignInAccount() {
        return mMainView.getGoogleSignInAccountIntent();
    }

    /**
     * Open Hots
     */
    @Override
    public void openHots() {
        mMainView.openHotsUi();
    }

    /**
     * Open Category
     */
    @Override
    public void openCategory() {
        mMainView.openCategoryUi();
    }

    /**
     * Open Follow
     */
    @Override
    public void openFollow() {
        mMainView.openFollowUi();
    }

    @Override
    public void showStreamDialog() {
        mMainView.openStreamUi();
    }
}
