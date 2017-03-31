package com.lq.beauty.app;

import com.lq.beauty.base.BaseApplication;

/**
 * Created by wuqingqing on 2017/3/16.
 */

public class BeautyApplication extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        SettingManager.getInstance().read();
    }
}
