package com.lq.beauty.base.opengl;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.annotation.TargetApi;
import android.opengl.GLSurfaceView;
import android.os.Build;

@TargetApi(Build.VERSION_CODES.CUPCAKE)
public class WRendererBase implements GLSurfaceView.Renderer {

    protected long mLastTickInNanoSeconds;
    protected int renderWidth;
    protected int renderHeight;
    protected boolean mNativeInitCompleted = false;
    protected WGLSurfaceView mWGLSurfaceView = null;

    public void setWGLSurfaceView(WGLSurfaceView view) {
        this.mWGLSurfaceView = view;
    }

    public void setRenderWidthAndHeight(final int surfaceWidth, final int surfaceHeight) {
        this.renderWidth = surfaceWidth;
        this.renderHeight = surfaceHeight;
    }

    @Override
    public void onSurfaceCreated(final GL10 GL10, final EGLConfig EGLConfig) {
        WRendererBase.nativeInit(this.renderWidth, this.renderHeight);
        this.mLastTickInNanoSeconds = System.nanoTime();
        mNativeInitCompleted = true;
    }

    @Override
    public void onSurfaceChanged(final GL10 GL10, final int width, final int height) {
        WRendererBase.nativeOnSurfaceChanged(width, height);
        setRenderWidthAndHeight(width, height);
    }

    @Override
    public void onDrawFrame(final GL10 gl) {
        onRender(gl);
    }

    protected void onRenderBefore(final GL10 gl) {}

    protected void onRender(final GL10 gl) {
        onRenderBefore(gl);
        onDraw();
        onRenderAfter(gl);
    }

    protected synchronized void onDraw() {
        WRendererBase.nativeRender();
    }

    protected void onRenderAfter(final GL10 gl) {}

    private static native void nativeTouchesBegin(final int id, final float x, final float y);
    private static native void nativeTouchesEnd(final int id, final float x, final float y);
    private static native void nativeTouchesMove(final int[] ids, final float[] xs, final float[] ys);
    private static native void nativeTouchesCancel(final int[] ids, final float[] xs, final float[] ys);
    private static native boolean nativeKeyDown(final int keyCode);
    private static native void nativeRender();
    private static native void nativeInit(final int width, final int height);
    private static native void nativeOnSurfaceChanged(final int width, final int height);
    private static native void nativeOnPause();
    private static native void nativeOnResume();

    public void handleActionDown(final int id, final float x, final float y) {
        WRendererBase.nativeTouchesBegin(id, x, y);
    }

    public void handleActionUp(final int id, final float x, final float y) {
        WRendererBase.nativeTouchesEnd(id, x, y);
    }

    public void handleActionCancel(final int[] ids, final float[] xs, final float[] ys) {
        WRendererBase.nativeTouchesCancel(ids, xs, ys);
    }

    public void handleActionMove(final int[] ids, final float[] xs, final float[] ys) {
        WRendererBase.nativeTouchesMove(ids, xs, ys);
    }

    public void handleKeyDown(final int keyCode) {
        WRendererBase.nativeKeyDown(keyCode);
    }

    public void handleOnPause() {
        if (! mNativeInitCompleted)
            return;

        WRendererBase.nativeOnPause();
    }

    public void handleOnResume() {
        WRendererBase.nativeOnResume();
    }

}
