package org.flamie.flamethrower.objects.buttons;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringListener;
import com.facebook.rebound.SpringSystem;

import org.flamie.flamethrower.R;

public class FlashButtonAuto extends View implements SpringListener {

    private SpringSystem mSpringSystemIcon;
    private Spring mSpringFlashAuto;
    private final Drawable flashDrawableAuto;

    private SpringSystem mSpringSystemOpacity;
    private Spring mSpringOpacity;

    public FlashButtonAuto(Context context) {
        super(context);
        mSpringSystemIcon = SpringSystem.create();
        mSpringFlashAuto = mSpringSystemIcon.createSpring();
        mSpringFlashAuto.addListener(this);

        mSpringSystemOpacity = SpringSystem.create();
        mSpringOpacity = mSpringSystemOpacity.createSpring();
        mSpringOpacity.addListener(this);

        flashDrawableAuto = ContextCompat.getDrawable(getContext(), R.drawable.flash_auto);
        flashDrawableAuto.setBounds(0, 0, flashDrawableAuto.getIntrinsicWidth(), flashDrawableAuto.getIntrinsicHeight());

        mSpringFlashAuto.setEndValue(0);
        mSpringOpacity.setEndValue(255);

        mSpringOpacity.setSpringConfig(new SpringConfig(100, 20));
        mSpringFlashAuto.setSpringConfig(new SpringConfig(100, 20));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        flashDrawableAuto.draw(canvas);
    }

    @Override
    public void onSpringUpdate(Spring spring) {
        int valueY = (int) mSpringFlashAuto.getCurrentValue();
        int valueOpacity = (int) mSpringOpacity.getCurrentValue();
        flashDrawableAuto.setBounds(0, valueY, flashDrawableAuto.getIntrinsicWidth(), valueY + flashDrawableAuto.getIntrinsicHeight());
        flashDrawableAuto.setAlpha(valueOpacity);
        invalidate();
    }

    @Override
    public void onSpringAtRest(Spring spring) {}

    @Override
    public void onSpringActivate(Spring spring) {}

    @Override
    public void onSpringEndStateChange(Spring spring) {}
//
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if(flashDrawableAuto != null) {
            setMeasuredDimension(flashDrawableAuto.getIntrinsicWidth(), flashDrawableAuto.getIntrinsicHeight() + 100);
        }
    }

    public Spring getSpringFlashAuto() {
        return mSpringFlashAuto;
    }

    public Spring getSpringOpacity() {
        return mSpringOpacity;
    }
}
