package com.mark.streamproject;

import androidx.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mark.streamproject.data.Room;
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
import com.mark.streamproject.util.UserManager;

import java.util.ArrayList;

import static com.google.common.base.Preconditions.checkNotNull;

public class MainPresenter implements MainContract.Presenter, HotsContract.Presenter,
        CategoryContract.Presenter, FollowContract.Presenter, RoomContract.Presenter {

    private MainContract.View mMainView;

    private HotsPresenter mHotsPresenter;
    private CategoryPresenter mCategoryPresenter;
    private FollowPresenter mFollowPresenter;

    private RoomPresenter mRoomPresenter;



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
        User user = new User();
        if (mMainView.getAccountIntent().getPhotoUrl() != null) {
            user.setImage(mMainView.getAccountIntent().getPhotoUrl().toString());
        }
        user.setId(mMainView.getAccountIntent().getId());
        user.setName(mMainView.getAccountIntent().getDisplayName());

        UserManager.getInstance().setUser(user);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("User").document(UserManager.getInstance().getUser().getId())
                .set(UserManager.getInstance().getUser())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(Constants.TAG, "DocumentSnapshot successfully written!");
                        getUserData();
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
    public void getUserData() {
        mMainView.showUserUi(UserManager.getInstance().getUser());
    }

    @Override
    public void createRoom(String title, String tag, String image, String watchId, long publishTime) {
        Room room = new Room();
        room.setTitle(title);
        room.setTag(tag);
        room.setImage(image);
        room.setWatchId(watchId);
        room.setPublishTime(publishTime);
        room.setStreamerId(UserManager.getInstance().getUser().getId());
        room.setStreamerName(UserManager.getInstance().getUser().getName());
        room.setStreamerImage(UserManager.getInstance().getUser().getImage());

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Room").document(room.getStreamerId())
                .set(room)
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
    public void setHotsData(ArrayList<Room> rooms) {
        mHotsPresenter.setHotsData(rooms);
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
    public void openRoom(@NonNull Room room) {
        mMainView.openRoomUi(room);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Room").document(room.getStreamerId())
                .collection("Audience").document(UserManager.getInstance().getUser().getId())
                .set(UserManager.getInstance().getUser())
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
    public void setRoomData(Room room) {
        mRoomPresenter.setRoomData(room);
    }

    @Override
    public void loadRoomData() {
        mRoomPresenter.loadRoomData();
    }

    @Override
    public void exitRoom() {
        mRoomPresenter.exitRoom();
    }

    @Override
    public void sendMessage(String text) {
        mRoomPresenter.sendMessage(text);
    }
}
