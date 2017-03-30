package com.lq.beauty.app.data;

import java.io.Serializable;

/**
 * Created by wuqingqing on 2017/3/30.
 */

public class SettingData implements Serializable {
    public final static int SETTING_ID_DEFAULT_FILTER           = 1;
    public final static int SETTING_ID_DEFAULT_CAMERA_RATIO     = 2;
    public final static int SETTING_ID_DEFAULT_VIDEO_TIME       = 3;
    public final static int SETTING_ID_DEFAULT_WATERMARK        = 4;
    public final static int SETTING_ID_DEFAULT_VIDEO_HD         = 5;

    private int defaultFilter;
    private int cameraRatio;
    private int videoTime;
    private boolean isWatermark;
    private boolean isHD;

    public int getDefaultFilter() {
        return defaultFilter;
    }

    public void setDefaultFilter(int defaultFilter) {
        this.defaultFilter = defaultFilter;
    }

    public int getCameraRatio() {
        return cameraRatio;
    }

    public void setCameraRatio(int cameraRatio) {
        this.cameraRatio = cameraRatio;
    }

    public int getVideoTime() {
        return videoTime;
    }

    public void setVideoTime(int videoTime) {
        this.videoTime = videoTime;
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
}
