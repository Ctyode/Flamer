package org.flamie.flamethrower.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.flamie.flamethrower.CameraController;
import org.flamie.flamethrower.ui.CameraPreview;
import org.flamie.flamethrower.ui.MainObjects;
import org.flamie.flamethrower.util.DimenUtils;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private CameraController cameraController;
    private MainObjects mainObjects;
    private CameraPreview mPreview;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DimenUtils.init(getApplicationContext());
        cameraController = new CameraController();

        mPreview = new CameraPreview(this.getApplicationContext(), cameraController,
                this.getWindowManager().getDefaultDisplay().getRotation());
        cameraController.setPreview(mPreview);
        cameraController.start();
        mainObjects = new MainObjects(getApplicationContext(), this, cameraController, mPreview);
        setContentView(mainObjects);
        if(getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        cameraController.requireCameraOpen();
    }

    @Override
    protected void onPause() {
        super.onPause();
        cameraController.requireCameraRelease();
    }

}
