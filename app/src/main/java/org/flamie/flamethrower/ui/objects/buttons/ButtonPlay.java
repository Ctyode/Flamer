package org.flamie.flamethrower.ui.objects.buttons;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.View;

import org.flamie.flamethrower.R;

public class ButtonPlay extends View {

    private Drawable playDrawable;

    public ButtonPlay(Context context) {
        super(context);
        playDrawable = ContextCompat.getDrawable(getContext(), R.drawable.video_play);
        playDrawable.setBounds(0, 0, playDrawable.getIntrinsicWidth(), playDrawable.getIntrinsicHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        playDrawable.draw(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if(playDrawable != null) {
            setMeasuredDimension(playDrawable.getIntrinsicWidth(), playDrawable.getIntrinsicHeight());
        }
    }

}
