package org.flamie.flamethrower.ui.objects.buttons;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.View;

import org.flamie.flamethrower.R;

public class ButtonAccept extends View {

    private Drawable acceptDrawable;

    public ButtonAccept(Context context) {
        super(context);
        acceptDrawable = ContextCompat.getDrawable(getContext(), R.drawable.btn_done);
        acceptDrawable.setBounds(0, 0, acceptDrawable.getIntrinsicWidth(), acceptDrawable.getIntrinsicHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        acceptDrawable.draw(canvas);
    }

    @Override
    public void onMeasure(int measureWidthSpec, int measureHeightSpec) {
        if(acceptDrawable != null) {
            setMeasuredDimension(acceptDrawable.getIntrinsicWidth(), acceptDrawable.getIntrinsicHeight());
        }
    }


}
