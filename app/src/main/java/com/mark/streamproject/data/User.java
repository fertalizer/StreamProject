package com.mark.streamproject.data;

public class User {
    private String mName;
    private String mImage;
    private String mId;
    private boolean mStreaming;
    private String mWatchId;

    public User() {
        mName = "";
        mImage = "";
        mId = "";
        mStreaming = false;
        mWatchId = "";
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

    public boolean isStreaming() {
        return mStreaming;
    }

    public void setStreaming(boolean streaming) {
        mStreaming = streaming;
    }

    public String getWatchId() {
        return mWatchId;
    }

    public void setWatchId(String watchId) {
        mWatchId = watchId;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }
}
