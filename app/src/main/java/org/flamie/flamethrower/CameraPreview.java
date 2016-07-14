package org.flamie.flamethrower;

import android.app.Activity;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.hardware.Camera;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.FrameLayout;

import java.io.IOException;
import java.util.List;
import java.util.Observer;

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {

    private SurfaceHolder mHolder;
    private Camera mCamera;
    private Activity activity;
    private CameraListener cameraListener;

    private static final String TAG = "CameraPreview";
//    private static final int CAMERA_ID = 0;
    private int cameraId;

    public interface CameraListener {
        void onCamera(Camera camera);
    }

    public CameraPreview(Context context, Camera camera, Activity activity, CameraListener cameraListener) {
        super(context);
        this.cameraListener = cameraListener;
        this.activity = activity;
        mCamera = camera;
        mHolder = getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public void surfaceCreated(SurfaceHolder holder) {
        try {
            mHolder.removeCallback(this);
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
            MainActivity.safeToTakePicture = true;
            setCameraDisplayOrientation(cameraId, mCamera);
            setPreviewSize(true);
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

    public void setCameraDisplayOrientation(int cameraId, Camera camera) {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0: degrees = 0; break;
            case Surface.ROTATION_90: degrees = 90; break;
            case Surface.ROTATION_180: degrees = 180; break;
            case Surface.ROTATION_270: degrees = 270; break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;
        } else {
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
    }

    public void setPreviewSize(boolean fullscreen) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        boolean widthIsMax = display.getWidth() > display.getHeight();
        Camera.Size size = mCamera.getParameters().getPreviewSize();

        RectF rectDisplay = new RectF();
        RectF rectPreview = new RectF();
        rectDisplay.set(0, 0, display.getWidth(), display.getHeight());

        if (widthIsMax) {
            rectPreview.set(0, 0, size.width, size.height);
        } else {
            rectPreview.set(0, 0, size.height, size.width);
        }

        Matrix matrix = new Matrix();
        if (!fullscreen) {
            matrix.setRectToRect(rectPreview, rectDisplay, Matrix.ScaleToFit.START);
        } else {
            matrix.setRectToRect(rectDisplay, rectPreview, Matrix.ScaleToFit.START);
            matrix.invert(matrix);
        }
        matrix.mapRect(rectPreview);

        getLayoutParams().height = (int) (rectPreview.bottom);
        getLayoutParams().width = (int) (rectPreview.right);
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
            setCameraDisplayOrientation(cameraId, mCamera);
//            setPreviewSize(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}