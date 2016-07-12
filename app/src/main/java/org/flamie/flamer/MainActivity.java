package org.flamie.flamer;

import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static org.flamie.flamer.ImageSave.getOutputMediaFile;

public class MainActivity extends AppCompatActivity {

    private static final int MEDIA_TYPE_IMAGE = 1;
    private static final String TAG = "MainActivity";

    private Camera mCamera;
    private CameraPreview mPreview;
    private FrameLayout preview;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mCamera = getCameraInstance();
        mPreview = new CameraPreview(getApplicationContext(), mCamera, this);
        preview = (FrameLayout) findViewById(R.id.camera_preview);
        if (preview != null) {
            preview.addView(mPreview);
        }

        Button captureButton = (Button) findViewById(R.id.button_capture);
        if (captureButton != null) {
            captureButton.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     mCamera.takePicture(null, null, mPicture);
                 }
             });
        }

    }

    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

            File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
            camera.startPreview();
            if (pictureFile == null) {
                Log.d(TAG, "Error creating media file, check storage permissions: ");
                return;
            }

            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();
            } catch (FileNotFoundException e) {
                Log.d(TAG, "File not found: " + e.getMessage());
            } catch (IOException e) {
                Log.d(TAG, "Error accessing file: " + e.getMessage());
            }
        }

    };

    private synchronized Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open(0);
        } catch (Exception e) {
            Log.e(TAG, "Camera is not available");
        }
        return c;
    }

}
