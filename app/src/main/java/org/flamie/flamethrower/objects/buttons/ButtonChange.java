package org.flamie.flamethrower.objects.buttons;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.View;

import org.flamie.flamethrower.R;

public class ButtonChange extends View {

    private Drawable switchDrawable;
    private Paint circlePaint;

    public ButtonChange(Context context) {
        super(context);
        circlePaint = new Paint();
        circlePaint.setAntiAlias(true);
        circlePaint.setStyle(Paint.Style.FILL);
        circlePaint.setColor(Color.WHITE);

        switchDrawable = ContextCompat.getDrawable(getContext(), R.drawable.switcher);
        switchDrawable.setBounds(0, 0, switchDrawable.getIntrinsicWidth(), switchDrawable.getIntrinsicHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        switchDrawable.draw(canvas);
    }

    @Override
    public void onMeasure(int measureWidthSpec, int measureHeightSpec) {
        if(switchDrawable != null)
            setMeasuredDimension(switchDrawable.getIntrinsicWidth(), switchDrawable.getIntrinsicHeight());
    }

}
