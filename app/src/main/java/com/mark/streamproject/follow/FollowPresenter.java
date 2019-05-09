package com.mark.streamproject.follow;

import static com.google.common.base.Preconditions.checkNotNull;

import android.util.Log;
import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.mark.streamproject.data.User;
import com.mark.streamproject.util.Constants;
import com.mark.streamproject.util.UserManager;
import java.util.ArrayList;
import java.util.Iterator;
import javax.annotation.Nullable;

public class FollowPresenter implements FollowContract.Presenter {
    private final FollowContract.View mFollowView;

    public FollowPresenter(@NonNull FollowContract.View followView) {
        mFollowView = checkNotNull(followView, "followView cannot be null!");
        mFollowView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void loadFollowData() {
        FirebaseFirestore.getInstance().collection(Constants.USER)
                .orderBy(Constants.STATUS, Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            System.err.println("Listen failed:" + e);
                            return;
                        }
                        ArrayList<User> users = new ArrayList<>();
                        Log.d(Constants.TAG, "UserManager = "
                                + UserManager.getInstance().getUser().getFollowList().size());
                        for (DocumentSnapshot doc : value) {
                            for (int i = 0; i < UserManager.getInstance().getUser().getFollowList().size(); i++) {
                                if (doc.toObject(User.class).getId()
                                        .equals(UserManager.getInstance().getUser().getFollowList().get(i))) {
                                    users.add(doc.toObject(User.class));
                                }
                            }
                        }
                        Log.d(Constants.TAG, "User size = " + users.size());
                        setFollowData(users);
                    }
                });

    }

    @Override
    public void setFollowData(ArrayList<User> users) {
        mFollowView.showFollowUi(users);
    }

    @Override
    public void openRoomByUserId(User user) {

    }

    @Override
    public void removeStreamer(User user) {
        Log.d(Constants.TAG, "User Id = " + user.getId());
        Iterator<String> iterator = UserManager.getInstance().getUser().getFollowList().iterator();
        while (iterator.hasNext()) {
            String streamerId = iterator.next();
            if (streamerId.equals(user.getId())) {
                iterator.remove();
            }
        }
        uploadFollowList();
    }

    private void uploadFollowList() {
        FirebaseFirestore.getInstance().collection(Constants.USER).document(UserManager.getInstance().getUser().getId())
                .update(Constants.FOLLOW_LIST, UserManager.getInstance().getUser().getFollowList())
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
    public void showAlertDialog(User user) {

    }
}
