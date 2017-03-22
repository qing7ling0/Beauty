package com.lq.beauty.app.main.widget;

import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Property;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.Button;

import com.lq.beauty.R;
import com.lq.beauty.base.utils.Debug;
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
        mPaint.setStrokeWidth(2);
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
        WLog.d("RecordButton", "onMeasure w=" + mWidth + "; h=" + mHeight);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float r = mWidth / 2;
        float cx = mWidth / 2;
        float cy = mHeight / 2;

        mPaint.setColor(mCircleColor);
        canvas.drawCircle(mWidth/2, mHeight/2, mWidth/2, mPaint);
        if (mIsRecording) {
            r = r - UIHelper.dp2px(2);
            mPaint.setColor(mRingColor);
            canvas.drawCircle(cx, cy, r, mPaint);

            r = r - UIHelper.dp2px(5);
            mPaint.setColor(mCircleColor);
            mPaint.setAlpha((int) (255*(1-mRecordCountDown)));
            canvas.drawCircle(cx, cy, r, mPaint);

            mPaint.setColor(Color.BLACK);
            mPaint.setAlpha((int) (255*(mRecordCountDown)));
            canvas.drawCircle(cx, cy, r*mRecordCountDown, mPaint);
        } else {
            r = r - UIHelper.dp2px(2);
            mPaint.setColor(mRingColor);
            canvas.drawCircle(cx, cy, r, mPaint);

            r = r - UIHelper.dp2px(5);
            mPaint.setColor(mCircleColor);
            canvas.drawCircle(cx, cy, r, mPaint);
        }
    }

    /**
     * =============================================================================================
     * The Animate
     * =============================================================================================
     */
    private static final Interpolator ANIMATION_INTERPOLATOR = new DecelerateInterpolator();
    private static final int ANIMATION_DURATION = 3000;
    private ObjectAnimator mAnimator;

    public void animateCheckedState() {
        AnimatorProperty property = new AnimatorProperty();
        property.progress = 1;
        mIsRecording = true;
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
}
