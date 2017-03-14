package com.lq.beauty.base.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;
import android.webkit.WebView;


import com.lq.beauty.base.BaseApplication;
import com.lq.beauty.base.ui.UIToast;

/**
 * Created by wuqingqing on 2017/3/7.
 */
public class DeviceInfo {

    public static DisplayMetrics getDisplayMetrics() {
        return BaseApplication.context().getResources().getDisplayMetrics();
    }

    /**
     * 获取设备高度
     * @return float
     */
    public static float getScreenHeight() {
        return getDisplayMetrics().heightPixels;
    }

    /**
     * 获取设备宽度
     * @return float
     */
    public static float getScreenWidth() {
        return getDisplayMetrics().widthPixels;
    }

    /**
     * 是否有网络
     * @return boolean
     */
    public static boolean hasNet() {
        ConnectivityManager cm = (ConnectivityManager) BaseApplication.context()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return info != null && info.isAvailable() && info.isConnected();
    }

    /**
     * wifi是否打开
     * @return boolean
     */
    public static boolean isWifiOpen() {
        ConnectivityManager cm = (ConnectivityManager) BaseApplication
                .context().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info == null) return false;
        if (!info.isAvailable() || !info.isConnected()) return false;
        if (info.getType() != ConnectivityManager.TYPE_WIFI) return false;
        return true;
    }

    /**
     * 是否纵向
     * @return boolean
     */
    public static boolean isPortrait() {
        return BaseApplication.context().getResources().getConfiguration()
                .orientation == Configuration.ORIENTATION_PORTRAIT;
    }

    /**
     * 平板电脑
     * @return boolean
     */
    public static boolean isPad() {
        int s = BaseApplication.context().getResources().getConfiguration().screenLayout;
        s &= Configuration.SCREENLAYOUT_SIZE_MASK;
        return s >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    /**
     * 是否有默认应用市场
     * @param context
     * @return boolean
     */
    public static boolean isHaveMarket(Context context) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_APP_MARKET);
        PackageManager pm = context.getPackageManager();

        return pm.queryIntentActivities(intent, 0).size() > 0;
    }

    public static int getVersionCode() {
        return getVersionCode(BaseApplication.context().getPackageName());
    }

    public static int getVersionCode(String packageName) {
        try {
            return BaseApplication.context()
                    .getPackageManager()
                    .getPackageInfo(packageName, 0)
                    .versionCode;
        } catch (PackageManager.NameNotFoundException ex) {
            return 0;
        }
    }

    public static String getVersionName() {
        try {
            return BaseApplication
                    .context()
                    .getPackageManager()
                    .getPackageInfo(BaseApplication.context().getPackageName(), 0)
                    .versionName;
        } catch (PackageManager.NameNotFoundException ex) {
            return "undefined version name";
        }
    }

    private static Boolean mHasWebView;

    /**
     * 是否有WebView
     * @param context
     * @return
     */
    public static boolean hasWebView(Context context) {
        if (mHasWebView != null) {
            if (!mHasWebView)
                UIToast.show(context, "Not WebView for you phone");
            return mHasWebView;
        }
        try {
            WebView webView = new WebView(context);
            webView.destroy();
            mHasWebView = true;
        } catch (Exception e) {
            e.printStackTrace();
            mHasWebView = false;
            UIToast.show(context, "Not WebView for you phone");
        }
        return mHasWebView;
    }


}
