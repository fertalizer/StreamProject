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

public class RoundAndStrokeTransformation implements Transformation {

    private int radius = 0;

    public RoundAndStrokeTransformation(int radius) {
        this.radius = radius;
    }

    @Override
    public Bitmap transform(Bitmap source) {

        Paint paint = new Paint();
        BitmapShader bitmapShader = new BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        paint.setShader(bitmapShader);
        paint.setAntiAlias(true);

        Paint borderPaint = new Paint();
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(4);
        borderPaint.setColor(StreamProject.getAppContext().getColor(R.color.yellow));
        borderPaint.setStrokeCap(Paint.Cap.ROUND);
        borderPaint.setAntiAlias(true);

        RectF rectF = new RectF(0, 0, source.getWidth(), source.getHeight());

        Bitmap bitmap = Bitmap.createBitmap(source.getWidth(), source.getHeight(), source.getConfig());
        Canvas canvas = new Canvas(bitmap);
        canvas.drawRoundRect(rectF, radius, radius, paint);
        canvas.drawRoundRect(2f, 2f, source.getWidth() - 2f, source.getHeight() - 2f, radius, radius, borderPaint);
        source.recycle();
        return bitmap;
    }

    @Override
    public String key() {
        return "roundCorner + stroke";
    }
}
