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
import android.widget.ImageView;
import android.widget.TextView;

import com.mark.streamproject.R;
import com.mark.streamproject.StreamProject;
import com.mark.streamproject.data.Room;
import com.mark.streamproject.util.Constants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.squareup.picasso.Picasso;

public class RoomFragment extends Fragment implements RoomContract.View {

    private RoomContract.Presenter mPresenter;

    private YouTubePlayerView mYouTubePlayerView;
    private TextView mTextTitle;
    private TextView mTextTag;
    private TextView mTextStreamerName;
    private TextView mTextLike;
    private TextView mTextDislike;
    private ImageView mImageStreamer;



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

        init(root);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.hideProfileAndBottomNavigation();
        mPresenter.loadRoomData();

        getLifecycle().addObserver(mYouTubePlayerView);
        mYouTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                String videoId = mRoom.getWatchId();
                youTubePlayer.loadVideo(videoId, 0);
            }
        });

        mTextTitle.setText(mRoom.getTitle());
        mTextStreamerName.setText(mRoom.getStreamerName());
        String like = Integer.toString(mRoom.getLike());
        mTextLike.setText(like);
        String dislike = Integer.toString(mRoom.getDislike());
        mTextDislike.setText(dislike);

        if (!mRoom.getStreamerImage().equals("")) {
            Picasso.get()
                    .load(mRoom.getStreamerImage())
                    .resize(30, 30)
                    .centerCrop()
                    .into(mImageStreamer);
        }

    }

    private void init(View view) {
        mYouTubePlayerView = view.findViewById(R.id.player);
        mTextTitle = view.findViewById(R.id.text_room_title);
        mTextTag = view.findViewById(R.id.text_room_tag);
        mTextStreamerName = view.findViewById(R.id.text_room_name);
        mTextLike = view.findViewById(R.id.text_room_like);
        mTextDislike = view.findViewById(R.id.text_rooms_dislike);
        mImageStreamer = view.findViewById(R.id.image_room_user);
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
        mPresenter.exitRoom();
        mPresenter.showProfileAndBottomNavigation();
        mYouTubePlayerView.release();
        Log.d(Constants.TAG, "Destroyed");
    }

    @Override
    public boolean isActive() {
        return !isHidden();
    }
}
