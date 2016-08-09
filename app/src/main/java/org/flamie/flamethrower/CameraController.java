package org.flamie.flamethrower;

import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.FileObserver;

import org.flamie.flamethrower.ui.CameraPreview;
import org.flamie.flamethrower.util.ImageSaveUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CameraController extends Thread implements Camera.PictureCallback, VideoFileCallback {

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

    private class VideoFileObserver extends FileObserver {

        private VideoFileCallback callback;

        public VideoFileObserver(String path, VideoFileCallback callback) {
            super(path, FileObserver.CLOSE_WRITE);
            this.callback = callback;
        }

        @Override
        public void onEvent(int event, String path) {
            callback.videoFile(path);
        }

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
    private boolean recordStartRequired = false;
    private boolean stopRecordRequired = false;

    private VideoFileCallback videoFileCallback;
    private VideoFileObserver observer;
    private CameraPreview mPreview;
    private MediaRecorder mediaRecorder;
    private Camera mCamera;
    private Camera.CameraInfo cameraInfo;
    private int orientationHint;
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

    public void requireStartRecord() {
        recordStartRequired = true;
    }

    public void requireStopRecord() {
        stopRecordRequired = true;
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
                } else if(recordStartRequired) {
                    startRecord();
                    recordStartRequired = false;
                } else if(stopRecordRequired) {
                    stopRecord();
                    stopRecordRequired = false;
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

    private void startRecord() {
        if (prepareVideoRecorder()) {
            mediaRecorder.start();
        } else {
            releaseMediaRecorder();
        }
    }

    private void stopRecord() {
        if (mediaRecorder != null) {
            mediaRecorder.stop();
            mCamera.stopPreview();
            reconnectCamera();
            releaseMediaRecorder();
        }
    }

    private void releaseMediaRecorder() {
        if (mediaRecorder != null) {
            mediaRecorder.reset();
            mediaRecorder.release();
            mediaRecorder = null;
            unlockCamera();
        }
    }

    public boolean prepareVideoRecorder() {
        mediaRecorder = new MediaRecorder();
        unlockCamera();
        mediaRecorder.setCamera(mCamera);
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        mediaRecorder.setProfile(CamcorderProfile.get(getCameraId(), CamcorderProfile.QUALITY_HIGH));
        mediaRecorder.setOutputFile(ImageSaveUtils.getOutputMediaFile(2).toString());
        mediaRecorder.setPreviewDisplay(mPreview.getHolder().getSurface());
        mediaRecorder.setOrientationHint(orientationHint);
        try {
            mediaRecorder.prepare();
        } catch (IllegalStateException e) {
            releaseMediaRecorder();
            return false;
        } catch (IOException e) {
            releaseMediaRecorder();
            return false;
        }
        observer = new VideoFileObserver(ImageSaveUtils.mediaFile.getAbsolutePath(), this);
        observer.startWatching();
        return true;
    }


    private void takePicture() {
        mCamera.takePicture(null, null, this);
    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        for (Camera.PictureCallback onPictureCallback : onPictureCallbacks) {
            onPictureCallback.onPictureTaken(data, camera);
        }
    }

    @Override
    public void videoFile(String path) {
        observer.stopWatching();
        videoFileCallback.videoFile(path);
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

    public void setOrientationHint(int rotation) {
        this.orientationHint = rotation;
    }

    public void setPreview(CameraPreview mPreview) {
        this.mPreview = mPreview;
    }

    public void setVideoFileCallback(VideoFileCallback videoFileCallback) {
        this.videoFileCallback = videoFileCallback;
    }

}
