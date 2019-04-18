package com.mark.streamproject.hots;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
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
        db.collection("User")
                .whereEqualTo("streaming", true)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<User> users = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.i("Firebase", document.getId() + " => " + document.getData());
                                users.add(document.toObject(User.class));
                            }
                            setHotsData(users);
                        } else {
                            Log.d("Firebase", "Error getting documents: ", task.getException());
                        }
                    }
                });

    }

    @Override
    public void setHotsData(ArrayList<User> users) {
        mHotsView.showHotsUI(users);
    }

    @Override
    public void openRoom() {

    }
}
