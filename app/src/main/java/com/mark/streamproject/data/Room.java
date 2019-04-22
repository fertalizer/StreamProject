package com.mark.streamproject.data;

import com.google.api.client.util.DateTime;

public class Room {
//    private User mStreamer;
    private String mWatchId;
    private String mTitle;
    private String mTag;
    private String mImage;
    private long mPublishTime;
    private int mLike;
    private int mDislike;

    public Room() {
        mWatchId = "";
        mTitle = "";
        mTag = "";
        mImage = "";
        mPublishTime = -1;
        mLike = 0;
        mDislike = 0;
    }

    public String getWatchId() {
        return mWatchId;
    }

    public void setWatchId(String watchId) {
        mWatchId = watchId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getTag() {
        return mTag;
    }

    public void setTag(String tag) {
        mTag = tag;
    }

    public String getImage() {
        return mImage;
    }

    public void setImage(String image) {
        mImage = image;
    }

    public int getLike() {
        return mLike;
    }

    public void setLike(int like) {
        this.mLike = like;
    }

    public int getDislike() {
        return mDislike;
    }

    public void setDislike(int dislike) {
        mDislike = dislike;
    }

    public long getPublishTime() {
        return mPublishTime;
    }

    public void setPublishTime(long publishTime) {
        mPublishTime = publishTime;
    }
}
