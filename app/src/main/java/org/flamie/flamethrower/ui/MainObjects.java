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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import org.flamie.flamethrower.CameraController;
import org.flamie.flamethrower.OnSwipeTouchListener;
import org.flamie.flamethrower.ui.objects.BottomPanel;
import org.flamie.flamethrower.ui.objects.buttons.ButtonAccept;
import org.flamie.flamethrower.ui.objects.buttons.ButtonCapture;
import org.flamie.flamethrower.ui.objects.buttons.ButtonChange;
import org.flamie.flamethrower.ui.objects.buttons.ButtonDecline;
import org.flamie.flamethrower.ui.objects.buttons.ButtonPlay;
import org.flamie.flamethrower.ui.objects.buttons.FlashButtonAuto;
import org.flamie.flamethrower.ui.objects.buttons.FlashButtonOff;
import org.flamie.flamethrower.ui.objects.buttons.FlashButtonOn;
import org.flamie.flamethrower.util.ImageSaveUtils;
import org.flamie.flamethrower.util.PreviewUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.flamie.flamethrower.util.DimenUtils.dp;

public class MainObjects extends RelativeLayout implements Camera.PictureCallback {

    // TODO: исправить дерьмовую архитектуру, утечки памяти, убиться и не кодить никогда больше

    private static final String TAG = "MainObjects";
    private byte[] data;

    private CameraController cameraController;
    private Activity activity;
    private CameraPreview mPreview;
    private MediaRecorder mediaRecorder;

    private FlashButtonAuto flashButtonAuto;
    private FlashButtonOn flashButtonOn;
    private FlashButtonOff flashButtonOff;
    private BottomPanel confirmationPanel;
    private ImageView photoPreview;
    private ButtonAccept buttonAccept;
    private ButtonDecline buttonDecline;
    private boolean isRecording = false;
    public static boolean videoMode = false;
    private boolean isFront = false;
    private boolean isPlaying = false;
    private Bitmap bitmap;
    private Bitmap newBitmap;
    private boolean flashModeAuto = true;
    private boolean flashModeOn = false;
    private boolean flashModeOff = false;
    private ButtonPlay buttonPlay;
    private VideoView videoView;

    public MainObjects(Context context, Activity activity, CameraController cameraController) {
        super(context);
        this.activity = activity;
        this.cameraController = cameraController;

        mPreview = new CameraPreview(activity.getApplicationContext(), cameraController,
                                     activity.getWindowManager().getDefaultDisplay().getRotation());
        cameraController.onPicture(this);
        init();
    }

    private void init() {
        flashButtonAuto = new FlashButtonAuto(getContext());
        flashButtonOn = new FlashButtonOn(getContext());
        flashButtonOff = new FlashButtonOff(getContext());
        confirmationPanel = new BottomPanel(getContext());
        photoPreview = new ImageView(getContext());
        photoPreview.setBackgroundColor(Color.rgb(0, 0, 0));
        buttonAccept = new ButtonAccept(getContext());
        buttonDecline = new ButtonDecline(getContext());
        buttonPlay = new ButtonPlay(getContext());
        videoView = new VideoView(getContext());
        videoView.setZOrderMediaOverlay(true);

        final BottomPanel bottomPanel = new BottomPanel(getContext());
        final ButtonCapture buttonCapture = new ButtonCapture(getContext());
        final ButtonChange buttonChange = new ButtonChange(getContext());

        confirmationPanel.setVisibility(INVISIBLE);
        photoPreview.setVisibility(INVISIBLE);
        buttonAccept.setVisibility(INVISIBLE);
        buttonDecline.setVisibility(INVISIBLE);
        flashButtonOff.setVisibility(INVISIBLE);

        LayoutParams flashAutoLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        LayoutParams flashOnLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        LayoutParams flashOffLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        LayoutParams photoPreviewParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        final LayoutParams captureButtonParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        LayoutParams acceptButtonParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        LayoutParams declineButtonParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        LayoutParams buttonChangeParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        LayoutParams bottomPanelParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        LayoutParams previewParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        LayoutParams playButtonParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

        flashAutoLayoutParams.addRule(ALIGN_PARENT_RIGHT);
        flashAutoLayoutParams.addRule(ALIGN_PARENT_TOP);
        flashAutoLayoutParams.topMargin = dp(-20);
        flashAutoLayoutParams.rightMargin = dp(10);

        flashOnLayoutParams.addRule(ALIGN_PARENT_RIGHT);
        flashOnLayoutParams.addRule(ALIGN_PARENT_TOP);
        flashOnLayoutParams.topMargin = dp(-20);
        flashOnLayoutParams.rightMargin = dp(10);

        flashOffLayoutParams.addRule(ALIGN_PARENT_RIGHT);
        flashOffLayoutParams.addRule(ALIGN_PARENT_TOP);
        flashOffLayoutParams.topMargin = dp(-20);
        flashOffLayoutParams.rightMargin = dp(10);

        playButtonParams.addRule(ALIGN_PARENT_BOTTOM);
        playButtonParams.addRule(CENTER_HORIZONTAL);
        playButtonParams.bottomMargin = dp(30);

        photoPreviewParams.addRule(ALIGN_PARENT_TOP);
        photoPreview.setScaleType(ImageView.ScaleType.FIT_START);
        buttonChangeParams.addRule(ALIGN_PARENT_BOTTOM);
        buttonChangeParams.addRule(ALIGN_PARENT_LEFT);
        buttonChangeParams.bottomMargin = dp(30);
        buttonChangeParams.leftMargin = dp(35);

        captureButtonParams.addRule(ALIGN_PARENT_BOTTOM);
        captureButtonParams.addRule(CENTER_HORIZONTAL);
        captureButtonParams.bottomMargin = dp(15);

        acceptButtonParams.addRule(ALIGN_PARENT_BOTTOM);
        acceptButtonParams.addRule(ALIGN_PARENT_RIGHT);
        acceptButtonParams.bottomMargin = dp(25);
        acceptButtonParams.rightMargin = dp(38);

        declineButtonParams.addRule(ALIGN_PARENT_BOTTOM);
        declineButtonParams.addRule(ALIGN_PARENT_LEFT);
        declineButtonParams.bottomMargin = dp(25);
        declineButtonParams.leftMargin = dp(0);

        flashButtonOn.setVisibility(INVISIBLE);
        flashButtonOff.setVisibility(INVISIBLE);
        buttonPlay.setVisibility(INVISIBLE);
        videoView.setVisibility(INVISIBLE);

        flashButtonAuto.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                flashButtonOn.setVisibility(VISIBLE);
                flashButtonAuto.getSpringOpacity().setEndValue(0);
                flashButtonAuto.getSpringFlashAuto().setEndValue(200);
                flashButtonOn.getSpringFlashOn().setEndValue(100);
                flashButtonOn.getSpringOpacity().setEndValue(255);
                flashButtonOff.getSpringFlashOff().setEndValue(0);
                flashButtonAuto.setVisibility(INVISIBLE);
                flashModeAuto = false;
                flashModeOn = true;
                flashModeOff = false;
            }
        });

        flashButtonOn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                flashButtonOff.setVisibility(VISIBLE);
                flashButtonOn.getSpringOpacity().setEndValue(0);
                flashButtonOn.getSpringFlashOn().setEndValue(200);
                flashButtonOff.getSpringFlashOff().setEndValue(100);
                flashButtonOff.getSpringOpacity().setEndValue(255);
                flashButtonAuto.getSpringFlashAuto().setEndValue(0);
                flashButtonOn.setVisibility(INVISIBLE);
                flashModeOn = false;
                flashModeAuto = false;
                flashModeOff = true;
            }
        });

        flashButtonOff.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                flashButtonAuto.setVisibility(VISIBLE);
                flashButtonOff.getSpringOpacity().setEndValue(0);
                flashButtonOff.getSpringFlashOff().setEndValue(200);
                flashButtonAuto.getSpringFlashAuto().setEndValue(100);
                flashButtonAuto.getSpringOpacity().setEndValue(255);
                flashButtonOn.getSpringFlashOn().setEndValue(0);
                flashButtonOff.setVisibility(INVISIBLE);
                flashModeOn = false;
                flashModeAuto = true;
                flashModeOff = false;
            }
        });


        buttonCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(videoMode) {
                    if(isRecording) {
                        onClickStopRecord();
                        buttonCapture.getSpringOuterX().setEndValue(130f);
                        buttonCapture.getSpringOuterY().setEndValue(100f);
                        buttonCapture.getSpringBigRecord().setEndValue(0f);
                        buttonCapture.getSpringRectangleRecord().setEndValue(0f);
                        buttonPlay.setVisibility(VISIBLE);
                        buttonChange.setVisibility(INVISIBLE);
                        buttonCapture.setVisibility(INVISIBLE);
                        buttonAccept.setVisibility(VISIBLE);
                        buttonDecline.setVisibility(VISIBLE);
                        videoView.setVisibility(VISIBLE);
                        videoView.setVideoPath(ImageSaveUtils.mediaFile.getAbsolutePath());
                        videoView.setMediaController(null);
                        isRecording = false;
                    } else {
                        onClickStartRecord();
                        buttonCapture.getSpringOuterX().setEndValue(0f);
                        buttonCapture.getSpringOuterY().setEndValue(0f);
                        buttonCapture.getSpringBigRecord().setEndValue(100f);
                        buttonCapture.getSpringRectangleRecord().setEndValue(40f);
                        isRecording = true;
                    }
                } else {
                    if(cameraController.getCameraId() == Camera.CameraInfo.CAMERA_FACING_BACK) {
                        if (flashModeOff) {
                            Camera.Parameters parameters = cameraController.getCamera().getParameters();
                            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                            cameraController.getCamera().setParameters(parameters);
                        } else if (flashModeOn) {
                            Camera.Parameters parameters = cameraController.getCamera().getParameters();
                            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
                            cameraController.getCamera().setParameters(parameters);
                        } else if (flashModeAuto) {
                            Camera.Parameters parameters = cameraController.getCamera().getParameters();
                            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
                            cameraController.getCamera().setParameters(parameters);
                        }
                    }
                    cameraController.requireCameraPicture();
                }
            }
         });

        buttonChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isFront) {
                    buttonChange.getSpringStroke().setEndValue(dp(2));
                    buttonChange.getSpringRotate().setEndValue(180);
                    buttonChange.getSpringRadius().setEndValue(dp(7));
                    isFront = false;
                } else {
                    buttonChange.getSpringStroke().setEndValue(dp(9));
                    buttonChange.getSpringRotate().setEndValue(0);
                    buttonChange.getSpringRadius().setEndValue(dp(3));
                    isFront = true;
                }
                cameraController.requireCameraCycle();
            }
         });

        buttonAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // DA
                if(videoMode) {
                    buttonPlay.setVisibility(INVISIBLE);
                    videoView.setVisibility(INVISIBLE);
                } else {
                    bitmap.recycle();
                    newBitmap.recycle();
                    System.gc();
                }
                ImageSaveUtils.saveImage(data);
                cameraController.getCamera().startPreview();
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
                if(videoMode) {
                    buttonPlay.setVisibility(INVISIBLE);
                    videoView.setVisibility(INVISIBLE);
                    buttonCapture.setVisibility(VISIBLE);
                    buttonChange.setVisibility(VISIBLE);
                } else {
                    bitmap.recycle();
                    newBitmap.recycle();
                    System.gc();
                }
                cameraController.getCamera().startPreview();
                confirmationPanel.setVisibility(INVISIBLE);
                photoPreview.setVisibility(INVISIBLE);
                buttonAccept.setVisibility(INVISIBLE);
                buttonDecline.setVisibility(INVISIBLE);
            }
        });

        mPreview.setOnTouchListener(new OnSwipeTouchListener(activity) {
            public void onSwipeLeft() {
                bottomPanel.getSpringOpacity().setEndValue(110);
                buttonCapture.getSpringOuterX().setEndValue(130f);
                buttonCapture.getSpringInner().setEndValue(0f);
                buttonCapture.getSpringCentral().setEndValue(0f);
                buttonCapture.getSpringSmallRecord().setEndValue(20f);
                videoMode = true;
            }

            public void onSwipeRight() {
                bottomPanel.getSpringOpacity().setEndValue(255);
                buttonCapture.getSpringOuterX().setEndValue(100f);
                buttonCapture.getSpringInner().setEndValue(90f);
                buttonCapture.getSpringCentral().setEndValue(60f);
                buttonCapture.getSpringSmallRecord().setEndValue(0f);
                videoMode = false;
            }
        });

        buttonPlay.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isPlaying) {
                    videoView.pause();
                    isPlaying = false;
                } else {
                    videoView.start();
                    isPlaying = true;
                }
            }
        });


        flashButtonAuto.setLayoutParams(flashAutoLayoutParams);
        flashButtonOn.setLayoutParams(flashOnLayoutParams);
        flashButtonOff.setLayoutParams(flashOffLayoutParams);

        buttonAccept.setLayoutParams(acceptButtonParams);
        buttonDecline.setLayoutParams(declineButtonParams);
        photoPreview.setLayoutParams(photoPreviewParams);
        buttonCapture.setLayoutParams(captureButtonParams);
        bottomPanel.setLayoutParams(bottomPanelParams);
        buttonChange.setLayoutParams(buttonChangeParams);
        buttonPlay.setLayoutParams(playButtonParams);
        videoView.setLayoutParams(photoPreviewParams);
        setLayoutParams(previewParams);

        addView(mPreview);
        addView(bottomPanel);
        addView(buttonChange);
        addView(flashButtonOff);
        addView(flashButtonOn);
        addView(flashButtonAuto);
        addView(buttonCapture);

        addView(photoPreview);
        addView(videoView);
        addView(confirmationPanel);
        addView(buttonAccept);
        addView(buttonDecline);
        addView(buttonPlay);
    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        this.data = data;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inSampleSize = 3;
        bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options);

        Matrix matrix = new Matrix();
        if (cameraController.getCameraInfo().facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            float[] mirrorY = { -1, 0, 0, 0, 1, 0, 0, 0, 1 };
            Matrix matrixMirrorY = new Matrix();
            matrixMirrorY.setValues(mirrorY);
            matrix.postConcat(matrixMirrorY);
        }
        matrix.postRotate(PreviewUtils.cameraRotation(cameraController.getCameraInfo(),
                          activity.getWindowManager().getDefaultDisplay().getRotation()));

        newBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        newBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        this.data = stream.toByteArray();
        photoPreview.setImageBitmap(newBitmap);

        confirmationPanel.setVisibility(VISIBLE);
        photoPreview.setVisibility(VISIBLE);
        buttonAccept.setVisibility(VISIBLE);
        buttonDecline.setVisibility(VISIBLE);
    }

    public void onClickStartRecord() {
        if (prepareVideoRecorder()) {
            mediaRecorder.start();
            isRecording = true;
        } else {
            releaseMediaRecorder();
        }
    }

    public void onClickStopRecord() {
        if (mediaRecorder != null) {
            mediaRecorder.stop();
            cameraController.getCamera().stopPreview();
            cameraController.requireCameraReconnect();
            isRecording = false;
            releaseMediaRecorder();
        }
    }

    private boolean prepareVideoRecorder() {
        cameraController.getCamera().setDisplayOrientation(PreviewUtils.cameraRotation(cameraController.getCameraInfo(),
                                                           activity.getWindowManager().getDefaultDisplay().getRotation()));
        mediaRecorder = new MediaRecorder();
        cameraController.requireCameraUnlock();
        mediaRecorder.setCamera(cameraController.getCamera());
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        mediaRecorder.setProfile(CamcorderProfile.get(cameraController.getCameraId(), CamcorderProfile.QUALITY_HIGH));
        mediaRecorder.setOutputFile(ImageSaveUtils.getOutputMediaFile(2).toString());
        mediaRecorder.setPreviewDisplay(mPreview.getHolder().getSurface());

        if(cameraController.getCameraInfo().facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            mediaRecorder.setOrientationHint(PreviewUtils.cameraRotation(cameraController.getCameraInfo(),
                                             activity.getWindowManager().getDefaultDisplay().getRotation()) + 180);
        } else {
            mediaRecorder.setOrientationHint(PreviewUtils.cameraRotation(cameraController.getCameraInfo(),
                                             activity.getWindowManager().getDefaultDisplay().getRotation()));
        }
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
            cameraController.requireCameraUnlock();
        }
    }

}
