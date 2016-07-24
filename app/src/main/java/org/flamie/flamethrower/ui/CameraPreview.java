package org.flamie.flamethrower.ui;

import android.content.Context;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import org.flamie.flamethrower.CameraController;
import org.flamie.flamethrower.util.PreviewUtils;

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback,
        CameraController.BeforeCameraCallback,
                                                          CameraController.AfterCameraCallback {

    private CameraController cameraController;
    private int displayRotation;

    public CameraPreview(Context context, CameraController cameraController, int displayRotation) {
        super(context);
        this.cameraController = cameraController;
        this.displayRotation = displayRotation;

        SurfaceHolder mHolder = getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        cameraController.beforeCamera(this);
        cameraController.afterCamera(this);
    }

    @Override
    public void afterCamera(Camera camera) {
        try {
            if(camera != null) {
                camera.setPreviewDisplay(getHolder());
                camera.startPreview();
                camera.setDisplayOrientation(PreviewUtils.cameraRotation(cameraController.getCameraInfo(),
                                                                         displayRotation));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void beforeCamera(Camera camera) {
        if(camera != null) {
            camera.stopPreview();
            getHolder().removeCallback(this);
            camera.setPreviewCallback(null);
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            Camera camera = cameraController.getCamera();
            if(camera != null) {
                camera.setPreviewDisplay(getHolder());
                camera.startPreview();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if(getHolder().getSurface() != null) {
            Camera camera = cameraController.getCamera();
            if(camera != null) {
                try {
                    camera.stopPreview();
                } catch (Exception e) {
                    // ignore
                }

                try {
                    camera.setPreviewDisplay(getHolder());
                    camera.startPreview();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Camera camera = cameraController.getCamera();
        if (camera != null) {
            camera.stopPreview();
            getHolder().removeCallback(this);
            camera.setPreviewCallback(null);
        }
    }
}
