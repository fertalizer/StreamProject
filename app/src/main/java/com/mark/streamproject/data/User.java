package com.mark.streamproject.data;

import com.mark.streamproject.util.Constants;

import java.util.ArrayList;

public class User {
    private String mName;
    private String mImage;
    private String mId;
    private int mStatus;
    private ArrayList<String> mLikeList;
    private ArrayList<String> mDislikeList;
    private ArrayList<String> mFollowList;

    public User() {
        mName = "";
        mImage = "";
        mId = "";
        mStatus = 0;
        mLikeList = new ArrayList<>();
        mDislikeList = new ArrayList<>();
        mFollowList = new ArrayList<>();
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getImage() {
        return mImage;
    }

    public void setImage(String image) {
        mImage = image;
    }

    public int getStatus() {
        return mStatus;
    }

    public void setStatus(int status) {
        mStatus = status;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public ArrayList<String> getLikeList() {
        return mLikeList;
    }

    public void setLikeList(ArrayList<String> likeList) {
        mLikeList = likeList;
    }

    public ArrayList<String> getDislikeList() {
        return mDislikeList;
    }

    public void setDislikeList(ArrayList<String> dislikeList) {
        mDislikeList = dislikeList;
    }

    public ArrayList<String> getFollowList() {
        return mFollowList;
    }

    public void setFollowList(ArrayList<String> followList) {
        mFollowList = followList;
    }
}
