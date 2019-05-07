package com.mark.streamproject.categeory;

import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mark.streamproject.data.Room;

import java.util.ArrayList;

import androidx.annotation.NonNull;

import static com.google.common.base.Preconditions.checkNotNull;

public class CategoryPresenter implements  CategoryContract.Presenter {

    private final CategoryContract.View mCategoryView;

    public CategoryPresenter(@NonNull CategoryContract.View categoryView) {
        mCategoryView = checkNotNull(categoryView, "hotsView cannot be null!");
        mCategoryView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void loadCategoryData() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Room")
                .orderBy("like", Query.Direction.DESCENDING)
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
                            setCategoryData(rooms);
                        } else {
                            Log.d("Firebase", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    @Override
    public void setCategoryData(ArrayList<Room> rooms) {
        mCategoryView.showCategoryUi(rooms);
    }

    @Override
    public void openRoom(Room room) {

    }

    @Override
    public boolean isCategoryRefreshing() {
        return mCategoryView.isRefreshing();
    }

    @Override
    public void searchRoomData(String string) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Room")
                .orderBy("streamerName")
                .startAt(string)
                .endAt(string + "\uf8ff")
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
                            setCategoryData(rooms);
                        } else {
                            Log.d("Firebase", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}
