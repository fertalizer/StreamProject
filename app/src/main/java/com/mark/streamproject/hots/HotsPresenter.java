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

import java.util.ArrayList;

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
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Room")
                .orderBy("publishTime", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<Room> rooms = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.i("Firebase", document.getId() + " => " + document.getData());
                                rooms.add(document.toObject(Room.class));
                            }
                            setHotsData(rooms);
                        } else {
                            Log.d("Firebase", "Error getting documents: ", task.getException());
                        }
                    }
                });

    }

    @Override
    public void setHotsData(ArrayList<Room> rooms) {
        mHotsView.showHotsUI(rooms);
    }

    @Override
    public void openRoom(Room room) {

    }

}
