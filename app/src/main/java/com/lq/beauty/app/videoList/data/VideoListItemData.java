package com.lq.beauty.app.videoList.data;

import android.graphics.Bitmap;

import com.lq.beauty.base.cache.CacheItem;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/3/25.
 */

public class VideoListItemData implements Serializable {
    private boolean isMy;
    private String name;
    private String videoPath;
    private Bitmap bitmap;
    private String time;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public boolean isMy() {
        return isMy;
    }

    public void setMy(boolean my) {
        isMy = my;
    }
}
