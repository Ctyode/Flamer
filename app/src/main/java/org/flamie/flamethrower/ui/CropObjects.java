package org.flamie.flamethrower.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Matrix;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.flamie.flamethrower.R;
import org.flamie.flamethrower.ui.objects.CropBottomPanel;
import org.flamie.flamethrower.ui.objects.CropRectangle;
import org.flamie.flamethrower.ui.objects.buttons.ButtonRotate;

import uk.co.senab.photoview.PhotoView;

import static org.flamie.flamethrower.util.DimenUtils.dp;

public class CropObjects extends RelativeLayout {

    private Activity activity;
    private PhotoView photoPreview;
    private static Matrix photoResultMatrix;

    public CropObjects(Context context, Activity activity) {
        super(context);
        this.activity = activity;
        init();
    }

    public void init() {
        this.setBackgroundColor(Color.BLACK);
        final Context context = activity.getApplication().getApplicationContext();

        photoPreview = new PhotoView(context);
//        photoPreview.setScaleType(ImageView.ScaleType.MATRIX);
        CropBottomPanel cropBottomPanel = new CropBottomPanel(context);
        final TextView cancelText = new TextView(context);
        final TextView resultCancelText = new TextView(context);
        TextView resetText = new TextView(context);
        TextView doneText = new TextView(context);
        final ButtonRotate buttonRotate = new ButtonRotate(context);
        final CropRectangle cropRectangle = new CropRectangle(context);
        photoResultMatrix = new Matrix();
        final ImageView photoResult = new ImageView(context);
        final CropBottomPanel resultBottomPanel = new CropBottomPanel(context);
        final TextView saveText = new TextView(context);
        photoResult.setBackgroundColor(Color.BLACK);
        photoResult.setVisibility(INVISIBLE);
        resultBottomPanel.setVisibility(INVISIBLE);
        saveText.setVisibility(INVISIBLE);
        resultCancelText.setVisibility(INVISIBLE);

        photoPreview.setImageBitmap(MainObjects.getBitmap());

        cancelText.setText(R.string.cancel_text);
        resultCancelText.setText(R.string.cancel_text);
        resetText.setText(R.string.reset_text);
        doneText.setText(R.string.done_text);
        saveText.setText(R.string.save_text);

        final LayoutParams photoPreviewParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        LayoutParams cancelTextParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        LayoutParams resetTextParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        LayoutParams doneTextParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        LayoutParams saveTextParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        LayoutParams buttonRotateParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

        photoPreviewParams.addRule(ALIGN_PARENT_TOP);
        photoPreviewParams.addRule(CENTER_HORIZONTAL);
        photoPreviewParams.height = dp(460);

        cancelTextParams.addRule(ALIGN_PARENT_BOTTOM);
        cancelTextParams.addRule(ALIGN_PARENT_LEFT);
        cancelTextParams.bottomMargin = dp(15);
        cancelTextParams.leftMargin = dp(15);

        resetTextParams.addRule(ALIGN_PARENT_BOTTOM);
        resetTextParams.addRule(ALIGN_PARENT_LEFT);
        resetTextParams.bottomMargin = dp(15);
        resetTextParams.leftMargin = dp(100);

        doneTextParams.addRule(ALIGN_PARENT_BOTTOM);
        doneTextParams.addRule(ALIGN_PARENT_RIGHT);
        doneTextParams.bottomMargin = dp(15);
        doneTextParams.rightMargin = dp(15);

        saveTextParams.addRule(ALIGN_PARENT_BOTTOM);
        saveTextParams.addRule(ALIGN_PARENT_RIGHT);
        saveTextParams.bottomMargin = dp(15);
        saveTextParams.rightMargin = dp(15);

        cancelText.setTextColor(Color.WHITE);
        resetText.setTextColor(Color.WHITE);
        doneText.setTextColor(Color.CYAN);
        saveText.setTextColor(Color.CYAN);

        buttonRotateParams.addRule(ALIGN_PARENT_BOTTOM);
        buttonRotateParams.addRule(ALIGN_PARENT_RIGHT);
        buttonRotateParams.bottomMargin = dp(60);
        buttonRotateParams.rightMargin = dp(10);

        buttonRotate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                photoResultMatrix.postRotate((float) -90, photoPreview.getDrawable().getBounds().width() / 2,
                                                          photoPreview.getDrawable().getBounds().height() / 2);
                photoPreview.setImageMatrix(photoResultMatrix);
                photoPreview.invalidate();
            }
        });

        cancelText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });

        doneText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                photoResult.setImageBitmap(cropRectangle.cropResult());
                photoResult.setLayoutParams(photoPreviewParams);
                photoResult.setVisibility(VISIBLE);
                resultBottomPanel.setVisibility(VISIBLE);
                saveText.setVisibility(VISIBLE);
                buttonRotate.setVisibility(INVISIBLE);
                resultCancelText.setVisibility(VISIBLE);
            }
        });

        photoPreview.setLayoutParams(photoPreviewParams);
        cancelText.setLayoutParams(cancelTextParams);
        resetText.setLayoutParams(resetTextParams);
        doneText.setLayoutParams(doneTextParams);
        saveText.setLayoutParams(saveTextParams);
        buttonRotate.setLayoutParams(buttonRotateParams);
        resultCancelText.setLayoutParams(cancelTextParams);

        addView(photoPreview);
        addView(cropRectangle);
        addView(cropBottomPanel);
        addView(cancelText);
        addView(resetText);
        addView(doneText);
        addView(buttonRotate);
        addView(photoResult);
        addView(resultBottomPanel);
        addView(resultCancelText);
        addView(saveText);
    }

    public static Matrix getResultMatrix() {
        return photoResultMatrix;
    }
}
