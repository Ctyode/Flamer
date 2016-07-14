package org.flamie.flamethrower;

import android.hardware.Camera;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ImageSave {

    public static final int MEDIA_TYPE_IMAGE = 1;

    public static final String TAG = "ImageSave";

    public static File getOutputMediaFile(int type) {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Flamethrower");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(TAG, "Oops! Failed create directory");
                return null;
            }
        }

        File mediaFile;
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        if (type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "Jackal_"+ timeStamp + ".jpg");
        } else {
            return null;
        }
        return mediaFile;
    }

}
