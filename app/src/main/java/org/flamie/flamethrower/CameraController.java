package org.flamie.flamethrower;

import android.hardware.Camera;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CameraController extends Thread implements Camera.PictureCallback {

    public interface BeforeCameraCallback {
        void beforeCamera(Camera camera);
    }

    public interface AfterCameraCallback {
        void afterCamera(Camera camera);
    }

    public interface BeforeUnlockCallback {
        void beforeUnlock(Camera camera);
    }

    public interface AfterUnlockCallback {
        void afterUnlock(Camera camera);
    }

    public interface BeforeReconnectCallback {
        void beforeReconnect(Camera camera);
    }

    public interface AfterReconnectCallback {
        void afterReconnect(Camera camera);
    }

    private List<BeforeCameraCallback> beforeCameraCallbacks;
    private List<AfterCameraCallback> afterCameraCallbacks;
    private List<Camera.PictureCallback> onPictureCallbacks;
    private List<BeforeUnlockCallback> beforeUnlockCallbacks;
    private List<AfterUnlockCallback> afterUnlockCallbacks;
    private List<BeforeReconnectCallback> beforeReconnectCallbacks;
    private List<AfterReconnectCallback> afterReconnectCallbacks;

    private boolean cameraCycleRequired = false;
    private boolean cameraOpenRequired = false;
    private boolean cameraReleaseRequired = false;
    private boolean cameraPictureRequired = false;
    private boolean cameraUnlockRequired = false;
    private boolean cameraReconnectRequired = false;

    private Camera mCamera;
    private Camera.CameraInfo cameraInfo;
    private int cameraId;

    public CameraController() {
        beforeCameraCallbacks = new ArrayList<>();
        afterCameraCallbacks = new ArrayList<>();
        onPictureCallbacks = new ArrayList<>();
        beforeUnlockCallbacks = new ArrayList<>();
        afterUnlockCallbacks = new ArrayList<>();
        beforeReconnectCallbacks = new ArrayList<>();
        afterReconnectCallbacks = new ArrayList<>();
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

    public void requireCameraUnlock() {
        cameraUnlockRequired = true;
    }

    public void requireCameraReconnect() {
        cameraReconnectRequired = true;
    }

    @Override
    public void run() {
        for(;;) {
            try {
                Thread.sleep(60);
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
                } else if(cameraUnlockRequired) {
                    unlockCamera();
                    cameraUnlockRequired = false;
                } else if(cameraReconnectRequired) {
                    reconnectCamera();
                    cameraReconnectRequired = false;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
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

    private void unlockCamera() {
        for (BeforeUnlockCallback callback : beforeUnlockCallbacks) {
            callback.beforeUnlock(mCamera);
        }

        mCamera.unlock();

        for(AfterUnlockCallback callback : afterUnlockCallbacks) {
            callback.afterUnlock(mCamera);
        }
    }

    private void reconnectCamera() {
        for (BeforeReconnectCallback callback : beforeReconnectCallbacks) {
            callback.beforeReconnect(mCamera);
        }

        try {
            mCamera.reconnect();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for(AfterReconnectCallback callback : afterReconnectCallbacks) {
            callback.afterReconnect(mCamera);
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
