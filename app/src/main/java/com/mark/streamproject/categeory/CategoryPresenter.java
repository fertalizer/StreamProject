package com.mark.streamproject.categeory;

import static com.google.common.base.Preconditions.checkNotNull;

import android.util.Log;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mark.streamproject.data.Room;
import com.mark.streamproject.util.Constants;

import java.util.ArrayList;

public class CategoryPresenter implements  CategoryContract.Presenter {

    private final CategoryContract.View mCategoryView;

    public CategoryPresenter(@NonNull CategoryContract.View categoryView) {
        mCategoryView = checkNotNull(categoryView, "categoryView cannot be null!");
        mCategoryView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void loadCategoryData() {
        FirebaseFirestore.getInstance().collection(Constants.ROOM)
                .orderBy(Constants.LIKE, Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<Room> rooms = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.i(Constants.FIREBASE, document.getId() + " => " + document.getData());
                                rooms.add(document.toObject(Room.class));
                            }
                            setCategoryData(rooms);
                        } else {
                            Log.d(Constants.FIREBASE, "Error getting documents: ", task.getException());
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
        FirebaseFirestore.getInstance().collection(Constants.ROOM)
                .orderBy(Constants.STREAMER_NAME)
                .startAt(string)
                .endAt(string + "\uf8ff")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<Room> rooms = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.i(Constants.FIREBASE, document.getId() + " => " + document.getData());
                                rooms.add(document.toObject(Room.class));
                            }
                            setCategoryData(rooms);
                        } else {
                            Log.d(Constants.FIREBASE, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}
