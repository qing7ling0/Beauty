package com.lq.beauty.base.widget;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.ViewParent;

import com.lq.beauty.base.utils.ViewTransform;

/**
 * Created by wuqingqing on 2017/3/27.
 */

public class BaseRecyclerView extends RecyclerView {
    private int mScrollPointerId;
    private int mLastTouchY;
    private LayoutManager mLayout;
    private int mTouchSlop;
    private int mMaxHeight;
    private boolean mFosucInterceptTouchEvent;

    public BaseRecyclerView(Context context) {
        super(context);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public BaseRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public BaseRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public void setLayoutManager(LayoutManager layout) {
        super.setLayoutManager(layout);
        mLayout = layout;
    }

    public void setMaxHeight(int height) {
        mMaxHeight = height;
        if (mMaxHeight > 0) {
            int _h = getMeasuredHeight();
            _h = _h > mMaxHeight ? mMaxHeight : _h;
            ViewTransform.setViewHeight(this, _h);
        }
    }

    public boolean isFosucInterceptTouchEvent() {
        return mFosucInterceptTouchEvent;
    }

    public void setFosucInterceptTouchEvent(boolean fosucInterceptTouchEvent) {
        this.mFosucInterceptTouchEvent = fosucInterceptTouchEvent;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (mMaxHeight > 0) {
            int _h = h;
            _h = _h > mMaxHeight ? mMaxHeight : _h;
            ViewTransform.setViewHeight(this, _h);
        }
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        final int action = MotionEventCompat.getActionMasked(e);
        final int actionIndex = MotionEventCompat.getActionIndex(e);
        boolean intercept = false;
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mScrollPointerId = e.getPointerId(0);
                mLastTouchY = (int) e.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                final int index = e.findPointerIndex(mScrollPointerId);
                final int x = (int) (e.getX(index) + 0.5f);
                final int y = (int) (e.getY(index) + 0.5f);
                int dy = y - mLastTouchY;
                if (mLayout.canScrollVertically()) {
                    if ((dy < 0 && ViewCompat.canScrollVertically(this, 1)) || (dy > 0 && ViewCompat.canScrollVertically(this, -1))) {
                        intercept = true;
                    }
                }
                break;
        }
        if (mFosucInterceptTouchEvent || intercept) {
            final ViewParent p = getParent();
            if (null != p)
                p.requestDisallowInterceptTouchEvent(true);
        }
        return super.onInterceptTouchEvent(e) || mFosucInterceptTouchEvent;
    }
}
