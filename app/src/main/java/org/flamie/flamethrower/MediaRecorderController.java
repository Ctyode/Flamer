package org.flamie.flamethrower;

import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.view.SurfaceView;

import org.flamie.flamethrower.util.ImageSaveUtils;

import java.io.IOException;

public class MediaRecorderController extends Thread implements CameraController.BeforeUnlockCallback,
                                                               CameraController.AfterUnlockCallback {

    private int orientationHint;

    private boolean recordStartRequired;
    private boolean stopRecordRequired;
    private boolean captureCameraAfterCameraUnlock;

    private CameraController cameraController;
    private MediaRecorder mediaRecorder;
    private SurfaceView surfaceView;

    public MediaRecorderController(CameraController cameraController, SurfaceView surfaceView) {
        this.cameraController = cameraController;
        this.surfaceView = surfaceView;
        cameraController.beforeUnlock(this);
        cameraController.afterUnlock(this);
        mediaRecorder = new MediaRecorder();
    }

    @Override
    public void run() {
        for(;;) {
            try {
                Thread.sleep(60);
                if(recordStartRequired) {
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

    public void requireStartRecord() {
        recordStartRequired = true;
    }

    public void requireStopRecord() {
        stopRecordRequired = true;
    }

    @Override
    public void beforeUnlock(Camera camera) {
        if(recordStartRequired)
        cameraController.getCamera().setDisplayOrientation(orientationHint);
    }

    @Override
    public void afterUnlock(Camera camera) {
        if(recordStartRequired) {
            prepareVideoRecorder();
        }
    }

    private void startRecord() {
        try {
            if (prepareVideoRecorder()) {
                mediaRecorder.start();
            } else {
                releaseMediaRecorder();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void stopRecord() {
        if (mediaRecorder != null) {
            mediaRecorder.stop();
            cameraController.getCamera().stopPreview();
            cameraController.requireCameraReconnect();
            releaseMediaRecorder();
        }
    }

    private void releaseMediaRecorder() {
        if (mediaRecorder != null) {
            mediaRecorder.reset();
            mediaRecorder.release();
            mediaRecorder = null;
            cameraController.requireCameraUnlock();
        }
    }

    public boolean prepareVideoRecorder() {
        cameraController.requireCameraUnlock();
        mediaRecorder.setCamera(cameraController.getCamera());
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        mediaRecorder.setProfile(CamcorderProfile.get(cameraController.getCameraId(), CamcorderProfile.QUALITY_HIGH));
        mediaRecorder.setOutputFile(ImageSaveUtils.getOutputMediaFile(2).toString());
        mediaRecorder.setPreviewDisplay(surfaceView.getHolder().getSurface());
        setOrientationHint(orientationHint);
        try {
            mediaRecorder.prepare();
        } catch (IllegalStateException e) {
            releaseMediaRecorder();
            return false;
        } catch (IOException e) {
            releaseMediaRecorder();
            return false;
        }
        return true;
    }

    public void setOrientationHint(int rotation) {
        this.orientationHint = rotation;
    }

    public void captureCameraAfterCameraUnlock() {
        captureCameraAfterCameraUnlock = true;
    }

}
