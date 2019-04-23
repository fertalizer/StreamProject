package com.mark.streamproject.hots;

import android.app.AlertDialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mark.streamproject.R;
import com.mark.streamproject.StreamProject;
import com.mark.streamproject.data.Room;
import com.mark.streamproject.data.User;
import com.mark.streamproject.dialog.StreamDialog;

import java.util.ArrayList;


public class HotsFragment extends Fragment implements HotsContract.View, View.OnClickListener {
    private HotsContract.Presenter mPresenter;
    private HotsAdapter mHotsAdapter;

    public HotsFragment() {}

    public static HotsFragment newInstance() {
        return new HotsFragment();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHotsAdapter = new HotsAdapter(mPresenter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_hots, container, false);
        RecyclerView recyclerView = root.findViewById(R.id.recycler_hots);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(mHotsAdapter);


        root.findViewById(R.id.button_stream).setOnClickListener(this);
        root.findViewById(R.id.button_record).setOnClickListener(this);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.loadHotsData();
    }

    @Override
    public void setPresenter(HotsContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public boolean isActive() {
        return !isHidden();
    }

    @Override
    public void showHotsUI(ArrayList<Room> rooms) {
        mHotsAdapter.updateData(rooms);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_stream:
                mPresenter.showStreamDialog();
                break;
            case R.id.button_record:
                Toast.makeText(StreamProject.getAppContext(), "Coming Soon", Toast.LENGTH_SHORT).show();
                break;
            default:
        }
    }
}
