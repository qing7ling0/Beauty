package com.lq.beauty.app.main.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.Button;

import com.lq.beauty.R;

import static com.lq.beauty.R.*;

/**
 * Created by wuqingqing on 2017/3/15.
 */

public class RecordButton extends android.support.v7.widget.AppCompatButton {
    private Paint mPaint;
    private float mAlpha;

    public RecordButton(Context context) {
        super(context);
    }

    public RecordButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        onInit(attrs);
    }

    public RecordButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        onInit(attrs);
    }

    protected void onInit(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, styleable.RecordButton);
        mAlpha = typedArray.getColor(styleable.RecordButton_alpha, 0);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

//    @Override
//    protected void onTouchEvent() {
//
//    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mPaint == null) {
            mPaint = new Paint();
            mPaint.setColor(0xff000000);
            mPaint.setAlpha((int) (255*mAlpha));
            mPaint.setStrokeWidth(2);
            mPaint.setAntiAlias(true);
        }
        canvas.drawCircle(0,0,100,mPaint);
    }
}
