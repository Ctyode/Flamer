package org.flamie.flamethrower.ui.objects.buttons;

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

public class FlashButtonOn extends View implements SpringListener {

    private SpringSystem mSpringSystemIcon;
    private Spring mSpringFlashOn;
    private final Drawable flashDrawableOn;

    private SpringSystem mSpringSystemOpacity;
    private Spring mSpringOpacity;

    public FlashButtonOn(Context context) {
        super(context);
        mSpringSystemIcon = SpringSystem.create();
        mSpringFlashOn = mSpringSystemIcon.createSpring();
        mSpringFlashOn.addListener(this);

        mSpringSystemOpacity = SpringSystem.create();
        mSpringOpacity = mSpringSystemOpacity.createSpring();
        mSpringOpacity.addListener(this);

        flashDrawableOn = ContextCompat.getDrawable(getContext(), R.drawable.flash_on);
        flashDrawableOn.setBounds(0, 0, flashDrawableOn.getIntrinsicWidth(), flashDrawableOn.getIntrinsicHeight());

        mSpringFlashOn.setEndValue(0);
        mSpringOpacity.setEndValue(0);

        mSpringOpacity.setSpringConfig(new SpringConfig(100, 40));
        mSpringFlashOn.setSpringConfig(new SpringConfig(100, 40));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        flashDrawableOn.draw(canvas);
    }

    @Override
    public void onSpringUpdate(Spring spring) {
        int valueY = (int) mSpringFlashOn.getCurrentValue();
        int valueOpacity = (int) mSpringOpacity.getCurrentValue();
        flashDrawableOn.setBounds(0, valueY, flashDrawableOn.getIntrinsicWidth(), valueY + flashDrawableOn.getIntrinsicHeight());
        flashDrawableOn.setAlpha(valueOpacity);
        invalidate();
    }

    @Override
    public void onSpringAtRest(Spring spring) {}

    @Override
    public void onSpringActivate(Spring spring) {}

    @Override
    public void onSpringEndStateChange(Spring spring) {}

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if(flashDrawableOn != null) {
            setMeasuredDimension(flashDrawableOn.getIntrinsicWidth(), flashDrawableOn.getIntrinsicHeight() + 300);
        }
    }

    public Spring getSpringFlashOn() {
        return mSpringFlashOn;
    }

    public Spring getSpringOpacity() {
        return mSpringOpacity;
    }
}
