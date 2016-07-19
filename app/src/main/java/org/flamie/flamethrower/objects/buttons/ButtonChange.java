package org.flamie.flamethrower.objects.buttons;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.View;

import org.flamie.flamethrower.R;

public class ButtonChange extends View {

    private final Drawable switchDrawable;
    private int switchIconX = 125;
    private int switchIconY = 1500;

    public ButtonChange(Context context) {
        super(context);
        switchDrawable = ContextCompat.getDrawable(getContext(), R.drawable.switcher);
        switchDrawable.setBounds(switchIconX, switchIconY, switchIconX + switchDrawable.getIntrinsicWidth(),
                                                           switchIconY + switchDrawable.getIntrinsicHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        switchDrawable.draw(canvas);
    }

    @Override
    public void onMeasure(int measureWidthSpec, int measureHeightSpec) {
        if(switchDrawable != null)
            setMeasuredDimension(switchIconX + switchDrawable.getIntrinsicWidth(), switchIconY + switchDrawable.getIntrinsicHeight());
    }

}
