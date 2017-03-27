package com.lq.beauty.app.videoList;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import com.lq.beauty.app.camera.CameraHelper;
import com.lq.beauty.base.BaseApplication;
import com.lq.beauty.base.utils.FileUtil;

import java.io.File;

/**
 * Created by wuqingqing on 2017/3/27.
 */

public class VideoListConfig {
    public final static int VIDEO_THUMBNAIL_WIDTH = 100;
    public final static int VIDEO_THUMBNAIL_HEIGHT = 100;

    public final static String[] VIDEO_SEARCH_PATH_LIST = {
            "/DCIM/", "/ANDROID/", "/DOWNLOAD/", "/DOWNLOADS/", "/MOVIES/", "/MOVIE/", "VIDEO", "VIDEOS"
    };

    /**
     * 判断目录是否可以搜索
     * @param path
     * @param dirName
     * @return
     */
    public static boolean checkVideoPathCanSearch(String path, String dirName) {
        String sdRoot = FileUtil.getSDRoot();
        if (path.startsWith(sdRoot)) {
            path = path.substring(sdRoot.length());
        }
        if (!path.endsWith(File.separator)) {
            path += File.separator;
        }
        path = path.toUpperCase();
        for(String filter : VIDEO_SEARCH_PATH_LIST) {
            if (path.contains(filter)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 判断视频是不是我自己拍摄的
     * @param path
     * @return
     */
    public static boolean checkVideoMy(String path) {
        String videoPath = CameraHelper.getVideoPath();

        return (!TextUtils.isEmpty(path) && path.startsWith(videoPath));
    }
}
