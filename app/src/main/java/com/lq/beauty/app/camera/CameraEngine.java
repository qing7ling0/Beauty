package com.lq.beauty.app.camera;

import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.view.SurfaceView;

import java.io.IOException;
import java.util.List;

public class CameraEngine {
    private static Camera camera = null;
    private static int cameraID = 0;
    private static SurfaceTexture surfaceTexture;
    private static SurfaceView surfaceView;
    private static int screenPreviewWidth;
    private static int screenPreviewHeight;

    public static Camera getCamera(){
        return camera;
    }

    public static int getScreenPreviewWidth() {
        return screenPreviewWidth;
    }

    public static void setScreenPreviewWidth(int screenPreviewWidth) {
        CameraEngine.screenPreviewWidth = screenPreviewWidth;
    }

    public static int getScreenPreviewHeight() {
        return screenPreviewHeight;
    }

    public static void setScreenPreviewHeight(int screenPreviewHeight) {
        CameraEngine.screenPreviewHeight = screenPreviewHeight;
    }

    public static void setScreenPreviewSize(int width, int height) {
        setScreenPreviewWidth(width);
        setScreenPreviewHeight(height);
    }

    public static boolean openCamera(){
        if(camera == null){
            try{
                camera = Camera.open(cameraID);
                setDefaultParameters();
                return true;
            }catch(RuntimeException e){
                return false;
            }
        }
        return false;
    }

    public static boolean openCamera(int id){
        if(camera == null){
            try{
                camera = Camera.open(id);
                cameraID = id;
                setDefaultParameters();
                return true;
            }catch(RuntimeException e){
                return false;
            }
        }
        return false;
    }

    public static void releaseCamera(){
        if(camera != null){
            camera.setPreviewCallback(null);
            camera.stopPreview();
            camera.release();
            camera = null;
        }
    }

    public void resumeCamera(){
        openCamera();
    }

    public void setParameters(Parameters parameters){
        camera.setParameters(parameters);
    }

    public Parameters getParameters(){
        if(camera != null)
            camera.getParameters();
        return null;
    }

    public static void switchCamera(){
        releaseCamera();
        cameraID = cameraID == 0 ? 1 : 0;
        openCamera(cameraID);
        startPreview(surfaceTexture);
    }

    private static void setDefaultParameters(){
        Parameters parameters = camera.getParameters();
        if (parameters.getSupportedFocusModes().contains(
                Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
            parameters.setFocusMode(Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        }
        Size previewSize = getCeilPreviewSize(camera, screenPreviewWidth, screenPreviewHeight);
        parameters.setPreviewSize(previewSize.width, previewSize.height);
        Size pictureSize = getCeilPictureSize(camera, screenPreviewWidth, screenPreviewHeight);
        parameters.setPictureSize(pictureSize.width, pictureSize.height);
        parameters.setRotation(180);
        camera.setDisplayOrientation(180);
        camera.setParameters(parameters);
    }

    private static Size getPreviewSize(){
        return camera.getParameters().getPreviewSize();
    }

    private static Size getPictureSize(){
        return camera.getParameters().getPictureSize();
    }

    public static void startPreview(SurfaceTexture surfaceTexture){
        if(camera != null)
            try {
                camera.setPreviewTexture(surfaceTexture);
                CameraEngine.surfaceTexture = surfaceTexture;
                camera.startPreview();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    public static void startPreview(){
        if(camera != null)
            camera.startPreview();
    }

    public static void stopPreview(){
        camera.stopPreview();
    }

    public static void setRotation(int rotation){
        Camera.Parameters params = camera.getParameters();
        params.setRotation(rotation);
        camera.setParameters(params);
    }

    public static void takePicture(Camera.ShutterCallback shutterCallback, Camera.PictureCallback rawCallback,
                                   Camera.PictureCallback jpegCallback){
        camera.takePicture(shutterCallback, rawCallback, jpegCallback);
    }

    public static WCameraInfo getCameraInfo(){
        WCameraInfo info = new WCameraInfo();
        Size size = getPreviewSize();
        CameraInfo cameraInfo = new CameraInfo();
        Camera.getCameraInfo(cameraID, cameraInfo);
        info.previewWidth = size.width;
        info.previewHeight = size.height;
        info.orientation = cameraInfo.orientation;
        info.isFront = cameraID == 1 ? true : false;
        size = getPictureSize();
        info.pictureWidth = size.width;
        info.pictureHeight = size.height;
        return info;
    }


    public static Camera.Size getLargePictureSize(Camera camera){
        if(camera != null){
            List<Size> sizes = camera.getParameters().getSupportedPictureSizes();
            Camera.Size temp = sizes.get(0);
            for(int i = 1;i < sizes.size();i ++){
                float scale = (float)(sizes.get(i).height) / sizes.get(i).width;
                if(temp.width < sizes.get(i).width)
                    temp = sizes.get(i);
            }
            return temp;
        }
        return null;
    }

    public static Camera.Size getCeilPictureSize(Camera camera, int width, int height){
        if(camera != null){
            List<Camera.Size> sizes = camera.getParameters().getSupportedPictureSizes();
            Camera.Size ret = null;
            for(int i=0; i<sizes.size(); i++) {
                Camera.Size temp = sizes.get(i);
                if (temp.width >= width && temp.height >= height) {
                    if (ret == null) {
                        ret = temp;
                    } else {
                        if (ret.width * ret.height > temp.width*temp.height) {
                            ret = temp;
                        }
                    }
                }
            }
            if (ret == null) return getLargePictureSize(camera);

            return ret;
        }
        return null;
    }

    public static Camera.Size getLargePreviewSize(Camera camera){
        if(camera != null){
            List<Camera.Size> sizes = camera.getParameters().getSupportedPreviewSizes();
            Camera.Size temp = sizes.get(0);
            for(int i = 1;i < sizes.size();i ++){
                if(temp.width < sizes.get(i).width)
                    temp = sizes.get(i);
            }
            return temp;
        }
        return null;
    }

    public static Camera.Size getCeilPreviewSize(Camera camera, int width, int height){
        if(camera != null){
            List<Camera.Size> sizes = camera.getParameters().getSupportedPreviewSizes();
            Camera.Size ret = null;
            for(int i=0; i<sizes.size(); i++) {
                Camera.Size temp = sizes.get(i);
                if (temp.width >= width && temp.height >= height) {
                    if (ret == null) {
                        ret = temp;
                    } else {
                        if (ret.width * ret.height > temp.width*temp.height) {
                            ret = temp;
                        }
                    }
                }
            }
            if (ret == null) return getLargePreviewSize(camera);

            return ret;
        }
        return null;
    }
}