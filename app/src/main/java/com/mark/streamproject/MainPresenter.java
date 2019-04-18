package com.mark.streamproject;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mark.streamproject.room.RoomContract;
import com.mark.streamproject.room.RoomPresenter;
import com.mark.streamproject.categeory.CategoryContract;
import com.mark.streamproject.categeory.CategoryPresenter;
import com.mark.streamproject.data.User;
import com.mark.streamproject.follow.FollowContract;
import com.mark.streamproject.follow.FollowPresenter;
import com.mark.streamproject.hots.HotsContract;
import com.mark.streamproject.hots.HotsPresenter;
import com.mark.streamproject.util.Constants;

import java.util.ArrayList;

import static com.google.common.base.Preconditions.checkNotNull;

public class MainPresenter implements MainContract.Presenter, HotsContract.Presenter,
        CategoryContract.Presenter, FollowContract.Presenter, RoomContract.Presenter {

    private MainContract.View mMainView;

    private HotsPresenter mHotsPresenter;
    private CategoryPresenter mCategoryPresenter;
    private FollowPresenter mFollowPresenter;

    private RoomPresenter mRoomPresenter;

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

    void setRoomPresenter(RoomPresenter roomPresenter) {
        mRoomPresenter = checkNotNull(roomPresenter);
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
                        mMainView.showUserUi(getUserData());
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
    public User getUserData() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("User").document(mMainView.getAccountIntent().getId());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(Constants.TAG, "DocumentSnapshot data: " + document.getData());
                        mUser = document.toObject(User.class);
                    } else {
                        Log.d(Constants.TAG, "No such document");
                    }
                } else {
                    Log.d(Constants.TAG, "get failed with ", task.getException());
                }
            }
        });

        return mUser;
    }

    @Override
    public void start() {

    }

    @Override
    public GoogleSignInAccount getAccount() {
        return mMainView.getAccountIntent();
    }


    @Override
    public void hideProfileAndBottomNavigation() {
        mMainView.hideBottomNavigationUi();
        mMainView.hideProfileUi();
    }

    @Override
    public void showProfileAndBottomNavigation() {
        mMainView.showBottomNavigationUi();
        mMainView.showProfileUi();
    }

    /**
     * Open Hots
     */
    @Override
    public void openHots() {
        mMainView.openHotsUi();
    }

    @Override
    public void loadHotsData() {
        if (mHotsPresenter != null) {
            mHotsPresenter.loadHotsData();
        }
    }

    @Override
    public void setHotsData(ArrayList<User> users) {
        mHotsPresenter.setHotsData(users);
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

    @Override
    public void openRoom() {
        mMainView.openRoomUi();
    }
}
