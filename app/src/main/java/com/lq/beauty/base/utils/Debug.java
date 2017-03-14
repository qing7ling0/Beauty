package com.lq.beauty.base.utils;

import android.util.Log;

/**
 * Created by wuqinqing on 2017/3/6.
 */

public class Debug {

    public static String APP_TAG = "Look Look";

    public static void e(String tag, String msg) {
        Log.e((tag!=null && tag.length()>0) ? tag : APP_TAG, msg);
    }

    public static void w(String tag, String msg) {
        Log.w((tag!=null && tag.length()>0) ? tag : APP_TAG, msg);
    }

    public static void d(String tag, String msg) {
        Log.d((tag!=null && tag.length()>0) ? tag : APP_TAG, msg);
    }

    public static void i(String tag, String msg) {
        Log.i((tag!=null && tag.length()>0) ? tag : APP_TAG, msg);
    }

    public static void v(String tag, String msg) {
        Log.v((tag!=null && tag.length()>0) ? tag : APP_TAG, msg);
    }
}
