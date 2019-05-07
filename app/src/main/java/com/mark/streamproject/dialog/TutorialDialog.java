package com.mark.streamproject.dialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;
import ru.tinkoff.scrollingpagerindicator.ScrollingPagerIndicator;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.mark.streamproject.MainContract;
import com.mark.streamproject.R;
import com.mark.streamproject.StreamProject;


public class TutorialDialog extends AppCompatDialogFragment implements View.OnClickListener {

    MainContract.Presenter mMainPresenter;

    TutorialAdapter mDescriptionAdapter;

    public TutorialDialog() {

    }

    public void setMainPresenter(MainContract.Presenter mainPresenter) {
        mMainPresenter = mainPresenter;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDescriptionAdapter = new TutorialAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.dialog_tutorial, container, false);
        Window window = getDialog().getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        RecyclerView recyclerView = root.findViewById(R.id.recycler_description);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(StreamProject.getAppContext(),
                LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mDescriptionAdapter);

        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);

        ScrollingPagerIndicator recyclerIndicator = root.findViewById(R.id.indicator);
        recyclerIndicator.attachToRecyclerView(recyclerView);

        root.findViewById(R.id.button_description_cancel).setOnClickListener(this);

        return root;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_description_cancel:
                dismiss();
                break;
            default:
        }
    }
}
