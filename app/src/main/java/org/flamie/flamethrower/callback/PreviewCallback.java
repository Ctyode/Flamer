package org.flamie.flamethrower.callback;

import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import org.flamie.flamethrower.ui.MainObjects;
import org.flamie.flamethrower.util.PreviewUtils;

import java.io.IOException;

public class PreviewCallback extends SurfaceView implements SurfaceHolder.Callback {

    // TODO: пофиксить нуллпоинтер при горизонтальной ориентации

    private SurfaceHolder mHolder;
    private Camera mCamera;
    private CameraListener cameraListener;
    private PreviewUtils previewUtils;
    private Activity activity;

    private static final String TAG = "CameraPreview";
    private int cameraId;

    public interface CameraListener {
        void onCamera(Camera camera);
    }

    public PreviewCallback(Context context, Camera camera, Activity activity, CameraListener cameraListener) {
        super(context);
        this.activity = activity;
        this.cameraListener = cameraListener;
        mCamera = camera;
        mHolder = getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        previewUtils = new PreviewUtils(activity);

    }

    public void surfaceCreated(SurfaceHolder holder) {
        try {
            mHolder.removeCallback(this);
            if(mCamera != null) {
                mCamera.setPreviewDisplay(holder);
                mCamera.startPreview();
                MainObjects.safeToTakePicture = true;
                mCamera.setDisplayOrientation(previewUtils.cameraRotation(cameraId));
                previewUtils.setPreviewSize(true, this, mCamera);
            }
        } catch (IOException e) {
            Log.d(TAG, "Error setting camera preview: " + e.getMessage());
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        if (mCamera != null) {
            mCamera.stopPreview();
        }
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        if (mHolder.getSurface() == null) {
            return;
        }
        try {
            mCamera.stopPreview();
        } catch (Exception e){
            e.printStackTrace();
        }
        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();
        } catch (Exception e){
            Log.d(TAG, "Error starting camera preview: " + e.getMessage());
        }
    }

    public void switchCamera() {
        if (mCamera != null) {
            mCamera.stopPreview();
            mHolder.removeCallback(this);
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
        }
        if (cameraId == Camera.CameraInfo.CAMERA_FACING_BACK) {
            cameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
        } else {
            cameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
        }
        try {
            mCamera = Camera.open(cameraId);
            cameraListener.onCamera(mCamera);
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();
            mCamera.setDisplayOrientation(previewUtils.cameraRotation(cameraId));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getCameraId() {
        return cameraId;
    }
}