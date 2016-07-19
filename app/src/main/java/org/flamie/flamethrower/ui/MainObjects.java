package org.flamie.flamethrower.ui;

import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import org.flamie.flamethrower.callback.PictureCallback;
import org.flamie.flamethrower.callback.PreviewCallback;
import org.flamie.flamethrower.objects.BottomPanel;
import org.flamie.flamethrower.objects.buttons.ButtonCapture;
import org.flamie.flamethrower.objects.buttons.ButtonChange;

public class MainObjects extends FrameLayout  implements PreviewCallback.CameraListener {

    private static final String TAG = "MainObjects";
    public static boolean safeToTakePicture = false;

    private Activity activity;
    private PreviewCallback mPreview;
    private PictureCallback mPicture;
    private Camera mCamera;

    public MainObjects(Context context, Activity activity) {
        super(context);
        this.activity = activity;

        mCamera = getCameraInstance();
        mPicture = new PictureCallback();
        mPreview = new PreviewCallback(activity.getApplicationContext(), mCamera, activity, this);
        init();
    }

    private void init() {
        BottomPanel bottomPanel = new BottomPanel(getContext());
        ButtonCapture buttonCapture = new ButtonCapture(getContext());
        ButtonChange buttonChange = new ButtonChange(getContext());

        LayoutParams captureButtonParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        LayoutParams buttonChangeParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        LayoutParams bottomPanelParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        LayoutParams previewParams = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);

        buttonCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (safeToTakePicture) {
                    mCamera.takePicture(null, null, mPicture);
                    safeToTakePicture = false;
                }
            }
        });

        buttonChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPreview.switchCamera();
            }
         });

        buttonCapture.setLayoutParams(captureButtonParams);
        bottomPanel.setLayoutParams(bottomPanelParams);
        buttonChange.setLayoutParams(buttonChangeParams);
        setLayoutParams(previewParams);

        addView(mPreview);
        addView(bottomPanel);
        addView(buttonCapture);
        addView(buttonChange);
    }

    public synchronized Camera getCameraInstance() {
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
