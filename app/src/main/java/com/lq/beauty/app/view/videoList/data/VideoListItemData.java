package com.lq.beauty.app.view.videoList.data;

import android.text.TextUtils;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/3/25.
 */

public class VideoListItemData implements Serializable {
    private boolean isMy;
    private String name;
    private String videoPath;
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

    public boolean isMy() {
        return isMy;
    }

    public void setMy(boolean my) {
        isMy = my;
    }

    @Override
    public int hashCode() {
        final int prime = 15;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((time == null) ? 0 : time.hashCode());
        result = prime * result + ((videoPath == null) ? 0 : videoPath.hashCode());
        result = prime * result + (isMy?1:0);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        VideoListItemData other = (VideoListItemData) obj;

        return (isMy == other.isMy) &&
                TextUtils.equals(name, other.name) &&
                TextUtils.equals(videoPath, other.videoPath) &&
                TextUtils.equals(time, other.time);
    }
}
