package com.lq.beauty.base.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * 正方形布局
 *
 * Created by wuqingqing on 2017/3/15.
 */

public class SquareRelativeLayout extends RelativeLayout {

    // 是否适应宽度
    protected boolean mAdapterWithWidth;
    protected OnSizeChangeListener onSizeChangeListener;

    public OnSizeChangeListener getOnSizeChangeListener() {
        return onSizeChangeListener;
    }

    public void setOnSizeChangeListener(OnSizeChangeListener onSizeChangeListener) {
        this.onSizeChangeListener = onSizeChangeListener;
    }

    public SquareRelativeLayout(Context context) {
        super(context);
        setAdapterWithWidth(true);
    }

    public SquareRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setAdapterWithWidth(true);
    }

    public SquareRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setAdapterWithWidth(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int r = heightMeasureSpec;
        if (isAdapterWithWidth())
            r = widthMeasureSpec;
        super.onMeasure(r, r);
        if (onSizeChangeListener != null) onSizeChangeListener.onSizeChanged(r, r);
    }

    public void setAdapterWithWidth(boolean adapterWithWidth) {
        mAdapterWithWidth = adapterWithWidth;
    }

    public boolean isAdapterWithWidth() {
        return mAdapterWithWidth;
    }

    public interface OnSizeChangeListener {
        void onSizeChanged(int width, int height);
    }
}
