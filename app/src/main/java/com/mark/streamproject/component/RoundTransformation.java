package com.mark.streamproject.component;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;

import com.mark.streamproject.R;
import com.mark.streamproject.StreamProject;
import com.squareup.picasso.Transformation;

public class RoundTransformation implements Transformation {

    private int radius = 0;

    public RoundTransformation(int radius) {
        this.radius = radius;
    }

    @Override
    public Bitmap transform(Bitmap source) {
        Bitmap bitmap = Bitmap.createBitmap(source.getWidth(), source.getHeight(), source.getConfig());
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        BitmapShader bitmapShader = new BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        paint.setShader(bitmapShader);
        paint.setAntiAlias(true);

        RectF rectF = new RectF(0, 0, source.getWidth(), source.getHeight());
        canvas.drawRoundRect(rectF, radius, radius, paint);
        source.recycle();
        return bitmap;
    }

    @Override
    public String key() {
        return "roundCorner";
    }
}

