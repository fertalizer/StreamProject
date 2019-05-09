package com.mark.streamproject;

import static com.google.common.base.Preconditions.checkNotNull;

import android.util.Log;
import androidx.annotation.NonNull;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mark.streamproject.categeory.CategoryContract;
import com.mark.streamproject.categeory.CategoryPresenter;
import com.mark.streamproject.data.Message;
import com.mark.streamproject.data.Room;
import com.mark.streamproject.data.User;
import com.mark.streamproject.follow.FollowContract;
import com.mark.streamproject.follow.FollowPresenter;
import com.mark.streamproject.hots.HotsContract;
import com.mark.streamproject.hots.HotsPresenter;
import com.mark.streamproject.room.RoomContract;
import com.mark.streamproject.room.RoomPresenter;
import com.mark.streamproject.util.Constants;
import com.mark.streamproject.util.UserManager;

import java.util.ArrayList;

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
    public void getUserData() {
        FirebaseFirestore.getInstance().collection(Constants.USER).document(mMainView.getAccountIntent().getId())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d(Constants.TAG, "DocumentSnapshot data: " + document.getData());
                                UserManager.getInstance().setUser(document.toObject(User.class));
                                changeStatus(Constants.ONLINE);
                                mMainView.showUserUi(UserManager.getInstance().getUser());
                            } else {
                                Log.d(Constants.TAG, "No such document");
                                setUserData();
                            }
                        } else {
                            Log.d(Constants.TAG, "get failed with ", task.getException());
                        }
                    }
                });
    }

    @Override
    public void setUserData() {
        User user = new User();
        if (mMainView.getAccountIntent().getPhotoUrl() != null) {
            user.setImage(mMainView.getAccountIntent().getPhotoUrl().toString());
        }
        user.setId(mMainView.getAccountIntent().getId());
        user.setName(mMainView.getAccountIntent().getDisplayName());
        user.setStatus(Constants.ONLINE);
        UserManager.getInstance().setUser(user);

        createUserData();
    }

    private void createUserData() {
        FirebaseFirestore.getInstance().collection(Constants.USER).document(UserManager.getInstance().getUser().getId())
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
    public void updateUserData() {
        FirebaseFirestore.getInstance().collection(Constants.USER).document(UserManager.getInstance().getUser().getId())
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
    public void setRoomData(String title, String tag, String image, String watchId, long publishTime) {
        Room room = new Room();
        room.setTitle(title);
        room.setTag(tag);
        room.setImage(image);
        room.setWatchId(watchId);
        room.setPublishTime(publishTime);
        room.setStreamerId(UserManager.getInstance().getUser().getId());
        room.setStreamerName(UserManager.getInstance().getUser().getName());
        room.setStreamerImage(UserManager.getInstance().getUser().getImage());

        createRoom(room);
    }

    private void createRoom(Room room) {
        FirebaseFirestore.getInstance().collection(Constants.ROOM).document(room.getStreamerId())
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
    public void closeRoom() {
        FirebaseFirestore.getInstance().collection(Constants.ROOM).document(UserManager.getInstance().getUser().getId())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(Constants.TAG, "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(Constants.TAG, "Error deleting document", e);
                    }
                });
    }

    @Override
    public void cleanMessage() {

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

    @Override
    public void goToDeskTop() {
        mMainView.moveTaskToBack();
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
    public void setHotsData(ArrayList<Room> rooms, ArrayList<Integer> numbers) {
        mHotsPresenter.setHotsData(rooms, numbers);
    }

    @Override
    public boolean isHotsRefreshing() {
        return mHotsPresenter.isHotsRefreshing();
    }

    @Override
    public void showStreamDialog() {
        mMainView.openStreamUi();
    }


    @Override
    public void showDescriptionDialog() {
        mMainView.openDescriptionUi();
    }

    /**
     * Open Category
     */
    @Override
    public void openCategory() {
        mMainView.openCategoryUi();
    }

    @Override
    public void loadCategoryData() {
        if (mCategoryPresenter != null) {
            mCategoryPresenter.loadCategoryData();
        }
    }

    @Override
    public void setCategoryData(ArrayList<Room> rooms) {
        mCategoryPresenter.setCategoryData(rooms);
    }

    @Override
    public boolean isCategoryRefreshing() {
        return mCategoryPresenter.isCategoryRefreshing();
    }

    @Override
    public void searchRoomData(String string) {
        mCategoryPresenter.searchRoomData(string);
    }

    /**
     * Open Follow
     */
    @Override
    public void openFollow() {
        mMainView.openFollowUi();
    }

    @Override
    public void loadFollowData() {
        if (mFollowPresenter != null) {
            mFollowPresenter.loadFollowData();
        }
    }

    @Override
    public void setFollowData(ArrayList<User> users) {
        mFollowPresenter.setFollowData(users);
    }

    @Override
    public void openRoomByUserId(User user) {
        FirebaseFirestore.getInstance().collection(Constants.ROOM).document(user.getId())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d(Constants.TAG, "DocumentSnapshot data: " + document.getData());
                                Room room = document.toObject(Room.class);
                                openRoom(room);
                            } else {
                                Log.d(Constants.TAG, "No such document");
                            }
                        } else {
                            Log.d(Constants.TAG, "get failed with ", task.getException());
                        }
                    }
                });
    }

    @Override
    public void removeStreamer(User user) {
        mFollowPresenter.removeStreamer(user);
    }

    @Override
    public void showAlertDialog(User user) {
        mMainView.openAlertDialogUi(user);
    }

    @Override
    public void openRoom(@NonNull Room room) {
        mMainView.openRoomUi(room);
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
    public void enterRoom() {
        mRoomPresenter.enterRoom();
    }

    @Override
    public void exitRoom() {
        mRoomPresenter.exitRoom();
    }

    @Override
    public void sendMessage(String text, long time) {
        mRoomPresenter.sendMessage(text, time);
    }

    @Override
    public void loadMessageData() {
        mRoomPresenter.loadMessageData();
    }

    @Override
    public void setMessageData(ArrayList<Message> messages) {
        mRoomPresenter.setMessageData(messages);
    }

    @Override
    public void refreshHotsData() {
        mHotsPresenter.loadHotsData();
    }

    @Override
    public void getRoomAudienceNumber() {
        mRoomPresenter.getRoomAudienceNumber();
    }

    @Override
    public void add2LikeList() {
        mRoomPresenter.add2LikeList();
    }

    @Override
    public void removeFromLikeList() {
        mRoomPresenter.removeFromLikeList();
    }

    @Override
    public void add2DislikeList() {
        mRoomPresenter.add2DislikeList();
    }

    @Override
    public void removeFromDisLikeList() {
        mRoomPresenter.removeFromDisLikeList();
    }

    @Override
    public void add2FollowList() {
        mRoomPresenter.add2FollowList();
    }

    @Override
    public void removeFromFollowList() {
        mRoomPresenter.removeFromFollowList();
    }

    @Override
    public void inLikeList() {
        mRoomPresenter.inLikeList();
    }

    @Override
    public void inDislikeList() {
        mRoomPresenter.inDislikeList();
    }

    @Override
    public void inFollowList() {
        mRoomPresenter.inFollowList();
    }

    @Override
    public void updateLikeData(boolean hasChanged, boolean isAdded) {
        mRoomPresenter.updateLikeData(hasChanged, isAdded);
    }

    @Override
    public void updateDislikeData(boolean hasChanged, boolean isAdded) {
        mRoomPresenter.updateDislikeData(hasChanged, isAdded);
    }

    @Override
    public void changeStatus(int status) {
        UserManager.getInstance().getUser().setStatus(status);
        uploadStatus();
    }

    private void uploadStatus() {
        FirebaseFirestore.getInstance().collection(Constants.USER).document(UserManager.getInstance().getUser().getId())
                .update(Constants.STATUS, UserManager.getInstance().getUser().getStatus())
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


}
