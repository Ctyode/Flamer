package org.flamie.flamethrower.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.flamie.flamethrower.CameraController;
import org.flamie.flamethrower.ui.MainObjects;
import org.flamie.flamethrower.util.DimenUtils;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private CameraController cameraController;
    private MainObjects mainObjects;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DimenUtils.init(getApplicationContext());
        cameraController = new CameraController();
        cameraController.start();
        mainObjects = new MainObjects(getApplicationContext(), this, cameraController);
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
