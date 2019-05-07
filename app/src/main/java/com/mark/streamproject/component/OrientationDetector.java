package com.mark.streamproject.component;

import android.content.Context;
import android.view.OrientationEventListener;

public class OrientationDetector extends OrientationEventListener {

    private OrientationEventCallback mCallback;

    public OrientationDetector(Context context, OrientationEventCallback callback) {
        super(context);
        mCallback = callback;
    }

    @Override
    public void onOrientationChanged(int orientation) {
        if (orientation == OrientationEventListener.ORIENTATION_UNKNOWN) {
            return;
        }

        if (orientation > 350 || orientation < 10) { // orientation = 0;

            mCallback.onPortrait();
        } else if (orientation > 80 && orientation < 100) { // orientation = 90;

            mCallback.onLandscape();
        } else if (orientation > 170 && orientation < 190) { // orientation = 180;

            mCallback.onPortrait();
        } else if (orientation > 260 && orientation < 280) { //270 orientation = 270;

            mCallback.onLandscape();
        } else {
            return;
        }
    }
}
