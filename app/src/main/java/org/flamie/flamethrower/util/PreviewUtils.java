package org.flamie.flamethrower.util;

import android.hardware.Camera;
import android.view.Surface;

import java.util.List;

public class PreviewUtils {

    public static int cameraRotation(Camera.CameraInfo cameraInfo, int displayRotation) {
        int degrees = 0;
        switch (displayRotation) {
            case Surface.ROTATION_0: degrees = 0; break;
            case Surface.ROTATION_90: degrees = 90; break;
            case Surface.ROTATION_180: degrees = 180; break;
            case Surface.ROTATION_270: degrees = 270; break;
        }

        int result;
        if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (cameraInfo.orientation + degrees) % 360;
            result = (360 - result) % 360;
        } else {
            result = (cameraInfo.orientation - degrees + 360) % 360;
        }
        return result;
    }

    public static Camera.Size getOptimalPreviewSize(List<Camera.Size> sizes, int w, int h) {
        final double ASPECT_TOLERANCE = 0.1;
        double targetRatio = (double) h / w;

        if (sizes == null) return null;

        Camera.Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        int targetHeight = h;

        for (Camera.Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optimalSize;
    }

//    public void setPreviewSize(boolean fullscreen, PreviewCallback previewCallback, Camera camera) {
//        Display display = activity.getWindowManager().getDefaultDisplay();
//        boolean widthIsMax = display.getWidth() > display.getHeight();
//        Camera.Size size = camera.getParameters().getPreviewSize();
//
//        RectF rectDisplay = new RectF();
//        RectF rectPreview = new RectF();
//        rectDisplay.set(0, 0, display.getWidth(), display.getHeight());
//
//        if (widthIsMax) {
//            rectPreview.set(0, 0, size.width, size.height);
//        } else {
//            rectPreview.set(0, 0, size.height, size.width);
//        }
//
//        Matrix matrix = new Matrix();
//        if (!fullscreen) {
//            matrix.setRectToRect(rectPreview, rectDisplay, Matrix.ScaleToFit.START);
//        } else {
//            matrix.setRectToRect(rectDisplay, rectPreview, Matrix.ScaleToFit.START);
//            matrix.invert(matrix);
//        }
//        matrix.mapRect(rectPreview);
//
//        previewCallback.getLayoutParams().height = (int) (rectPreview.bottom);
//        previewCallback.getLayoutParams().width = (int) (rectPreview.right);
//    }

}
