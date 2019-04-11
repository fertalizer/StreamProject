package com.mark.streamproject;

import android.os.Bundle;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mark.streamproject.base.BaseActivity;

import static com.google.common.base.Preconditions.checkNotNull;

public class MainActivity extends BaseActivity implements MainContract.View {

    private BottomNavigationView mBottomNavigation;
    private Toolbar mToolbar;

    private MainMvpController mMainMvpController;

    private MainContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
    }

    private void init() {
        setContentView(R.layout.activity_main);

        mMainMvpController = MainMvpController.create(this);
        mPresenter.openHots();

        setToolbar();
        setBottomNavigation();
    }


    @Override
    public void setPresenter(MainContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    private void setToolbar() {
        // Retrieve the AppCompact Toolbar
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
    }

    private void setBottomNavigation() {

        mBottomNavigation = findViewById(R.id.bottom_navigation_main);
        mBottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        BottomNavigationMenuView menuView =
                (BottomNavigationMenuView) mBottomNavigation.getChildAt(0);

        for (int i = 0; i < menuView.getChildCount(); i++) {
            final View iconView = menuView.getChildAt(i).findViewById(android.support.design.R.id.icon);
            final ViewGroup.LayoutParams layoutParams = iconView.getLayoutParams();
            layoutParams.height = (int) getResources().getDimension(R.dimen.size_bottom_nav_icon);
            layoutParams.width = (int) getResources().getDimension(R.dimen.size_bottom_nav_icon);
            iconView.setLayoutParams(layoutParams);
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = item -> {

        switch (item.getItemId()) {
            case R.id.navigation_home:
                mPresenter.openHots();
                return true;
            case R.id.navigation_category:
                mPresenter.openCategory();
                return true;
            case R.id.navigation_follow:
                mPresenter.openFollow();
                return true;
            default:
                return false;
        }
    };

    @Override
    public void openHotsUi() {
        mMainMvpController.findOrCreateHotsView();
    }

    @Override
    public void openCategoryUi() {
        mMainMvpController.findOrCreateCategoryView();
    }

    @Override
    public void openFollowUi() {
        mMainMvpController.findOrCreateFollowView();
    }
}
