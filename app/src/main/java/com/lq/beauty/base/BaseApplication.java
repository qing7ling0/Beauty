package com.lq.beauty.base;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;


@SuppressLint("InflateParams")
public class BaseApplication extends Application {
    private static Context _context;

    @Override
    public void onCreate() {
        super.onCreate();
        _context = getApplicationContext();
    }

    public static synchronized BaseApplication context() {
        return (BaseApplication) _context;
    }

}
