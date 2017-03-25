package com.lq.beauty.app.camera.video;

import android.os.Environment;


/**
 * Created by why8222 on 2016/2/26.
 */
public class VideoParams {
    public static final String videoPath = Environment.getExternalStorageDirectory().getPath();
    public static final String videoName = "MagicCamera_test2.mp4";
    public static final String MIME_TYPE = "video/avc";

    public static final int VIDEO_BIT_RATE = 2000000;
    public static final int FRAME_RATE = 30;
    public static final int IFRAME_INTERVAL = 5;

    public int videoBitRate;
    public int videoFrameRate;
    public int videoIFrameInterval;
    public String videoMimeType;

    public VideoParams() {
        videoMimeType = MIME_TYPE;
        videoBitRate = VIDEO_BIT_RATE;
        videoFrameRate = FRAME_RATE;
        videoIFrameInterval = IFRAME_INTERVAL;
    }
}
