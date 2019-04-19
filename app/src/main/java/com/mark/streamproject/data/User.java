package com.mark.streamproject.data;

public class User {
    private String mName;
    private String mImage;
    private String mId;
    private boolean mStreaming;

    public User() {
        mName = "";
        mImage = "";
        mId = "";
        mStreaming = false;
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

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }
}
