package com.lq.beauty.base.utils;

import android.view.View;
import android.view.ViewGroup;

/**
 * View变换操作
 *
 * Created by wuqingqing on 2017/3/7.
 */
public class ViewTransform {

    /**
     * 设置View高度
     * @param view
     * @param aimHeight
     * @return
     */
    public static boolean setViewHeight(final View view, final int aimHeight) {
        if (view.isInEditMode()) {
            return false;
        }

        if (view.getHeight() == aimHeight) {
            return false;
        }

        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams == null) {
            layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    aimHeight);
            view.setLayoutParams(layoutParams);
        } else {
            layoutParams.height = aimHeight;
            view.requestLayout();
        }

        return true;
    }

    /**
     * 设置View宽度
     * @param view
     * @param width
     * @return
     */
    public static boolean setViewWidth(final View view, final int width) {
        if (view.isInEditMode())
            return false;

        if (view.getWidth() == width)
            return false;

        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams == null) {
            layoutParams = new ViewGroup.LayoutParams(width, ViewGroup.LayoutParams.MATCH_PARENT);
            view.setLayoutParams(layoutParams);
        } else {
            layoutParams.width = width;
            view.requestLayout();
        }

        return true;
    }

    /**
     * 设置View宽度高度
     * @param view
     * @param width
     * @return
     */
    public static boolean setViewWidthHeight(final View view, final int width, final int height) {
        if (view.isInEditMode())
            return false;

        if (view.getWidth() == width && view.getHeight() == height)
            return false;

        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams == null) {
            layoutParams = new ViewGroup.LayoutParams(width, height);
            view.setLayoutParams(layoutParams);
        } else {
            layoutParams.width = width;
            layoutParams.height = height;
            view.requestLayout();
        }

        return true;
    }
}
