package com.lq.beauty.app.camera.render;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.SizeF;

import com.lq.beauty.base.opengl.Vector4F;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by wuqingqing on 2017/4/5.
 */

public class ViewPortRender {

    private final static int SIZE_CHANGE_STATUS_NONE = 0;
    private final static int SIZE_CHANGE_STATUS_BEGAN = 1;
    private final static int SIZE_CHANGE_STATUS_CHANGING = 2;
    private final static int SIZE_CHANGE_STATUS_FINISH = 3;


    private final String vertexShaderCode =
            "attribute vec4 vPosition;" +
                    "void main()" +
                    "{"+
                    "gl_Position = vPosition;"+
                    "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;\n" +
                    "}";

    float color[] = {1.0f, 1.0f, 1.0f, 1.0f};//绿色不透明

    private int mProgram = -1;
    private int mPositionHandle;
    private int mColorHandle;
    private VertexBuffer vertexBuffer;
    private ArrayList<Rectangle> rectangles;
    private int width;
    private int height;
    private int toWidth;
    private int toHeight;
    private int sizeChangeStatus;

    private short indexVertex[] = { 0, 1, 2, 0, 2, 3 }; // order to draw vertices

    // number of coordinates per vertex in this array
    private static final int COORDS_PER_VERTEX = 2;

    private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex


    public ViewPortRender()
    {
        vertexBuffer = new VertexBuffer(10, 8, 6);
        rectangles = new ArrayList<>();
        rectangles.add(new Rectangle(new Vector4F(0,0,0,0), new Vector4F(0,0,0,0))); // left
        rectangles.add(new Rectangle(new Vector4F(0,0,0,0), new Vector4F(0,0,0,0))); // right
        rectangles.add(new Rectangle(new Vector4F(0,0,0,0), new Vector4F(0,0,0,0))); // top
        rectangles.add(new Rectangle(new Vector4F(0,0,0,0), new Vector4F(0,0,0,0))); // bottom
        sizeChangeStatus = SIZE_CHANGE_STATUS_NONE;
    }

    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
        Rectangle rectLeft = rectangles.get(0);
        Rectangle rectRight = rectangles.get(1);
        Rectangle rectTop = rectangles.get(2);
        Rectangle rectBottom = rectangles.get(3);

        rectLeft.setFromCoords(new Vector4F(0, 0, 0, height));
        rectRight.setFromCoords(new Vector4F(width, 0, 0, height));
        rectTop.setFromCoords(new Vector4F(0, height, width, 0));
        rectBottom.setFromCoords(new Vector4F(0, 0, width, 0));
    }

    public void setToSize(int width, int height) {
        this.toWidth = width;
        this.toHeight = height;
        this.sizeChangeStatus = SIZE_CHANGE_STATUS_BEGAN;
    }

    private  int loadShader(int type, String shaderCode){

        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }

    static int disk = 1;

    private void update() {
        if (mProgram == -1) {
            int vertexShader    = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
            int fragmentShader  = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);

            mProgram = GLES20.glCreateProgram();             // create empty OpenGL ES Program
            GLES20.glAttachShader(mProgram, vertexShader);   // add the vertex shader to program
            GLES20.glAttachShader(mProgram, fragmentShader); // add the fragment shader to program
            GLES20.glLinkProgram(mProgram);
        }

        if (sizeChangeStatus == SIZE_CHANGE_STATUS_NONE) {
        } else {
            switch (sizeChangeStatus) {
                case SIZE_CHANGE_STATUS_BEGAN:
                    vertexBuffer.reset();
                    Rectangle rectLeft = rectangles.get(0);
                    Rectangle rectRight = rectangles.get(1);
                    Rectangle rectTop = rectangles.get(2);
                    Rectangle rectBottom = rectangles.get(3);

                    int w = (width - toWidth) / 2;
                    int h = (height - toHeight) / 2;

                    rectLeft.setToCoords(new Vector4F(0, 0, w, height));
                    rectRight.setToCoords(new Vector4F(width-w, 0, w, height));
                    rectTop.setToCoords(new Vector4F(0, height-h, width, h));
                    rectBottom.setToCoords(new Vector4F(0, 0, width, h));

                    sizeChangeStatus = SIZE_CHANGE_STATUS_CHANGING;
                    disk = 0;
                    break;
                case SIZE_CHANGE_STATUS_CHANGING:
                    boolean isChange = true;
                    for(int i=0; i<rectangles.size(); i++) {
                        Rectangle rect = rectangles.get(i);
                        rect.update(disk*1.0f / 2);
                        vertexBuffer.put(rect.getRenderVertex(width, height), indexVertex);
                        if (!rect.isChanging) isChange = false;
//                        break;
                    }
                    vertexBuffer.position(0);
                    if (!isChange) {
                        sizeChangeStatus = SIZE_CHANGE_STATUS_FINISH;
                    }
                    disk++;
                    break;
                case SIZE_CHANGE_STATUS_FINISH:
                    sizeChangeStatus = SIZE_CHANGE_STATUS_NONE;
                    break;
            }
        }


    }

    public void render(final GL10 gl)
    {
        update();
        GLES20.glUseProgram(mProgram);

        // get handle to vertex shader's vPosition member
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");

        // Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // Prepare the <insert shape here> coordinate data
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, vertexStride, vertexBuffer.vertexBuffer);

        mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
        GLES20.glUniform4fv(mColorHandle, 1, color, 0);

        GLES20.glDrawElements(GLES20.GL_TRIANGLES, vertexBuffer.indexVertexCount(), GLES20.GL_UNSIGNED_SHORT, vertexBuffer.indexBuffer);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }

    class VertexBuffer {
        int vexSize;
        int indexSize;
        int maxCount;
        int size;

        FloatBuffer vertexBuffer;
        ShortBuffer indexBuffer;


        public VertexBuffer(int maxCount, int vexSize, int indexSize) {
            this.maxCount = maxCount;
            this.vexSize = vexSize;
            this.indexSize = indexSize;

            ByteBuffer bb = ByteBuffer.allocateDirect(vexSize * maxCount * 4);
            bb.order(ByteOrder.nativeOrder());
            vertexBuffer = bb.asFloatBuffer();
            vertexBuffer.position(0);


            ByteBuffer dlb = ByteBuffer.allocateDirect(indexSize * maxCount * 2);
            dlb.order(ByteOrder.nativeOrder());
            indexBuffer = dlb.asShortBuffer();
            indexBuffer.position(0);
        }

        public void reset() {
            position(0);
            size = 0;
        }

        public void put(float vexCoords[], short vexIndex[]) {
            if (size >= maxCount) {
                // TODO ERROR
                return;
            }
            vertexBuffer.put(vexCoords);
            indexBuffer.put(vexIndex);
            size++;
        }

        public void position(int index) {
            vertexBuffer.position(index * vexSize);
            indexBuffer.position(index * indexSize);
        }

        public int indexVertexCount()  {
            return indexSize * size;
        }
    }

    class Rectangle {

        float drawCoords[] = {
                -0.5f,  0.5f,
                -0.5f, -0.5f,
                0.5f, -0.5f,
                1.0f,  1.0f,
        };

        Vector4F fromCoords;
        Vector4F toCoords;
        Vector4F curCoords;
        Vector4F dirCoords;

        boolean isChanging;


        float color[] = {0.0f, 1.0f, 0f, 1.0f};//绿色不透明


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

        public float[] getRenderVertex(int viewWidth, int viewHeight) {
            float vertex[] = new float[8];
            vertex[0] = vertex[2] = (curCoords.x * 2 / viewWidth) - 1.0f;
            vertex[1] = vertex[7] = (curCoords.w * 2 / viewHeight) - 1.0f;
            vertex[3] = vertex[5] = (curCoords.y * 2 / viewHeight) - 1.0f;
            vertex[4] = vertex[6] = (curCoords.z * 2 / viewWidth) - 1.0f;

            return vertex;
        }

        public void setColor(float r, float g, float b, float a) {
            color[0] = r;
            color[1] = g;
            color[2] = b;
            color[3] = a;
        }

    }
}
