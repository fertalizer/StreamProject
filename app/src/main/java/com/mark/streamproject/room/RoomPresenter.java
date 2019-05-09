package com.mark.streamproject.room;

import static com.google.common.base.Preconditions.checkNotNull;

import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mark.streamproject.data.Message;
import com.mark.streamproject.data.Room;
import com.mark.streamproject.data.User;
import com.mark.streamproject.util.Constants;
import com.mark.streamproject.util.UserManager;

import java.util.ArrayList;
import java.util.Iterator;

public class RoomPresenter implements RoomContract.Presenter {

    private final RoomContract.View mRoomView;

    private Room mRoom;

    public RoomPresenter(@NonNull RoomContract.View roomView) {
        mRoomView = checkNotNull(roomView, "roomView cannot be null!");
        mRoomView.setPresenter(this);
    }

    @Override
    public void setRoomData(Room room) {
        mRoom = room;
    }

    @Override
    public void loadRoomData() {
        mRoomView.showRoomUi(mRoom);
    }

    @Override
    public void start() {

    }

    @Override
    public void hideProfileAndBottomNavigation() {

    }

    @Override
    public void showProfileAndBottomNavigation() {

    }

    @Override
    public void enterRoom() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Room").document(mRoom.getStreamerId())
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
    public void exitRoom() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Room").document(mRoom.getStreamerId())
                .collection("Audience").document(UserManager.getInstance().getUser().getId())
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
    public void sendMessage(String text, long time) {
        if (!"".equals(text)) {
            Message message = new Message();
            message.setName(UserManager.getInstance().getUser().getName());
            message.setPublishTime(time);
            message.setContent(text);

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("Room").document(mRoom.getStreamerId())
                    .collection("Message").document()
                    .set(message)
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

    @Override
    public void loadMessageData() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Room").document(mRoom.getStreamerId()).collection("Message")
                .orderBy("publishTime", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(Constants.TAG, "Listen failed.", e);
                            return;
                        }

                        ArrayList<Message> messages = new ArrayList<>();
                        for (QueryDocumentSnapshot doc : value) {
                            messages.add(doc.toObject(Message.class));
                        }
                        Log.d(Constants.TAG, "Message size = " + messages.size());
                        setMessageData(messages);
                    }
                });
    }

    @Override
    public void setMessageData(ArrayList<Message> messages) {
        mRoomView.showMessageUi(messages);
    }

    @Override
    public void refreshHotsData() {

    }

    @Override
    public void updateUserData() {

    }

    @Override
    public void getRoomAudienceNumber() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Room").document(mRoom.getStreamerId()).collection("Audience")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(Constants.TAG, "Listen failed.", e);
                            return;
                        }

                        ArrayList<User> users = new ArrayList<>();
                        for (QueryDocumentSnapshot doc : value) {
                            users.add(doc.toObject(User.class));
                        }
                        Log.d(Constants.TAG, "User size = " + users.size());
                        mRoomView.showAudienceUi(users);
                    }
                });
    }

    @Override
    public void add2LikeList() {
        UserManager.getInstance().getUser().getLikeList().add(mRoom.getWatchId());
    }

    @Override
    public void removeFromLikeList() {
        Iterator<String> iterator = UserManager.getInstance().getUser().getLikeList().iterator();
        while (iterator.hasNext()) {
            String watchId = (String) iterator.next();
            if (watchId.equals(mRoom.getWatchId())) {
                iterator.remove();
            }
        }
    }

    @Override
    public void add2DislikeList() {
        UserManager.getInstance().getUser().getDislikeList().add(mRoom.getWatchId());
    }

    @Override
    public void removeFromDisLikeList() {
        Iterator<String> iterator = UserManager.getInstance().getUser().getDislikeList().iterator();
        while (iterator.hasNext()) {
            String watchId = (String) iterator.next();
            if (watchId.equals(mRoom.getWatchId())) {
                iterator.remove();
            }
        }
    }

    @Override
    public void add2FollowList() {
        UserManager.getInstance().getUser().getFollowList().add(mRoom.getStreamerId());
    }

    @Override
    public void removeFromFollowList() {
        Iterator<String> iterator = UserManager.getInstance().getUser().getFollowList().iterator();
        while (iterator.hasNext()) {
            String streamerId = (String) iterator.next();
            if (streamerId.equals(mRoom.getStreamerId())) {
                iterator.remove();
            }
        }
    }

    @Override
    public void inLikeList() {
        for (int i = 0; i < UserManager.getInstance().getUser().getLikeList().size(); i++) {
            if (UserManager.getInstance().getUser().getLikeList().get(i).equals(mRoom.getWatchId())) {
                mRoomView.inLikeListUi();
            }
        }
    }

    @Override
    public void inDislikeList() {
        for (int i = 0; i < UserManager.getInstance().getUser().getDislikeList().size(); i++) {
            if (UserManager.getInstance().getUser().getDislikeList().get(i).equals(mRoom.getWatchId())) {
                mRoomView.inDislikeListUi();
            }
        }
    }

    @Override
    public void inFollowList() {
        for (int i = 0; i < UserManager.getInstance().getUser().getFollowList().size(); i++) {
            if (UserManager.getInstance().getUser().getFollowList().get(i).equals(mRoom.getStreamerId())) {
                mRoomView.inFollowListUi();
            }
        }
    }

    @Override
    public void updateLikeData(boolean hasChanged, boolean isAdded) {
        if (hasChanged) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("Room").document(mRoom.getStreamerId())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    Log.d(Constants.TAG, "DocumentSnapshot data: " + document.getData());
                                    Room room = document.toObject(Room.class);
                                    int likeNumber;
                                    int dislikeNumber = room.getDislike();
                                    if (isAdded) {
                                        likeNumber = room.getLike() + 1;
                                    } else {
                                        likeNumber = room.getLike() - 1;
                                    }
                                    room.setLike(likeNumber);
                                    updateLikeNumber(db, room);
                                } else {
                                    Log.d(Constants.TAG, "No such document");
                                }
                            } else {
                                Log.d(Constants.TAG, "get failed with ", task.getException());
                            }
                        }
                    });
        }

    }

    private void updateLikeNumber(FirebaseFirestore db, Room room) {
        db.collection("Room").document(mRoom.getStreamerId())
                .update("like", room.getLike())
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
    public void updateDislikeData(boolean hasChanged, boolean isAdded) {
        if (hasChanged) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("Room").document(mRoom.getStreamerId())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    Log.d(Constants.TAG, "DocumentSnapshot data: " + document.getData());
                                    Room room = document.toObject(Room.class);
                                    int dislikeNumber;
                                    if (isAdded) {
                                        dislikeNumber = room.getDislike() + 1;
                                    } else {
                                        dislikeNumber = room.getDislike() - 1;
                                    }
                                    room.setDislike(dislikeNumber);
                                    updateDislikeNumber(db, room);
                                } else {
                                    Log.d(Constants.TAG, "No such document");
                                }
                            } else {
                                Log.d(Constants.TAG, "get failed with ", task.getException());
                            }
                        }
                    });
        }
    }

    private void updateDislikeNumber(FirebaseFirestore db, Room room) {
        db.collection("Room").document(mRoom.getStreamerId())
                .update("dislike", room.getDislike())
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
