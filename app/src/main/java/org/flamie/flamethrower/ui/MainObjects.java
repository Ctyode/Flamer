package org.flamie.flamethrower.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import org.flamie.flamethrower.callback.PictureCallback;
import org.flamie.flamethrower.callback.PreviewCallback;
import org.flamie.flamethrower.objects.BottomPanel;
import org.flamie.flamethrower.objects.buttons.ButtonAccept;
import org.flamie.flamethrower.objects.buttons.ButtonCapture;
import org.flamie.flamethrower.objects.buttons.ButtonChange;
import org.flamie.flamethrower.objects.buttons.ButtonDecline;
import org.flamie.flamethrower.util.ImageSave;
import org.flamie.flamethrower.util.PreviewUtils;

import java.io.File;
import java.io.IOException;

public class MainObjects extends RelativeLayout implements PreviewCallback.CameraListener {

    private static final String TAG = "MainObjects";
    public static boolean safeToTakePicture = false;
    private byte[] data;

    private Activity activity;
    private PreviewCallback mPreview;
    private PictureCallback mPicture;
    private Camera mCamera;
    private PreviewUtils previewUtils;
    private MediaRecorder mediaRecorder;
    private File videoFile;

    private BottomPanel confirmationPanel;
    private ImageView photoPreview;
    private ButtonAccept buttonAccept;
    private ButtonDecline buttonDecline;
    private boolean isRecord = false;

    public MainObjects(Context context, Activity activity) {
        super(context);
        this.activity = activity;

        previewUtils = new PreviewUtils(activity);
        mCamera = getCameraInstance();
        mPicture = new PictureCallback(this);
        mPreview = new PreviewCallback(activity.getApplicationContext(), mCamera, activity, this);
        init();
    }

    private void init() {
        Button videoButton = new Button(getContext());

        confirmationPanel = new BottomPanel(getContext());
        photoPreview = new ImageView(getContext());
        photoPreview.setBackgroundColor(Color.rgb(0, 0, 0));
        buttonAccept = new ButtonAccept(getContext());
        buttonDecline = new ButtonDecline(getContext());

        BottomPanel bottomPanel = new BottomPanel(getContext());
        ButtonCapture buttonCapture = new ButtonCapture(getContext());
        ButtonChange buttonChange = new ButtonChange(getContext());

        confirmationPanel.setVisibility(INVISIBLE);
        photoPreview.setVisibility(INVISIBLE);
        buttonAccept.setVisibility(INVISIBLE);
        buttonDecline.setVisibility(INVISIBLE);

        LayoutParams photoPreviewParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        LayoutParams captureButtonParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        LayoutParams buttonChangeParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        LayoutParams bottomPanelParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        LayoutParams previewParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

        photoPreviewParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        photoPreview.setScaleType(ImageView.ScaleType.FIT_START);

        videoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isRecord) {
                    onClickStopRecord();
                } else {
                    onClickStartRecord();
                }
            }
        });

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

        buttonAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // DA
                mPicture.saveImage(data, mCamera);
                confirmationPanel.setVisibility(INVISIBLE);
                photoPreview.setVisibility(INVISIBLE);
                buttonAccept.setVisibility(INVISIBLE);
                buttonDecline.setVisibility(INVISIBLE);
            }
        });

        buttonDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // NJET
                mCamera.startPreview();
                confirmationPanel.setVisibility(INVISIBLE);
                photoPreview.setVisibility(INVISIBLE);
                buttonAccept.setVisibility(INVISIBLE);
                buttonDecline.setVisibility(INVISIBLE);
                safeToTakePicture = true;
            }
        });

        photoPreview.setLayoutParams(photoPreviewParams);
        buttonCapture.setLayoutParams(captureButtonParams);
        bottomPanel.setLayoutParams(bottomPanelParams);
        buttonChange.setLayoutParams(buttonChangeParams);
        setLayoutParams(previewParams);

        addView(mPreview);
        addView(bottomPanel);
        addView(buttonCapture);
        addView(buttonChange);

        addView(photoPreview);
        addView(confirmationPanel);
        addView(buttonAccept);
        addView(buttonDecline);

        addView(videoButton);
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

    public void photoPreview(byte[] data) {
        this.data = data;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options);
        bitmap = rotateImage(bitmap, previewUtils.cameraRotation(mPreview.getCameraId()));

        photoPreview.setImageBitmap(bitmap);
        confirmationPanel.setVisibility(VISIBLE);
        photoPreview.setVisibility(VISIBLE);
        buttonAccept.setVisibility(VISIBLE);
        buttonDecline.setVisibility(VISIBLE);
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Bitmap retVal;

        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        retVal = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);

        return retVal;
    }

    public void onClickStartRecord() {
        if (prepareVideoRecorder()) {
            mediaRecorder.start();
            isRecord = true;
        } else {
            releaseMediaRecorder();
        }
    }

    public void onClickStopRecord() {
        if (mediaRecorder != null) {
            mediaRecorder.stop();
            isRecord = false;
            releaseMediaRecorder();
        }
    }

    private boolean prepareVideoRecorder() {
        mCamera = getCameraInstance();
        mCamera.setDisplayOrientation(previewUtils.cameraRotation(mPreview.getCameraId()));
        mediaRecorder = new MediaRecorder();
        mCamera.unlock();
        mediaRecorder.setCamera(mCamera);
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        mediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));
        mediaRecorder.setOutputFile(ImageSave.getOutputMediaFile(2).toString());
        mediaRecorder.setPreviewDisplay(mPreview.getHolder().getSurface());
        try {
            mediaRecorder.prepare();
        } catch (IllegalStateException e) {
            Log.d(TAG, "IllegalStateException preparing MediaRecorder: " + e.getMessage());
            releaseMediaRecorder();
            return false;
        } catch (IOException e) {
            Log.d(TAG, "IOException preparing MediaRecorder: " + e.getMessage());
            releaseMediaRecorder();
            return false;
        }
        return true;
    }

    private void releaseMediaRecorder() {
        if (mediaRecorder != null) {
            mediaRecorder.reset();
            mediaRecorder.release();
            mediaRecorder = null;
            mCamera.lock();
        }
    }

    @Override
    public void onCamera(Camera camera) {
        mCamera = camera;
    }

}
