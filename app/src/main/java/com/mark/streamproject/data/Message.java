package com.mark.streamproject.data;

public class Message {
    private String mName;
    private String mContent;
    private long mPublishTime;

    public Message() {
        mName = "";
        mContent = "";
        mPublishTime = -1;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        mContent = content;
    }

    public long getPublishTime() {
        return mPublishTime;
    }

    public void setPublishTime(long publishTime) {
        mPublishTime = publishTime;
    }
}
