package com.mark.streamproject.room;

import android.util.Log;

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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static com.google.common.base.Preconditions.checkNotNull;

public class RoomPresenter implements RoomContract.Presenter{

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
    public void sendMessage(String text) {
        Message message = new Message();
        message.setName(UserManager.getInstance().getUser().getName());
        message.setPublishTime(System.currentTimeMillis());
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
}
