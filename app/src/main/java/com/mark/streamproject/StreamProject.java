package com.mark.streamproject;

import android.app.Application;
import android.content.Context;

/**
 * Created by Wayne Chen on Feb. 2019.
 */
public class StreamProject extends Application {

    private static Context mContext;

    public StreamProject() {}

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    public static Context getAppContext() {
        return mContext;
    }

}