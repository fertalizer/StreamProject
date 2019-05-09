package com.mark.streamproject.room;

import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.mark.streamproject.R;
import com.mark.streamproject.StreamProject;
import com.mark.streamproject.data.Message;
import com.mark.streamproject.data.Room;
import com.mark.streamproject.data.User;
import com.mark.streamproject.util.Constants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerTracker;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import org.jetbrains.annotations.NotNull;

public class RoomFragment extends Fragment implements RoomContract.View, View.OnClickListener {

    private RoomContract.Presenter mPresenter;

    private RoomAdapter mRoomAdapter;

    private YouTubePlayerView mYouTubePlayerView;
    private TextView mTextTitle;
    private TextView mTextTag;
    private TextView mTextStreamerName;
    private TextView mTextLike;
    private TextView mTextDislike;
    private TextView mAudienceNumber;
    private ImageView mImageStreamer;
    private EditText mMessage;
    private TextView mButtonSend;
    private RecyclerView mRecyclerView;
    private ImageView mImageLike;
    private ImageView mImageDislike;
    private ImageView mImageFollow;

    private Room mRoom;

    private boolean isAddedLike;
    private boolean isAddedDisLike;
    private boolean hasFollowed;
    private boolean hasChangedLike;
    private boolean hasChangedDislike;

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
        mRoomAdapter = new RoomAdapter(mPresenter);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_room, container, false);
        TextView textView = root.findViewById(R.id.text_room_send);
        textView.setTypeface(Typeface.createFromAsset(StreamProject.getAppContext().getAssets(), "fonts/Minecraftia-Regular.ttf"));

        init(root);

        mRecyclerView = root.findViewById(R.id.recycler_room);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mRoomAdapter);


        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.hideProfileAndBottomNavigation();
        mPresenter.loadRoomData();
        mPresenter.loadMessageData();
        mPresenter.getRoomAudienceNumber();

        mButtonSend.setOnClickListener(this);
        mImageLike.setOnClickListener(this);
        mImageDislike.setOnClickListener(this);
        mImageFollow.setOnClickListener(this);

        mPresenter.inLikeList();
        mPresenter.inDislikeList();
        mPresenter.inFollowList();

        if (getResources().getConfiguration().orientation == 2) {
            mYouTubePlayerView.enterFullScreen();
        }

        Log.d(Constants.TAG, "hasChangedLike: " + hasChangedLike);
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) { // Landscape
            mYouTubePlayerView.enterFullScreen();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) { // Portrait
            mYouTubePlayerView.exitFullScreen();
        }
    }

    private void init(View view) {
        mYouTubePlayerView = view.findViewById(R.id.player);
        mTextTitle = view.findViewById(R.id.text_room_title);
        mTextTag = view.findViewById(R.id.text_room_tag);
        mTextStreamerName = view.findViewById(R.id.text_room_name);
        mTextLike = view.findViewById(R.id.text_hots_like);
        mTextDislike = view.findViewById(R.id.text_hots_dislike);
        mAudienceNumber = view.findViewById(R.id.text_room_audience_number);
        mImageStreamer = view.findViewById(R.id.image_room_user);
        mMessage = view.findViewById(R.id.edit_room_message);
        mButtonSend = view.findViewById(R.id.text_room_send);
        mImageLike = view.findViewById(R.id.image_room_like);
        mImageDislike = view.findViewById(R.id.image_room_dislike);
        mImageFollow = view.findViewById(R.id.image_room_follow);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_room_send:
                String text = mMessage.getText().toString().trim();
                long time = System.currentTimeMillis();
                mPresenter.sendMessage(text, time);
                mMessage.setText("");
                break;
            case R.id.image_room_like:
                if (!isAddedLike) {
                    isAddedLike = true;
                    add2LikeList();
                    likeStatusChange();
                } else {
                    isAddedLike = false;
                    removeFromLikeList();
                    likeStatusChange();
                }
                break;
            case R.id.image_room_dislike:
                if (!isAddedDisLike) {
                    isAddedDisLike = true;
                    add2DislikeList();
                    dislikeStatusChange();
                } else {
                    isAddedDisLike = false;
                    removeFromDislikeList();
                    dislikeStatusChange();
                }
                break;
            case R.id.image_room_follow:
                if (!hasFollowed) {
                    hasFollowed = true;
                    add2FollowList();
                } else {
                    hasFollowed = false;
                    removeFromFollowList();
                }
                break;
            default:
        }

    }

    @Override
    public void showRoomUi(Room room) {
        mRoom = room;

        getLifecycle().addObserver(mYouTubePlayerView);
        mYouTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                YouTubePlayerTracker tracker = new YouTubePlayerTracker();
                youTubePlayer.addListener(tracker);
                String videoId = mRoom.getWatchId();
                youTubePlayer.loadVideo(videoId, tracker.getCurrentSecond());
                youTubePlayer.seekTo(tracker.getCurrentSecond());
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
                    .fit()
                    .centerCrop()
                    .into(mImageStreamer);
        }


        mMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if ("".equals(s.toString())) {
                    mButtonSend.setClickable(false);
                    mButtonSend.setTextColor(getResources().getColor(R.color.gray));
                } else {
                    mButtonSend.setTextColor(getResources().getColor(R.color.yellow));
                    mButtonSend.setClickable(true);
                }
            }
        });
    }

    @Override
    public void showMessageUi(ArrayList<Message> messages) {
        mRoomAdapter.updateData(messages);
        mRecyclerView.scrollToPosition(mRoomAdapter.getItemCount() - 1);
    }

    @Override
    public void showAudienceUi(ArrayList<User> users) {
        String audienceNumber = Integer.toString(users.size());
        mAudienceNumber.setText(audienceNumber);
    }

    @Override
    public void onStart() {
        super.onStart();
        mPresenter.enterRoom();
    }

    @Override
    public void onStop() {
        super.onStop();
        mPresenter.exitRoom();
        mPresenter.updateUserData();
        mPresenter.updateLikeData(hasChangedLike, isAddedLike);
        mPresenter.updateDislikeData(hasChangedDislike, isAddedDisLike);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.refreshHotsData();

        mPresenter.showProfileAndBottomNavigation();
        mYouTubePlayerView.release();
        Log.d(Constants.TAG, "Room Destroyed");

    }

    @Override
    public boolean isActive() {
        return !isHidden();
    }

    private void add2LikeList() {
        mImageLike.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.yellow)));
        int likeNumber = mRoom.getLike() + 1;
        mRoom.setLike(likeNumber);
        String like = Integer.toString(mRoom.getLike());
        mTextLike.setTextColor(getResources().getColor(R.color.yellow));
        mTextLike.setText(like);
        mImageDislike.setClickable(false);
        mPresenter.add2LikeList();
    }

    private void removeFromLikeList() {
        mImageLike.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.gray)));
        int likeNumber = mRoom.getLike() - 1;
        mRoom.setLike(likeNumber);
        String like = Integer.toString(mRoom.getLike());
        mTextLike.setTextColor(getResources().getColor(R.color.gray));
        mTextLike.setText(like);
        mImageDislike.setClickable(true);
        mPresenter.removeFromLikeList();
    }

    private void add2DislikeList() {
        mImageDislike.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.yellow)));
        int dislikeNumber = mRoom.getDislike() + 1;
        mRoom.setDislike(dislikeNumber);
        String disLike = Integer.toString(mRoom.getDislike());
        mTextDislike.setTextColor(getResources().getColor(R.color.yellow));
        mTextDislike.setText(disLike);
        mImageLike.setClickable(false);
        mPresenter.add2DislikeList();
    }

    private void removeFromDislikeList() {
        mImageDislike.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.gray)));
        int dislikeNumber = mRoom.getDislike() - 1;
        mRoom.setDislike(dislikeNumber);
        String disLike = Integer.toString(mRoom.getDislike());
        mTextDislike.setTextColor(getResources().getColor(R.color.gray));
        mTextDislike.setText(disLike);
        mImageLike.setClickable(true);
        mPresenter.removeFromDisLikeList();
    }

    private void add2FollowList() {
        mImageFollow.setBackground(getResources().getDrawable(R.drawable.star_select));
        mImageFollow.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.yellow)));
        mPresenter.add2FollowList();
    }

    private void removeFromFollowList() {
        mImageFollow.setBackground(getResources().getDrawable(R.drawable.star));
        mImageFollow.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.yellow)));
        mPresenter.removeFromFollowList();
    }

    @Override
    public void inLikeListUi() {
        isAddedLike = true;
        mTextLike.setTextColor(getResources().getColor(R.color.yellow));
        mImageLike.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.yellow)));
        mImageDislike.setClickable(false);
    }

    @Override
    public void inDislikeListUi() {
        isAddedDisLike = true;
        mTextDislike.setTextColor(getResources().getColor(R.color.yellow));
        mImageDislike.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.yellow)));
        mImageLike.setClickable(false);
    }

    @Override
    public void inFollowListUi() {
        hasFollowed = true;
        mImageFollow.setBackground(getResources().getDrawable(R.drawable.star_select));
        mImageFollow.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.yellow)));
    }

    private void likeStatusChange() {
        if (!hasChangedLike) {
            hasChangedLike = true;
        } else {
            hasChangedLike = false;
        }
        Log.d(Constants.TAG, "hasChangedLike: " + hasChangedLike);
    }

    private void dislikeStatusChange() {
        if (!hasChangedDislike) {
            hasChangedDislike = true;
        } else {
            hasChangedDislike = false;
        }
        Log.d(Constants.TAG, "hasChangedDislike: " + hasChangedDislike);
    }
}
