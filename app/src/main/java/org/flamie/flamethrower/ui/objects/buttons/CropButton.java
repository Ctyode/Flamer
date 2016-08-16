package org.flamie.flamethrower.ui.objects.buttons;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.View;

import org.flamie.flamethrower.R;

public class CropButton extends View {

    private Drawable cropDrawable;

    public CropButton(Context context) {
        super(context);

        cropDrawable = ContextCompat.getDrawable(getContext(), R.drawable.crop);
        cropDrawable.setBounds(0, 0, cropDrawable.getIntrinsicWidth(), cropDrawable.getIntrinsicHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        cropDrawable.draw(canvas);
    }

    @Override
    public void onMeasure(int measureWidthSpec, int measureHeightSpec) {
        if(cropDrawable != null) {
            setMeasuredDimension(cropDrawable.getIntrinsicWidth(), cropDrawable.getIntrinsicHeight());
        }
    }


}
