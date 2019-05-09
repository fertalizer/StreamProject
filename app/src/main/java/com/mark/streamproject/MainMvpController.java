package com.mark.streamproject;

import static com.google.common.base.Preconditions.checkNotNull;

import androidx.annotation.NonNull;
import androidx.annotation.StringDef;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import com.mark.streamproject.categeory.CategoryFragment;
import com.mark.streamproject.categeory.CategoryPresenter;
import com.mark.streamproject.data.Room;
import com.mark.streamproject.follow.FollowFragment;
import com.mark.streamproject.follow.FollowPresenter;
import com.mark.streamproject.hots.HotsFragment;
import com.mark.streamproject.hots.HotsPresenter;
import com.mark.streamproject.room.RoomFragment;
import com.mark.streamproject.room.RoomPresenter;
import com.mark.streamproject.util.ActivityUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Mark Ho on April 2019.
 *
 * Class that creates fragments (MVP views) and makes the necessary connections between them.
 */
public class MainMvpController {
    private final FragmentActivity mActivity;
    private MainPresenter mMainPresenter;

    private HotsPresenter mHotsPresenter;
    private CategoryPresenter mCategoryPresenter;
    private FollowPresenter mFollowPresenter;

    private RoomPresenter mRoomPresenter;

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({
            HOTS, CATEGORY, FOLLOW, ROOM
    })
    public @interface FragmentType {}
    static final String HOTS    = "HOTS";
    static final String CATEGORY = "CATEGORY";
    static final String FOLLOW    = "FOLLOW";
    static final String ROOM  = "ROOM";


    private MainMvpController(@NonNull FragmentActivity activity) {
        mActivity = activity;
    }

    /**
     * Creates a controller.
     * @param activity the context activity
     * @return a MainMvpController
     */
    static MainMvpController create(@NonNull FragmentActivity activity) {
        checkNotNull(activity);
        MainMvpController mainMvpController = new MainMvpController(activity);
        mainMvpController.createMainPresenter();
        return mainMvpController;
    }

    /**
     * Hots View
     */
    void findOrCreateHotsView() {

        HotsFragment hotsFragment = findOrCreateHotsFragment();

        if (mCategoryPresenter == null) {
            mHotsPresenter = new HotsPresenter(hotsFragment);
            mMainPresenter.setHotsPresenter(mHotsPresenter);
            hotsFragment.setPresenter(mMainPresenter);
        }
    }

    /**
     * Category View
     */
    void findOrCreateCategoryView() {

        CategoryFragment categoryFragment = findOrCreateCategoryFragment();

        if (mCategoryPresenter == null) {
            mCategoryPresenter = new CategoryPresenter(categoryFragment);
            mMainPresenter.setCategoryPresenter(mCategoryPresenter);
            categoryFragment.setPresenter(mMainPresenter);
        }
    }

    /**
     * Follow View
     */
    void findOrCreateFollowView() {

        FollowFragment followFragment = findOrCreateFollowFragment();

        if (mFollowPresenter == null) {
            mFollowPresenter = new FollowPresenter(followFragment);
            mMainPresenter.setFollowPresenter(mFollowPresenter);
            followFragment.setPresenter(mMainPresenter);
        }
    }

    /**
     * Detail View
     */
    void findOrCreateRoomView(Room room) {

        RoomFragment roomFragment = createRoomFragment();

        mRoomPresenter = new RoomPresenter(roomFragment);
        mRoomPresenter.setRoomData(room);
        mMainPresenter.setRoomPresenter(mRoomPresenter);
        roomFragment.setPresenter(mMainPresenter);
    }



    /**
     * Hots Fragment
     * @return HotsFragment
     */
    @NonNull
    private HotsFragment findOrCreateHotsFragment() {

        HotsFragment hotsFragment =
                (HotsFragment) getFragmentManager().findFragmentByTag(HOTS);
        if (hotsFragment == null) {
            // Create the fragment
            hotsFragment = HotsFragment.newInstance();
        }

        ActivityUtils.showOrAddFragmentByTag(
                getFragmentManager(), hotsFragment, HOTS);

        return hotsFragment;
    }

    /**
     * Category Fragment
     * @return CategoryFragment
     */
    @NonNull
    private CategoryFragment findOrCreateCategoryFragment() {

        CategoryFragment categoryFragment =
                (CategoryFragment) getFragmentManager().findFragmentByTag(CATEGORY);
        if (categoryFragment == null) {
            // Create the fragment
            categoryFragment = CategoryFragment.newInstance();
        }

        ActivityUtils.showOrAddFragmentByTag(
                getFragmentManager(), categoryFragment, CATEGORY);

        return categoryFragment;
    }

    /**
     * Follow Fragment
     * @return FollowFragment
     */
    @NonNull
    private FollowFragment findOrCreateFollowFragment() {

        FollowFragment followFragment =
                (FollowFragment) getFragmentManager().findFragmentByTag(FOLLOW);
        if (followFragment == null) {
            // Create the fragment
            followFragment = FollowFragment.newInstance();
        }

        ActivityUtils.showOrAddFragmentByTag(
                getFragmentManager(), followFragment, FOLLOW);

        return followFragment;
    }

    /**
     * Detail Fragment
     * @return DetailFragment
     */
    @NonNull
    private RoomFragment createRoomFragment() {

        RoomFragment roomFragment = RoomFragment.newInstance();

        ActivityUtils.addFragmentByTag(
                getFragmentManager(), roomFragment, ROOM);

        return roomFragment;
    }



    /**
     * Create Main Presenter
     * @return MainPresenter
     */
    private MainPresenter createMainPresenter() {
        mMainPresenter = new MainPresenter((MainActivity) mActivity);

        return mMainPresenter;
    }

    private FragmentManager getFragmentManager() {
        return mActivity.getSupportFragmentManager();
    }
}
