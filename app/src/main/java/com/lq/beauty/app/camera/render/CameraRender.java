package com.lq.beauty.app.camera.render;

import android.graphics.SurfaceTexture;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.util.Log;

import com.lq.beauty.app.camera.CameraEngine;
import com.lq.beauty.base.opengl.WRendererBase;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by wuqingqing on 2017/3/9.
 */

public class CameraRender extends WRendererBase implements SurfaceTexture.OnFrameAvailableListener {

    private SurfaceTexture mSurfaceTexture;
    private int mSurfaceTextureID = -1;

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        super.onSurfaceCreated(gl, config);
        Log.i("", "onSurfaceCreated...");

        mSurfaceTextureID = createTextureID();
        mSurfaceTexture = new SurfaceTexture(mSurfaceTextureID);
        mSurfaceTexture.setOnFrameAvailableListener(this);
        CameraEngine.openCamera();

        setFilterType(0);
        setTextureID(mSurfaceTextureID);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        super.onSurfaceChanged(gl, width, height);

        Log.i("", "onSurfaceChanged...");
        if (CameraEngine.getCamera() == null)
            CameraEngine.openCamera();

        if (mSurfaceTexture != null)
            CameraEngine.startPreview(mSurfaceTexture);
    }

    @Override
    protected void onRenderBefore(final GL10 gl) {
        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        if (mSurfaceTexture != null)
            mSurfaceTexture.updateTexImage();
    }

    @Override
    protected void onRenderAfter(final GL10 gl) {

    }

//    @Override
//    protected void onRender(final GL10 gl) {
//        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
//        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
//        mSurfaceTexture.updateTexImage();
//        float[] mtx = new float[16];
//        mSurfaceTexture.getTransformMatrix(mtx);
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


    private native void setTextureID(int id);

    private native void setFilterType(int type);
}
