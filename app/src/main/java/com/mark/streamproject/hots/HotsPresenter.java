package com.mark.streamproject.hots;

import androidx.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mark.streamproject.data.Room;
import com.mark.streamproject.data.User;
import com.mark.streamproject.util.Constants;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import static com.google.common.base.Preconditions.checkNotNull;

public class HotsPresenter implements HotsContract.Presenter {

    private final HotsContract.View mHotsView;

    public HotsPresenter(@NonNull HotsContract.View hotsView) {
        mHotsView = checkNotNull(hotsView, "hotsView cannot be null!");
        mHotsView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void showStreamDialog() {

    }

    @Override
    public void loadHotsData() {
        getRoomData(new RoomCallback() {
            @Override
            public void onSuccess(ArrayList<Room> rooms, ArrayList<Integer> numbers) {
                setHotsData(rooms, numbers);
            }

            @Override
            public void onFail(String errorMessage) {

            }
        });
    }

    @Override
    public void setHotsData(ArrayList<Room> rooms, ArrayList<Integer> numbers) {
        mHotsView.showHotsUI(rooms, numbers);
    }

    @Override
    public void openRoom(Room room) {

    }

    @Override
    public boolean isDataRefreshing() {
        return mHotsView.isRefreshing();
    }


    public void getRoomData(RoomCallback roomCallback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Room")
                .orderBy("publishTime", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<Room> rooms = new ArrayList<>();
                            ArrayList<Integer> numbers = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.i("Firebase", document.getId() + " => " + document.getData());
                                rooms.add(document.toObject(Room.class));

                                getAudienceData(document.toObject(Room.class), new AudienceCallback() {
                                    @Override
                                    public void onSuccess(int number) {
                                        numbers.add(number);
                                    }

                                    @Override
                                    public void onFail(String errorMessage) {

                                    }
                                });

                            }

                            roomCallback.onSuccess(rooms, numbers);


                        } else {
                            Log.d("Firebase", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public void getAudienceData(Room room, AudienceCallback audienceCallback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Room").document(room.getStreamerId()).collection("Audience")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<User> users = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.i("Firebase", "For user size" + document.getId() + " => " + document.getData());
                                users.add(document.toObject(User.class));
                            }
                            Log.d(Constants.TAG, "User Size = " + users.size());
                            audienceCallback.onSuccess(users.size());

                        } else {
                            Log.d("Firebase", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public interface RoomCallback {
        void onSuccess(ArrayList<Room> rooms, ArrayList<Integer> numbers);

        void onFail(String errorMessage);
    }

    public interface AudienceCallback {
        void onSuccess(int number);

        void onFail(String errorMessage);
    }

}
