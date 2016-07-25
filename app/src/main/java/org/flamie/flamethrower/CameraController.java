package org.flamie.flamethrower;

import android.hardware.Camera;

import java.util.ArrayList;
import java.util.List;

public class CameraController extends Thread implements Camera.PictureCallback {

    public interface BeforeCameraCallback {
        void beforeCamera(Camera camera);
    }

    public interface AfterCameraCallback {
        void afterCamera(Camera camera);
    }

    private List<BeforeCameraCallback> beforeCameraCallbacks;
    private List<AfterCameraCallback> afterCameraCallbacks;
    private List<Camera.PictureCallback> onPictureCallbacks;

    private boolean cameraCycleRequired = false;
    private boolean cameraOpenRequired = false;
    private boolean cameraReleaseRequired = false;
    private boolean cameraPictureRequired = false;

    private Camera mCamera;
    private Camera.CameraInfo cameraInfo;
    private int cameraId;

    public CameraController() {
        beforeCameraCallbacks = new ArrayList<>();
        afterCameraCallbacks = new ArrayList<>();
        onPictureCallbacks = new ArrayList<>();
    }

    public void beforeCamera(BeforeCameraCallback beforeCameraCallback) {
        beforeCameraCallbacks.add(beforeCameraCallback);
    }

    public void afterCamera(AfterCameraCallback afterCameraCallback) {
        afterCameraCallbacks.add(afterCameraCallback);
    }

    public void onPicture(Camera.PictureCallback pictureCallback) {
        onPictureCallbacks.add(pictureCallback);
    }

    public void requireCameraOpen() {
        cameraOpenRequired = true;
    }

    public void requireCameraCycle() {
        cameraCycleRequired = true;
    }

    public void requireCameraRelease() {
        cameraReleaseRequired = true;
    }

    public void requireCameraPicture() {
        cameraPictureRequired = true;
    }

    @Override
    public void run() {
        for(;;) {
            if(cameraOpenRequired) {
                openCamera();
                cameraOpenRequired = false;
            } else if(cameraCycleRequired) {
                cameraCycle();
                cameraCycleRequired = false;
            } else if(cameraReleaseRequired) {
                releaseCamera();
                cameraReleaseRequired = false;
            } else if(cameraPictureRequired) {
                takePicture();
                cameraPictureRequired = false;
            }
        }
    }

    private synchronized void openCamera() {
        try {
            mCamera = Camera.open(cameraId);
            cameraInfo = new Camera.CameraInfo();
            Camera.getCameraInfo(cameraId, cameraInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }

        for(AfterCameraCallback callback : afterCameraCallbacks) {
            callback.afterCamera(mCamera);
        }
    }

    private void cameraCycle() {
        int newCameraId;
        if (cameraId == Camera.CameraInfo.CAMERA_FACING_BACK) {
            newCameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
        } else {
            newCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
        }

        for (BeforeCameraCallback callback : beforeCameraCallbacks) {
            callback.beforeCamera(mCamera);
        }

        if (mCamera != null) {
            mCamera.release();
        }
        mCamera = null;
        cameraInfo = null;
        cameraId = newCameraId;
        openCamera();
    }

    private void releaseCamera() {
        for (BeforeCameraCallback callback : beforeCameraCallbacks) {
            callback.beforeCamera(mCamera);
        }
        if(mCamera != null) {
            mCamera.release();
        }
        mCamera = null;
        cameraInfo = null;

        for(AfterCameraCallback callback : afterCameraCallbacks) {
            callback.afterCamera(mCamera);
        }
    }

    private void takePicture() {
        mCamera.takePicture(null, null, this);
    }

    public Camera getCamera() {
        return mCamera;
    }

    public int getCameraId() {
        return cameraId;
    }

    public Camera.CameraInfo getCameraInfo() {
        return cameraInfo;
    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        for (Camera.PictureCallback onPictureCallback : onPictureCallbacks) {
            onPictureCallback.onPictureTaken(data, camera);
        }
    }

}
