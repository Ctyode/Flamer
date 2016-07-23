package org.flamie.flamethrower.callback;

import android.hardware.Camera;
import android.util.Log;

import org.flamie.flamethrower.ui.MainObjects;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static org.flamie.flamethrower.util.ImageSave.getOutputMediaFile;

public class PictureCallback implements Camera.PictureCallback {

    private static final String TAG = "PictureCallback";
    private static final int MEDIA_TYPE_IMAGE = 1;

    private MainObjects mainObjects;

    public PictureCallback(MainObjects mainObjects) {
        this.mainObjects = mainObjects;
    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        mainObjects.photoPreview(data);
    }

    public void saveImage(byte[] data, Camera camera) {
        File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
        camera.startPreview();
        if (pictureFile == null) {
            MainObjects.safeToTakePicture = true;
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
        MainObjects.safeToTakePicture = true;
    }


}
