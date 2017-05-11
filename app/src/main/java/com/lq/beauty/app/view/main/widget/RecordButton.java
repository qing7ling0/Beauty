package com.lq.beauty.app.view.main.widget;

import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Property;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

import com.lq.beauty.app.RxBus.RxBus;
import com.lq.beauty.app.RxBus.RxBusEvent;
import com.lq.beauty.base.utils.UIHelper;
import com.lq.beauty.base.utils.WLog;

import static com.lq.beauty.R.*;

/**
 * Created by wuqingqing on 2017/3/15.
 */

public class RecordButton extends android.support.v7.widget.AppCompatButton {

    private Paint mPaint;
    private float mAlpha;
    private int mWidth;
    private int mHeight;
    private int mRingColor;
    private int mCircleColor;

    private float mRecordCountDown;
    private boolean mIsRecording;

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
        mAlpha = typedArray.getFloat(styleable.RecordButton_alpha, 0);

        mRingColor = Color.WHITE;
        mCircleColor = getContext().getResources().getColor(color.colorBg);

        mPaint = new Paint();
        mPaint.setColor(0xff000000);
        mPaint.setAlpha((int) (255*mAlpha));
        mPaint.setAntiAlias(true);
        setRecored(false);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
        WLog.d("RecordButton", "onMeasure w=" + mWidth + "; h=" + mHeight);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        WLog.d("RecordButton", "onTouchEvent w=" + event.getX() + "; h=" + event.getY());
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                start();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                stop();
                break;
            case MotionEvent.ACTION_CANCEL:
                stop();
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float r = mWidth / 2;
        float cx = mWidth / 2;
        float cy = mHeight / 2;
        if (mIsRecording) {
            mPaint.setColor(Color.GRAY);
            canvas.drawCircle(cx, cy, r, mPaint);

            RectF rect= new RectF(cx-r, cy-r, cx+r, cy+r);
            Path path = new Path();
            path.moveTo(cx, cy);
            path.arcTo(rect, -90, 360*mRecordCountDown, false);
            canvas.save();
            canvas.clipPath(path);
            mPaint.setAlpha(255);
            mPaint.setColor(mCircleColor);
            canvas.drawCircle(cx, cy, r, mPaint);
            canvas.restore();

            r = r - UIHelper.dp2px(2);
            mPaint.setColor(Color.WHITE);
            mPaint.setAlpha(255);
            canvas.drawCircle(cx, cy, r, mPaint);

            mPaint.setColor(Color.BLACK);
            mPaint.setTextAlign(Paint.Align.CENTER);
            mPaint.setTextSize(UIHelper.dp2px(20));
        } else {
            mPaint.setAlpha(255);
            mPaint.setColor(mCircleColor);
            canvas.drawCircle(cx, cy, r, mPaint);

            r = r - UIHelper.dp2px(2);
            mPaint.setColor(mRingColor);
            canvas.drawCircle(cx, cy, r, mPaint);

            r = r - UIHelper.dp2px(5);
            mPaint.setColor(mCircleColor);
            canvas.drawCircle(cx, cy, r, mPaint);
        }
    }

    protected void setRecored(boolean recored) {
        mIsRecording = recored;
    }

    protected void setRecoredSendEvent(boolean recored) {
        setRecored(recored);
        RxBus.getInstance().post(new RxBusEvent(recored));
    }

    /**
     * =============================================================================================
     * The Animate
     * =============================================================================================
     */
    private static final Interpolator ANIMATION_INTERPOLATOR = new DecelerateInterpolator();
    private static final int ANIMATION_DURATION = 10000;
    private ObjectAnimator mAnimator;

    public void start() {
        setRecoredSendEvent(true);
        AnimatorProperty property = new AnimatorProperty();
        property.progress = 1;
        if (mAnimator == null) {
            mAnimator = ObjectAnimator.ofObject(this, ANIM_VALUE, new AnimatorEvaluator(new AnimatorProperty()), property);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2)

                mAnimator.setAutoCancel(true);
            mAnimator.setDuration(ANIMATION_DURATION);
            mAnimator.setInterpolator(ANIMATION_INTERPOLATOR);
        } else {
            mAnimator.cancel();
            mAnimator.setObjectValues(property);
        }
        mAnimator.start();
    }

    public void stop() {
        if (mAnimator != null) {
            mAnimator.cancel();
        }
        setRecoredSendEvent(false);
    }

    /**
     * =============================================================================================
     * The custom properties
     * =============================================================================================
     */

    private AnimatorProperty mCurProperty = new AnimatorProperty();

    private void setAnimatorProperty(AnimatorProperty property) {
        this.mRecordCountDown = property.progress;
        invalidate();
    }

    private void setAnimatorProperty() {
        AnimatorProperty property = mCurProperty;
        property.progress = mRecordCountDown;
        setAnimatorProperty(property);
    }

    private final static class AnimatorProperty {
        private float progress;
    }

    private final static class AnimatorEvaluator implements TypeEvaluator<AnimatorProperty> {
        private final AnimatorProperty mProperty;

        public AnimatorEvaluator(AnimatorProperty property) {
            mProperty = property;
        }

        @Override
        public AnimatorProperty evaluate(float fraction, AnimatorProperty startValue, AnimatorProperty endValue) {
            // Values
            mProperty.progress = startValue.progress + (endValue.progress - startValue.progress) * fraction;

            return mProperty;
        }
    }

    private final static Property<RecordButton, AnimatorProperty> ANIM_VALUE = new Property<RecordButton, AnimatorProperty>(AnimatorProperty.class, "animValue") {
        @Override
        public AnimatorProperty get(RecordButton object) {
            return object.mCurProperty;
        }

        @Override
        public void set(RecordButton object, AnimatorProperty value) {
            object.setAnimatorProperty(value);
        }
    };

    public class RxBusEvent extends com.lq.beauty.app.RxBus.RxBusEvent {
        public boolean recording;

        public RxBusEvent(boolean recording) {
            super(0);
            this.recording = recording;
        }
    }
}
