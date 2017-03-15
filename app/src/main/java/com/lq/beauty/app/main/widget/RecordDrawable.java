package com.lq.beauty.app.main.widget;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;


import static android.R.attr.drawable;

/**
 * Created by wuqingqing on 2017/3/15.
 */

public class RecordDrawable extends Drawable {
    Paint mPaint = null;
    @Override
    public void draw(@NonNull Canvas canvas) {
        if (mPaint == null) {
            mPaint = new Paint();
            mPaint.setColor(0xff000000);
            mPaint.setStrokeWidth(2);
            mPaint.setAntiAlias(true);
        }
        canvas.drawCircle(0,0,100,mPaint);
    }

    @Override
    public void setAlpha(@IntRange(from = 0, to = 255) int alpha) {

    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return PixelFormat.OPAQUE;
    }
}
