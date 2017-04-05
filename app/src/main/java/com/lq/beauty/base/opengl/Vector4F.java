package com.lq.beauty.base.opengl;

import android.text.TextUtils;

import com.lq.beauty.app.view.videoList.data.VideoListItemData;

/**
 * Created by wuqingqing on 2017/4/5.
 */

public class Vector4F {
    public float x, y, z, w;

    public Vector4F(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    @Override
    public int hashCode() {
        int ret = (int) (x*255);
        ret = ret << 8 + (int)(y*255);
        ret = ret << 8 + (int)(z*255);
        ret = ret << 8 + (int)(w*255);
        return ret;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Vector4F other = (Vector4F) obj;

        return x==other.x && y==other.y && z==other.z && w==other.w;
    }
}
