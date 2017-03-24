package com.lq.beauty.app.camera;

import android.os.Environment;
import android.provider.ContactsContract;

import com.lq.beauty.app.AppConstants;
import com.lq.beauty.base.BaseApplication;
import com.lq.beauty.base.utils.FileUtil;
import com.lq.beauty.base.utils.StringUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by wuqingqing on 2017/3/24.
 */

public class CameraHelper {
    public final static String VIDEO_FORMAT = ".mp4";

    private final static ThreadLocal<SimpleDateFormat> YYYYMMDDHHMMSS = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
        }
    };

    /**
     * temp : x/xx/video/
     * @return
     */
    public static String getVideoPath() {
        String path = BaseApplication.context().getExternalFilesDir(Environment.DIRECTORY_MOVIES).getAbsolutePath();
        if (!path.endsWith(File.separator))
            path = path + File.separator;

        return path;
    }

    public static String createVideoFilePath() {
        return getVideoPath() + AppConstants.APP_TAG + YYYYMMDDHHMMSS.get().format(new Date()) + VIDEO_FORMAT;
    }
}
