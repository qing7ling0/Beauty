package com.lq.beauty.app.camera.render;

import android.graphics.SurfaceTexture;
import android.opengl.EGL14;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import com.lq.beauty.app.camera.CameraEngine;
import com.lq.beauty.app.camera.CameraHelper;
import com.lq.beauty.app.camera.WCameraInfo;
import com.lq.beauty.app.camera.video.TextureMovieHandler;
import com.lq.beauty.base.opengl.WRendererBase;

import java.io.File;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by wuqingqing on 2017/3/9.
 */

public class CameraRender extends WRendererBase implements SurfaceTexture.OnFrameAvailableListener, TextureMovieHandler.IMovieRender {

    private static final int RECORDING_OFF = 0;
    private static final int RECORDING_ON = 1;
    private static final int RECORDING_RESUMED = 2;
    
    private SurfaceTexture surfaceTexture;
    private int surfaceTextureID = -1;
    private boolean recordingEnabled;
    private int recordingStatus;

    private static TextureMovieHandler videoHandler = new TextureMovieHandler();

    public CameraRender() {
        super();
        recordingStatus = -1;
        recordingEnabled = false;
        videoHandler.setMovieRender(this);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        super.onSurfaceCreated(gl, config);
        if (mWGLSurfaceView != null) {
            mWGLSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        }

        recordingEnabled = videoHandler.isRecording();
        if (recordingEnabled)
            recordingStatus = RECORDING_RESUMED;
        else
            recordingStatus = RECORDING_OFF;

        surfaceTextureID = createTextureID();
        surfaceTexture = new SurfaceTexture(surfaceTextureID);
        surfaceTexture.setOnFrameAvailableListener(this);

        setFilterType(0);
        nativeSetTextureID(surfaceTextureID);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        super.onSurfaceChanged(gl, width, height);

        CameraEngine.setScreenPreviewSize(width, height);
        if (CameraEngine.getCamera() == null)
            CameraEngine.openCamera();

        if (surfaceTexture != null)
            CameraEngine.startPreview(surfaceTexture);
        if (CameraEngine.getCamera() != null) {
            WCameraInfo info = CameraEngine.getCameraInfo();
            int w = info.previewWidth;
            int h = info.previewHeight;
            if (info.orientation == 90 || info.orientation == 270) {
                w = info.previewHeight;
                h = info.previewWidth;
            }
            nativeSetCameraSize(w, h);
        }
    }

    @Override
    protected void onRenderBefore(final GL10 gl) {
        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        if (surfaceTexture != null){
            surfaceTexture.updateTexImage();

            if (recordingEnabled) {
                switch (recordingStatus) {
                    case RECORDING_OFF:
                        WCameraInfo info = CameraEngine.getCameraInfo();
                        videoHandler.setPreviewSize(info.previewWidth, info.pictureHeight);
                        videoHandler.startRecording(
                                new TextureMovieHandler.EncoderConfig(
                                        new File(CameraHelper.createVideoFilePath()),
                                        renderWidth,
                                        renderHeight,
                                        EGL14.eglGetCurrentContext(),
                                        info));
                        recordingStatus = RECORDING_ON;
                        break;
                    case RECORDING_RESUMED:
                        videoHandler.updateSharedContext(EGL14.eglGetCurrentContext());
                        recordingStatus = RECORDING_ON;
                        break;
                    case RECORDING_ON:
                        videoHandler.frameAvailable(surfaceTexture);
                        break;
                    default:
                        throw new RuntimeException("unknown status " + recordingStatus);
                }
            } else {
                switch (recordingStatus) {
                    case RECORDING_ON:
                    case RECORDING_RESUMED:
                        videoHandler.stopRecording();
                        recordingStatus = RECORDING_OFF;
                        break;
                    case RECORDING_OFF:
                        break;
                    default:
                        throw new RuntimeException("unknown status " + recordingStatus);
                }
            }
        }
    }

    @Override
    protected void onRenderAfter(final GL10 gl) {
    }

//    @Override
//    protected void onRender(final GL10 gl) {
//        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
//        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
//        surfaceTexture.updateTexImage();
//        float[] mtx = new float[16];
//        surfaceTexture.getTransformMatrix(mtx);
//    }

    private int createTextureID()
    {
        int[] texture = new int[1];

        GLES20.glGenTextures(1, texture, 0);
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, texture[0]);
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_MIN_FILTER,GL10.GL_LINEAR);
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);

        return texture[0];
    }

    @Override
    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
        mWGLSurfaceView.requestRender();
    }

    public void changeRecordingState(boolean isRecording) {
        recordingEnabled = isRecording;
    }

    private native void nativeSetCameraSize(int width, int height);

    private native void nativeSetTextureID(int id);

    private native void setFilterType(int type);

    @Override
    public void render() {
        this.onDraw();
    }
}
