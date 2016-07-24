package org.flamie.flamethrower.ui.objects.buttons;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.View;

import org.flamie.flamethrower.R;

public class ButtonDecline extends View {

    private Drawable declineDrawable;
    private int declineIconX = 125;
    private int declineIconY = 1480;

    public ButtonDecline(Context context) {
        super(context);
        declineDrawable = ContextCompat.getDrawable(getContext(), R.drawable.btn_cancel);
        declineDrawable.setBounds(declineIconX, declineIconY, declineIconX + declineDrawable.getIntrinsicWidth(),
                declineIconY + declineDrawable.getIntrinsicHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        declineDrawable.draw(canvas);
    }

    @Override
    public void onMeasure(int measureWidthSpec, int measureHeightSpec) {
        if(declineDrawable != null) {
            setMeasuredDimension(declineIconX + declineDrawable.getIntrinsicWidth(), declineIconY + declineDrawable.getIntrinsicHeight());
        }
    }

}
