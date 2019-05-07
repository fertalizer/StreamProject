package com.mark.streamproject.dialog;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.mark.streamproject.MainContract;
import com.mark.streamproject.R;
import com.mark.streamproject.data.User;

public class AlertDialog extends AppCompatDialogFragment implements View.OnClickListener {

    MainContract.Presenter mMainPresenter;

    User mUser;

    public AlertDialog() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setMainPresenter(MainContract.Presenter mainPresenter) {
        mMainPresenter = mainPresenter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.dialog_alert, container, false);
        Window window = getDialog().getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        root.findViewById(R.id.button_confirm).setOnClickListener(this);
        root.findViewById(R.id.button_deny).setOnClickListener(this);


        return root;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_confirm:
                mMainPresenter.removeStreamer(mUser);
                dismiss();
                break;
            case R.id.button_deny:
                dismiss();
                break;
            default:
        }
    }

    public void setUser(User user) {
        mUser = user;
    }
}
