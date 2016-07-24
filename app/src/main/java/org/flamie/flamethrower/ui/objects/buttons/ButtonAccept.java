package org.flamie.flamethrower.ui.objects.buttons;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.View;

import org.flamie.flamethrower.R;

public class ButtonAccept extends View {

    private Drawable acceptDrawable;
    private int acceptIconX = 800;
    private int acceptIconY = 1480;

    public ButtonAccept(Context context) {
        super(context);
        acceptDrawable = ContextCompat.getDrawable(getContext(), R.drawable.btn_done);
        acceptDrawable.setBounds(acceptIconX, acceptIconY, acceptIconX + acceptDrawable.getIntrinsicWidth(),
                acceptIconY + acceptDrawable.getIntrinsicHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        acceptDrawable.draw(canvas);
    }

    @Override
    public void onMeasure(int measureWidthSpec, int measureHeightSpec) {
        if(acceptDrawable != null) {
            setMeasuredDimension(acceptIconX + acceptDrawable.getIntrinsicWidth(), acceptIconY + acceptDrawable.getIntrinsicHeight());
        }
    }


}
