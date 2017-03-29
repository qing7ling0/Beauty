package com.lq.beauty.app.videoList.data;

import com.lq.beauty.base.cache.CacheItem;
import com.lq.beauty.base.cache.CacheManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuqingqing on 2017/3/28.
 */

public class VideoListItems extends CacheItem {
    public static final String KEY = "VideoListItems";
    public static final int EXPIRED_TIME = 1000;

    public VideoListItems() {
        super(KEY, null, EXPIRED_TIME);
    }


    public static void cache(List<VideoListItemData> datas) {
        VideoListItems items = new VideoListItems();
        items.setData(datas);
        CacheManager.getInstance().addCache(items);
    }
}
