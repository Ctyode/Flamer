package org.flamie.flamethrower.ui.objects.buttons;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.View;

import org.flamie.flamethrower.R;

public class ButtonRotate extends View {

    private Drawable rotateDrawable;

    public ButtonRotate(Context context) {
        super(context);

        rotateDrawable = ContextCompat.getDrawable(getContext(), R.drawable.rotate);
        rotateDrawable.setBounds(0, 0, rotateDrawable.getIntrinsicWidth(), rotateDrawable.getIntrinsicHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        rotateDrawable.draw(canvas);
    }

    @Override
    public void onMeasure(int measureWidthSpec, int measureHeightSpec) {
        if(rotateDrawable != null) {
            setMeasuredDimension(rotateDrawable.getIntrinsicWidth(), rotateDrawable.getIntrinsicHeight());
        }
    }


}
