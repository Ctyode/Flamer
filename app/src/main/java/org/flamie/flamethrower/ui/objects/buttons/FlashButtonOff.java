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

public class FlashButtonOff extends View implements SpringListener {

    private boolean visible;

    private SpringSystem mSpringSystemIcon;
    private Spring mSpringFlashOff;
    private final Drawable flashDrawableOff;

    private SpringSystem mSpringSystemOpacity;
    private Spring mSpringOpacity;

    public FlashButtonOff(Context context) {
        super(context);
        visible = false;

        mSpringSystemIcon = SpringSystem.create();
        mSpringFlashOff = mSpringSystemIcon.createSpring();
        mSpringFlashOff.addListener(this);

        mSpringSystemOpacity = SpringSystem.create();
        mSpringOpacity = mSpringSystemOpacity.createSpring();
        mSpringOpacity.addListener(this);

        flashDrawableOff = ContextCompat.getDrawable(getContext(), R.drawable.flash_off);
        flashDrawableOff.setBounds(0, 0, flashDrawableOff.getIntrinsicWidth(), flashDrawableOff.getIntrinsicHeight());

        mSpringFlashOff.setCurrentValue(0);
        mSpringOpacity.setEndValue(0);

        mSpringOpacity.setSpringConfig(new SpringConfig(230, 30));
        mSpringFlashOff.setSpringConfig(new SpringConfig(230, 30));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        flashDrawableOff.draw(canvas);
    }

    @Override
    public void onSpringUpdate(Spring spring) {
        int valueY = (int) mSpringFlashOff.getCurrentValue();
        int valueOpacity = (int) mSpringOpacity.getCurrentValue();
        flashDrawableOff.setBounds(0, valueY, flashDrawableOff.getIntrinsicWidth(), valueY + flashDrawableOff.getIntrinsicHeight());
        flashDrawableOff.setAlpha(valueOpacity);
        invalidate();
    }

    @Override
    public void onSpringAtRest(Spring spring) {
        if(!visible) {
            setVisibility(INVISIBLE);
            mSpringFlashOff.setEndValue(0);
            mSpringFlashOff.setCurrentValue(0);
        }
    }

    @Override
    public void onSpringActivate(Spring spring) {
        if(visible) {
            setVisibility(VISIBLE);
        }
    }

    @Override
    public void onSpringEndStateChange(Spring spring) {}

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if(flashDrawableOff != null) {
            setMeasuredDimension(flashDrawableOff.getIntrinsicWidth(), flashDrawableOff.getIntrinsicHeight() + 300);
        }
    }

    public void hide() {
        mSpringOpacity.setEndValue(0);
        mSpringFlashOff.setEndValue(200);
        visible = false;
    }

    public void show() {
        mSpringOpacity.setEndValue(255);
        mSpringFlashOff.setEndValue(100);
        visible = true;
    }

}
