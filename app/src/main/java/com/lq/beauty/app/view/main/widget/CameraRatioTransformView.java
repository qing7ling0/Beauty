package com.lq.beauty.app.view.main.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.lq.beauty.app.RxBus.RxBus;
import com.lq.beauty.app.SettingManager;
import com.lq.beauty.base.opengl.Vector4F;

/**
 * Created by wuqingqing on 2017/4/6.
 */

public class CameraRatioTransformView extends View {
    private final static int SIZE_CHANGE_STATUS_NONE = 0;
    private final static int SIZE_CHANGE_STATUS_BEGAN = 1;
    private final static int SIZE_CHANGE_STATUS_CHANGING = 2;
    private final static int SIZE_CHANGE_STATUS_FINISH = 3;

    private final static long COUNTDOWN_MAX = 500; // ms

    private Rectangle rectangles[];
    private int cameraRatio;
    private int sizeChangeStatus;
    private Paint paint;
    private long lastTime;

    public CameraRatioTransformView(Context context) {
        super(context);
    }

    public CameraRatioTransformView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CameraRatioTransformView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    protected void init() {
        rectangles = new Rectangle[4];
        for (int i=0; i<rectangles.length; i++) {
            rectangles[i] = new Rectangle(new Vector4F(0, 0, 0, 0), new Vector4F(0, 0, 0, 0));
        }

        paint = new Paint();
        paint.setColor(0xff000000);
        paint.setAlpha(120);
        paint.setAntiAlias(true);

        int height = getHeight();
        int width = getWidth();
        rectangles[0].setToCoords(new Vector4F(0, 0, 0, height));
        rectangles[1].setFromCoords(new Vector4F(width, 0, width, height));
        rectangles[2].setFromCoords(new Vector4F(0, 0, width, 0));
        rectangles[3].setFromCoords(new Vector4F(0, height, width, height));
    }

    public void setCameraRatio(int cameraRatio) {
        if (this.cameraRatio != cameraRatio) {
            this.cameraRatio = cameraRatio;
            sizeChangeStatus = SIZE_CHANGE_STATUS_BEGAN;
            invalidate();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(null == rectangles) {
            init();
        }

        if (sizeChangeStatus == SIZE_CHANGE_STATUS_NONE) {
            for(int i=0; i<rectangles.length; i++) {
                Rectangle rect = rectangles[i];
                if (null != rect.curCoords) {
                    paint.setColor(rect.color);
                    canvas.drawRect(rect.curCoords.x, rect.curCoords.y, rect.curCoords.z, rect.curCoords.w, paint);
                }
            }
        } else {
            switch (sizeChangeStatus) {
                case SIZE_CHANGE_STATUS_BEGAN:
                    int width = getWidth();
                    int height = getHeight();
                    int toWidth = SettingManager.getVideoCameraRatioWidth(this.cameraRatio, width);
                    int toHeight = SettingManager.getVideoCameraRatioHeight(this.cameraRatio, height);;

                    int w = (width - toWidth) / 2;
                    int h = (height - toHeight) / 2;

                    rectangles[0].setToCoords(new Vector4F(0, 0, w, height));
                    rectangles[1].setToCoords(new Vector4F(width-w, 0, width, height));
                    rectangles[2].setToCoords(new Vector4F(0, 0, width, h));
                    rectangles[3].setToCoords(new Vector4F(0, height-h, width, height));

                    sizeChangeStatus = SIZE_CHANGE_STATUS_CHANGING;
                    lastTime = 0;
                    invalidate();
                    break;
                case SIZE_CHANGE_STATUS_CHANGING:
                    if (lastTime == 0)
                        lastTime = System.currentTimeMillis();
                    float cd = Math.min((System.currentTimeMillis() - lastTime)*1.0f / COUNTDOWN_MAX, 1);

                    for(int i=0; i<rectangles.length; i++) {
                        Rectangle rect = rectangles[i];
                        rect.update(cd);
                        paint.setColor(rect.color);
                        canvas.drawRect(rect.curCoords.x, rect.curCoords.y, rect.curCoords.z, rect.curCoords.w, paint);
                    }
                    if (cd >= 1) {
                        sizeChangeStatus = SIZE_CHANGE_STATUS_FINISH;
                    }
                    invalidate();
                    break;
                case SIZE_CHANGE_STATUS_FINISH:
                    sizeChangeStatus = SIZE_CHANGE_STATUS_NONE;
                    for(int i=0; i<rectangles.length; i++) {
                        Rectangle rect = rectangles[i];
                        paint.setColor(rect.color);
                        canvas.drawRect(rect.curCoords.x, rect.curCoords.y, rect.curCoords.z, rect.curCoords.w, paint);
                    }
                    RxBus.getInstance().post(new RxBusEventChangeCameraRatio());
                    break;
            }
        }

    }

    public class RxBusEventChangeCameraRatio {
        public int id;
    }

    class Rectangle {

        Vector4F fromCoords;
        Vector4F toCoords;
        Vector4F curCoords;
        Vector4F dirCoords;

        boolean isChanging;


        int color = 0XFF000000;


        public Rectangle(Vector4F from, Vector4F to) {
            fromCoords = from;
            toCoords = to;
            dirCoords = new Vector4F(0, 0, 0, 0);
            isChanging = false;
        }

        public void setToCoords(Vector4F toCoords) {
            this.toCoords = toCoords;
            if (null != curCoords) {
                this.fromCoords.x = curCoords.x;
                this.fromCoords.y = curCoords.y;
                this.fromCoords.z = curCoords.z;
                this.fromCoords.w = curCoords.w;
            }
            this.dirCoords.x = toCoords.x - fromCoords.x;
            this.dirCoords.y = toCoords.y - fromCoords.y;
            this.dirCoords.z = toCoords.z - fromCoords.z;
            this.dirCoords.w = toCoords.w - fromCoords.w;
            isChanging = true;
        }

        public void setFromCoords(Vector4F fromCoords) {
            this.fromCoords = fromCoords;
        }

        public void update(float f) {
            if (isChanging) {
                if (null == curCoords) {
                    curCoords = new Vector4F(fromCoords.x, fromCoords.y, fromCoords.z, fromCoords.w);
                }
                if (toCoords.equals(curCoords)) {
                    isChanging = false;
                } else {
                    curCoords.x = fromCoords.x + dirCoords.x * f;
                    curCoords.y = fromCoords.y + dirCoords.y * f;
                    curCoords.z = fromCoords.z + dirCoords.z * f;
                    curCoords.w = fromCoords.w + dirCoords.w * f;
                }
            }
        }

        public void setColor(int color) {
            this.color = color;
        }
    }

}
