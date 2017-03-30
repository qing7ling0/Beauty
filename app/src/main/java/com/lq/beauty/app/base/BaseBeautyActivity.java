package com.lq.beauty.app.base;

import android.os.Build;
import android.support.v4.view.LayoutInflaterCompat;
import android.view.Window;
import android.view.WindowManager;

import com.lq.beauty.base.activity.BaseActivity;
import com.mikepenz.iconics.context.IconicsLayoutInflater;

/**
 * Created by wuqingqing on 2017/3/25.
 */

public abstract class BaseBeautyActivity extends BaseActivity {

    @Override
    protected void initWindow() {

//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            Window window = getWindow();
//            // Translucent status bar
//            window.setFlags(
//                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
//                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        }
    }

    @Override
    protected void onLayoutInflater() {
        LayoutInflaterCompat.setFactory(getLayoutInflater(), new IconicsLayoutInflater(getDelegate()));
    }
}
