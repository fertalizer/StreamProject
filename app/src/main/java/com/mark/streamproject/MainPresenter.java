package com.mark.streamproject;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mark.streamproject.categeory.CategoryContract;
import com.mark.streamproject.categeory.CategoryPresenter;
import com.mark.streamproject.data.User;
import com.mark.streamproject.follow.FollowContract;
import com.mark.streamproject.follow.FollowPresenter;
import com.mark.streamproject.hots.HotsContract;
import com.mark.streamproject.hots.HotsPresenter;
import com.mark.streamproject.util.Constants;

import static com.google.common.base.Preconditions.checkNotNull;

public class MainPresenter implements MainContract.Presenter, HotsContract.Presenter,
        CategoryContract.Presenter, FollowContract.Presenter {

    private MainContract.View mMainView;

    private HotsPresenter mHotsPresenter;
    private CategoryPresenter mCategoryPresenter;
    private FollowPresenter mFollowPresenter;

    private User mUser;


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
    public void setUserData() {
        mUser = new User();
        if (mMainView.getAccountIntent().getPhotoUrl() != null) {
            mUser.setImage(mMainView.getAccountIntent().getPhotoUrl().toString());
        }
        mUser.setId(mMainView.getAccountIntent().getId());
        mUser.setName(mMainView.getAccountIntent().getDisplayName());

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("User").document(mUser.getId())
                .set(mUser)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(Constants.TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(Constants.TAG, "Error writing document", e);
                    }
                });
    }

    @Override
    public void start() {

    }

    @Override
    public GoogleSignInAccount getAccount() {
        return mMainView.getAccountIntent();
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
