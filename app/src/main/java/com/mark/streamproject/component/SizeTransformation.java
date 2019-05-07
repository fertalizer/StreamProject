package com.mark.streamproject.component;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.squareup.picasso.Transformation;

public class SizeTransformation implements Transformation {

    private ImageView mImageView;

    public SizeTransformation(ImageView imageView) {
        mImageView = imageView;
    }

    @Override
    public Bitmap transform(Bitmap source) {
        int targetWidth = mImageView.getWidth();

        if (source.getWidth() == 0) {
            return source;
        }


        if (source.getWidth() < targetWidth) {
            return source;
        } else {
            double aspectRatio = (double) source.getHeight() / (double) source.getWidth();
            int targetHeight = (int) (targetWidth * aspectRatio);
            if (targetHeight != 0 && targetWidth != 0) {
                Bitmap result = Bitmap.createScaledBitmap(source, targetWidth, targetHeight, false);
                if (result != source) {
                    // Same bitmap is returned if sizes are the same
                    source.recycle();
                }
                return result;
            } else {
                return source;
            }
        }
    }

    @Override
    public String key() {
        return "size";
    }
}
