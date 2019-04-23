package com.mark.streamproject;

import android.os.Bundle;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.mark.streamproject.base.BaseActivity;
import com.mark.streamproject.data.Room;
import com.mark.streamproject.data.User;
import com.mark.streamproject.dialog.StreamDialog;
import com.mark.streamproject.util.Constants;
import com.squareup.picasso.Picasso;

import static com.google.common.base.Preconditions.checkNotNull;

public class MainActivity extends BaseActivity implements MainContract.View {

    private BottomNavigationView mBottomNavigation;
    private Toolbar mToolbar;
    private StreamDialog mStreamDialog;
    private ImageView mUserImage;
    private TextView mUserName;

    private MainMvpController mMainMvpController;

    private MainContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
//        GoogleSignInAccount googleSignInAccount = getAccountIntent();
//        Log.d(Constants.TAG, googleSignInAccount.getPhotoUrl() + "");
//        Log.d(Constants.TAG, googleSignInAccount.getDisplayName() + "");
//        Log.d(Constants.TAG, googleSignInAccount.getEmail() + "");

    }

    @Override
    public GoogleSignInAccount getAccountIntent() {
        return this.getIntent().getParcelableExtra("ACCOUNT");
    }

    private void init() {
        setContentView(R.layout.activity_main);

        mMainMvpController = MainMvpController.create(this);
        mPresenter.openHots();

//        setToolbar();
        setBottomNavigation();
        setUserLayout();
    }


    @Override
    public void setPresenter(MainContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    private void setUserLayout() {
        mUserImage = findViewById(R.id.image_room_user);
        mUserName = findViewById(R.id.text_name);
        mPresenter.setUserData();
    }

    private void setToolbar() {
        // Retrieve the AppCompact Toolbar
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");
    }

    private void setBottomNavigation() {

        mBottomNavigation = findViewById(R.id.bottom_navigation_main);
        mBottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        BottomNavigationMenuView menuView =
                (BottomNavigationMenuView) mBottomNavigation.getChildAt(0);

        for (int i = 0; i < menuView.getChildCount(); i++) {
            final View iconView = menuView.getChildAt(i).findViewById(R.id.icon);
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
    public void showUserUi(User user) {
        mUserName.setText(user.getName());
        if (!user.getImage().equals("")) {
            Picasso.get()
                    .load(user.getImage())
                    .resize(70, 70)
                    .centerCrop()
                    .into(mUserImage);
        }

    }

    @Override
    public void showBottomNavigationUi() {
        mBottomNavigation.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideBottomNavigationUi() {
        mBottomNavigation.setVisibility(View.GONE);
    }

    @Override
    public void showProfileUi() {
        findViewById(R.id.layout_profile).setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProfileUi() {
        findViewById(R.id.layout_profile).setVisibility(View.GONE);
    }

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

    @Override
    public void openStreamUi() {
        if (mStreamDialog == null) {

            mStreamDialog = new StreamDialog();
            mStreamDialog.setMainPresenter(mPresenter);
            mStreamDialog.setCancelable(false);
            mStreamDialog.show(getSupportFragmentManager(), Constants.STREAM);

        } else if (!mStreamDialog.isAdded()) {
            mStreamDialog.setCancelable(false);
            mStreamDialog.show(getSupportFragmentManager(), Constants.STREAM);
        }
    }

    @Override
    public void openRoomUi(Room room) {
        mMainMvpController.findOrCreateRoomView(room);
    }
}
