package com.mark.streamproject.room;

import android.graphics.Typeface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mark.streamproject.R;
import com.mark.streamproject.StreamProject;
import com.mark.streamproject.data.Room;
import com.mark.streamproject.util.Constants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

public class RoomFragment extends Fragment implements RoomContract.View {

    private RoomContract.Presenter mPresenter;

    private YouTubePlayerView mYouTubePlayerView;

    private Room mRoom;

    public RoomFragment() {

    }


    public static RoomFragment newInstance() {
        return new RoomFragment();
    }

    @Override
    public void setPresenter(RoomContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_room, container, false);
        TextView textView = root.findViewById(R.id.text_room_send);
        textView.setTypeface(Typeface.createFromAsset(StreamProject.getAppContext().getAssets(), "fonts/Minecraftia-Regular.ttf"));

        mYouTubePlayerView = root.findViewById(R.id.player);

        getLifecycle().addObserver(mYouTubePlayerView);
        mYouTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                String videoId = "QgjEF1EF8gw";
                youTubePlayer.loadVideo(videoId, 0);
            }
        });

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.hideProfileAndBottomNavigation();
    }

    @Override
    public void showRoomUi(Room room) {
        mRoom = room;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.showProfileAndBottomNavigation();
        mYouTubePlayerView.release();
        Log.d(Constants.TAG, "Destroyed");
    }

    @Override
    public boolean isActive() {
        return !isHidden();
    }
}
