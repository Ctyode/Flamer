package org.flamie.flamethrower;

import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

public class MainActivity extends AppCompatActivity implements CameraPreview.CameraListener {

    private static final String TAG = "MainActivity";

    private Camera mCamera;
    private CameraPreview mPreview;
    private PictureCallback mPicture;
    private FrameLayout preview;

    public static boolean safeToTakePicture = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mCamera = getCameraInstance();
        mPicture = new PictureCallback();
        mPreview = new CameraPreview(getApplicationContext(), mCamera, this, this);
        preview = (FrameLayout) findViewById(R.id.camera_preview);
        if (preview != null) {
            preview.addView(mPreview);
        }

        Button captureButton = (Button) findViewById(R.id.button_capture);
        if (captureButton != null) {
            captureButton.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     if (safeToTakePicture) {
                         mCamera.takePicture(null, null, mPicture);
                         safeToTakePicture = false;
                     }
                 }
             });
        }

        Button changeCamera = (Button) findViewById(R.id.button_change);

        if (changeCamera != null) {
            changeCamera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPreview.switchCamera();
                }
            });
        }

    }


    private synchronized Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open();
        } catch (Exception e) {
            Log.e(TAG, "Camera is not available");
            e.printStackTrace();
        }
        return c;
    }


    @Override
    public void onCamera(Camera camera) {
        mCamera = camera;
    }
}
