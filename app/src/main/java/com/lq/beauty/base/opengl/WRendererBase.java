package com.lq.beauty.base.opengl;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.annotation.TargetApi;
import android.opengl.GLSurfaceView;
import android.os.Build;

@TargetApi(Build.VERSION_CODES.CUPCAKE)
public class WRendererBase implements GLSurfaceView.Renderer {
    protected final static long NANOSECONDSPERSECOND = 1000000000L;
    protected final static long NANOSECONDSPERMICROSECOND = 1000000;

    protected static long sAnimationInterval = (long) (1.0 / 60 * WRendererBase.NANOSECONDSPERSECOND);

    protected long mLastTickInNanoSeconds;
    protected int mScreenWidth;
    protected int mScreenHeight;
    protected boolean mNativeInitCompleted = false;
    protected WGLSurfaceView mWGLSurfaceView = null;

    public static void setAnimationInterval(final double animationInterval) {
        WRendererBase.sAnimationInterval = (long) (animationInterval * WRendererBase.NANOSECONDSPERSECOND);
    }

    public void setWGLSurfaceView(WGLSurfaceView view) {
        this.mWGLSurfaceView = view;
    }

    public void setScreenWidthAndHeight(final int surfaceWidth, final int surfaceHeight) {
        this.mScreenWidth = surfaceWidth;
        this.mScreenHeight = surfaceHeight;
    }

    @Override
    public void onSurfaceCreated(final GL10 GL10, final EGLConfig EGLConfig) {
        WRendererBase.nativeInit(this.mScreenWidth, this.mScreenHeight);
        this.mLastTickInNanoSeconds = System.nanoTime();
        mNativeInitCompleted = true;
    }

    @Override
    public void onSurfaceChanged(final GL10 GL10, final int width, final int height) {
        WRendererBase.nativeOnSurfaceChanged(width, height);
    }

    @Override
    public void onDrawFrame(final GL10 gl) {
        if (sAnimationInterval <= 1.0 / 60 * WRendererBase.NANOSECONDSPERSECOND) {
            onRender(gl);
        } else {
            final long now = System.nanoTime();
            final long interval = now - this.mLastTickInNanoSeconds;
        
            if (interval < WRendererBase.sAnimationInterval) {
                try {
                    Thread.sleep((WRendererBase.sAnimationInterval - interval) / WRendererBase.NANOSECONDSPERMICROSECOND);
                } catch (final Exception e) {
                }
            }
            this.mLastTickInNanoSeconds = System.nanoTime();
            onRender(gl);
        }
    }

    protected void onRenderBefore(final GL10 gl) {}

    protected void onRender(final GL10 gl) {
        onRenderBefore(gl);
        WRendererBase.nativeRender();
        onRenderAfter(gl);
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
