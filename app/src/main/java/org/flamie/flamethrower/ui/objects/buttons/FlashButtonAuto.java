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

public class FlashButtonAuto extends View implements SpringListener {

    private boolean visible;

    private SpringSystem mSpringSystemIcon;
    private Spring mSpringFlashAuto;
    private final Drawable flashDrawableAuto;

    private SpringSystem mSpringSystemOpacity;
    private Spring mSpringOpacity;

    public FlashButtonAuto(Context context) {
        super(context);
        visible = true;

        mSpringSystemIcon = SpringSystem.create();
        mSpringFlashAuto = mSpringSystemIcon.createSpring();
        mSpringFlashAuto.addListener(this);

        mSpringSystemOpacity = SpringSystem.create();
        mSpringOpacity = mSpringSystemOpacity.createSpring();
        mSpringOpacity.addListener(this);

        flashDrawableAuto = ContextCompat.getDrawable(getContext(), R.drawable.flash_auto);
        flashDrawableAuto.setBounds(0, 0, flashDrawableAuto.getIntrinsicWidth(), flashDrawableAuto.getIntrinsicHeight());

        mSpringFlashAuto.setCurrentValue(100);
        mSpringOpacity.setEndValue(255);

        mSpringOpacity.setSpringConfig(new SpringConfig(230, 30));
        mSpringFlashAuto.setSpringConfig(new SpringConfig(230, 30));
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
    public void onSpringAtRest(Spring spring) {
        if(!visible) {
            setVisibility(INVISIBLE);
            mSpringFlashAuto.setEndValue(0);
            mSpringFlashAuto.setCurrentValue(0);
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
        if(flashDrawableAuto != null) {
            setMeasuredDimension(flashDrawableAuto.getIntrinsicWidth(), flashDrawableAuto.getIntrinsicHeight() + 300);
        }
    }

    public void hide() {
        mSpringOpacity.setEndValue(0);
        mSpringFlashAuto.setEndValue(200);
        visible = false;
    }

    public void show() {
        mSpringOpacity.setEndValue(255);
        mSpringFlashAuto.setEndValue(100);
        visible = true;
    }

}
