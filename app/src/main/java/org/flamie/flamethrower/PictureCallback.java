package org.flamie.flamethrower;

import android.hardware.Camera;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static org.flamie.flamethrower.ImageSave.getOutputMediaFile;

public class PictureCallback implements Camera.PictureCallback {

    private static final String TAG = "PictureCallback";
    private static final int MEDIA_TYPE_IMAGE = 1;

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
        camera.startPreview();
        if (pictureFile == null) {
            MainActivity.safeToTakePicture = true;
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
        MainActivity.safeToTakePicture = true;
    }

}
