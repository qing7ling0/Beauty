package com.lq.beauty.base.opengl;

import android.annotation.TargetApi;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;

/**
 * Created by wuqingqing on 2017/3/8.
 */
@TargetApi(Build.VERSION_CODES.CUPCAKE)
public class WGLSurfaceView extends GLSurfaceView {
    static{
        System.loadLibrary("look-lib");
    }

    private static final String TAG = WGLSurfaceView.class.getSimpleName();


    private static WGLSurfaceView mWGLSurfaceView;

    private WRendererBase mWRenderer;

    public WGLSurfaceView(final Context context) {
        super(context);

        this.initView();
    }

    public WGLSurfaceView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        
        this.initView();
    }

    protected void initView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
            this.setEGLContextClientVersion(2);
        }
        this.setFocusableInTouchMode(true);

        WGLSurfaceView.mWGLSurfaceView = this;
    }

    public static WGLSurfaceView getInstance() {
       return mWGLSurfaceView;
       }

    public void setWRenderer(final WRendererBase renderer) {
        this.mWRenderer = renderer;
        this.setRenderer(this.mWRenderer);
        this.mWRenderer.setWGLSurfaceView(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        this.setRenderMode(RENDERMODE_CONTINUOUSLY);
        this.queueEvent(new Runnable() {
            @Override
            public void run() {
                WGLSurfaceView.this.mWRenderer.handleOnResume();
            }
        });
    }

    @Override
    public void onPause() {
        this.queueEvent(new Runnable() {
            @Override
            public void run() {
                WGLSurfaceView.this.mWRenderer.handleOnPause();
            }
        });
        this.setRenderMode(RENDERMODE_WHEN_DIRTY);
    }

    @Override
    public boolean onTouchEvent(final MotionEvent pMotionEvent) {
        return true;
    }

    /*
     * This function is called before WRendererBase.nativeInit(), so the
     * width and height is correct.
     */
    @Override
    protected void onSizeChanged(final int pNewSurfaceWidth, final int pNewSurfaceHeight, final int pOldSurfaceWidth, final int pOldSurfaceHeight) {
        if(!this.isInEditMode()) {
            this.mWRenderer.setScreenWidthAndHeight(pNewSurfaceWidth, pNewSurfaceHeight);
        }
    }

    @Override
    public boolean onKeyDown(final int pKeyCode, final KeyEvent pKeyEvent) {
        switch (pKeyCode) {
            case KeyEvent.KEYCODE_BACK:
            case KeyEvent.KEYCODE_MENU:
            case KeyEvent.KEYCODE_DPAD_LEFT:
            case KeyEvent.KEYCODE_DPAD_RIGHT:
            case KeyEvent.KEYCODE_DPAD_UP:
            case KeyEvent.KEYCODE_DPAD_DOWN:
            case KeyEvent.KEYCODE_ENTER:
            case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
            case KeyEvent.KEYCODE_DPAD_CENTER:
                this.queueEvent(new Runnable() {
                    @Override
                    public void run() {
                        WGLSurfaceView.this.mWRenderer.handleKeyDown(pKeyCode);
                    }
                });
                return true;
            default:
                return super.onKeyDown(pKeyCode, pKeyEvent);
        }
    }

    private static void dumpMotionEvent(final MotionEvent event) {
//        final String names[] = { "DOWN", "UP", "MOVE", "CANCEL", "OUTSIDE", "POINTER_DOWN", "POINTER_UP", "7?", "8?", "9?" };
//        final StringBuilder sb = new StringBuilder();
//        final int action = event.getAction();
//        final int actionCode = action & MotionEvent.ACTION_MASK;
//        sb.append("event ACTION_").append(names[actionCode]);
//        if (actionCode == MotionEvent.ACTION_POINTER_DOWN || actionCode == MotionEvent.ACTION_POINTER_UP) {
//            sb.append("(pid ").append(action >> MotionEvent.ACTION_POINTER_INDEX_SHIFT);
//            sb.append(")");
//        }
//        sb.append("[");
//        for (int i = 0; i < event.getPointerCount(); i++) {
//            sb.append("#").append(i);
//            sb.append("(pid ").append(event.getPointerId(i));
//            sb.append(")=").append((int) event.getX(i));
//            sb.append(",").append((int) event.getY(i));
//            if (i + 1 < event.getPointerCount()) {
//                sb.append(";");
//            }
//        }
//        sb.append("]");
//        Log.d(WGLSurfaceView.TAG, sb.toString());
    }
}
