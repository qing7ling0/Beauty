package com.lq.beauty.app;

import com.lq.beauty.R;
import com.lq.beauty.base.BaseApplication;
import com.lq.beauty.base.cache.CacheItem;
import com.lq.beauty.base.cache.CacheManager;
import com.lq.beauty.base.utils.StringUtils;
import com.lq.beauty.base.utils.UIHelper;

import java.io.Serializable;
import java.text.DecimalFormat;

/**
 * Created by wuqingqing on 2017/3/30.
 */

public class SettingManager implements Serializable {

    public final static int[] ARR_FILTER_TEXTS                 = {
            R.string.filter1,
            R.string.filter2,
            R.string.filter3,
            R.string.filter4};

    public final static int[] ARR_VIDEO_CAMERA_RATIO_TEXTS     = {
            R.string.cameraRatioSquare,
            R.string.cameraRatio16_9,
            R.string.cameraRatio21_9,
            R.string.cameraRatioCircle,
            R.string.cameraRatio9_16};

    public final static int VIDEO_CAMERA_RATIO_SQUARE           = 0;
    public final static int VIDEO_CAMERA_RATIO_16_9             = 1;
    public final static int VIDEO_CAMERA_RATIO_21_9             = 2;
    public final static int VIDEO_CAMERA_RATIO_CIRCLE           = 3;
    public final static int VIDEO_CAMERA_RATIO_9_16             = 4;

    public final static float[] ARR_VIDEO_TIME                  = {1.5f, 3.0f, 6.0f, 9.0f, 12.0f};

    public final static int VIDEO_TIME_1_5                      = 0;
    public final static int VIDEO_TIME_3                        = 1;
    public final static int VIDEO_TIME_6                        = 2;
    public final static int VIDEO_TIME_9                        = 3;
    public final static int VIDEO_TIME_12                       = 4;


    public final static int SETTING_ID_DEFAULT_FILTER           = 0;
    public final static int SETTING_ID_DEFAULT_CAMERA_RATIO     = 1;
    public final static int SETTING_ID_DEFAULT_VIDEO_TIME       = 2;
    public final static int SETTING_ID_DEFAULT_WATERMARK        = 3;
    public final static int SETTING_ID_DEFAULT_VIDEO_HD         = 4;

    private final static String SETTING_CACHE_KEY               = "SettingManager";

    private int defaultFilter;
    private int cameraRatio;
    private int videoTime;
    private boolean isWatermark;
    private String watermarkText;
    private boolean isHD;

    private static SettingManager instance;

    public static synchronized SettingManager getInstance() {
        if (SettingManager.instance == null) {
            SettingManager.instance = new SettingManager();
        }
        return SettingManager.instance;
    }

    public void save() {
        CacheManager.getInstance().addCache(new CacheItem(SETTING_CACHE_KEY, this, -1));
    }

    public SettingManager read() {
        return (SettingManager)CacheManager.getInstance().getCache(SETTING_CACHE_KEY);
    }

    public int getDefaultFilter() {
        return defaultFilter;
    }

    public void setDefaultFilter(int defaultFilter) {
        this.defaultFilter = defaultFilter;
    }

    public String getDefaultFilterText() {
        return BeautyApplication.context().getString(ARR_FILTER_TEXTS[defaultFilter]);
    }

    public int getCameraRatio() {
        return cameraRatio;
    }

    public void setCameraRatio(int cameraRatio) {
        this.cameraRatio = cameraRatio;
    }

    public String getCameraRatioText() {
        return BeautyApplication.context().getString(ARR_VIDEO_CAMERA_RATIO_TEXTS[getCameraRatio()]);
    }

    public int getVideoTime() {
        return videoTime;
    }

    public void setVideoTime(int videoTime) {
        this.videoTime = videoTime;
    }

    public String getVideoTimeText() {
        return getVideoTime(ARR_VIDEO_TIME[getVideoTime()]);
    }

    public static String getVideoTime(float time) {

        DecimalFormat decimalFormat = new DecimalFormat("###################.###########");
        return String.format(BeautyApplication.context().getString(R.string.videoTime), decimalFormat.format(time));
    }

    public boolean isWatermark() {
        return isWatermark;
    }

    public void setWatermark(boolean watermark) {
        isWatermark = watermark;
    }

    public boolean isHD() {
        return isHD;
    }

    public void setHD(boolean HD) {
        isHD = HD;
    }

    public String getWatermarkText() {
        return watermarkText;
    }

    public void setWatermarkText(String watermarkText) {
        this.watermarkText = watermarkText;
    }
}
