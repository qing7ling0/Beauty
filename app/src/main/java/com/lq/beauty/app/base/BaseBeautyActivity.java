package com.lq.beauty.app.base;

import android.os.Build;
import android.view.Window;
import android.view.WindowManager;

import com.lq.beauty.base.activity.BaseActivity;

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
}
