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

public class ButtonAccept extends View implements SpringListener {

    private boolean visible;

    private SpringSystem mSpringSystem;
    private Spring mSpringSize;

    private Drawable acceptDrawable;
    private float valueSize;

    public ButtonAccept(Context context) {
        super(context);
        visible = true;

        mSpringSystem = SpringSystem.create();
        mSpringSize = mSpringSystem.createSpring();
        mSpringSize.addListener(this);

        acceptDrawable = ContextCompat.getDrawable(getContext(), R.drawable.btn_done);
        acceptDrawable.setBounds(0, 0, acceptDrawable.getIntrinsicWidth(), acceptDrawable.getIntrinsicHeight());
        mSpringSize.setCurrentValue(0.0f);

        mSpringSize.setSpringConfig(new SpringConfig(220, 24));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.scale(valueSize, valueSize, 80, 80);
        acceptDrawable.draw(canvas);
    }

    @Override
    public void onMeasure(int measureWidthSpec, int measureHeightSpec) {
        if(acceptDrawable != null) {
            setMeasuredDimension(acceptDrawable.getIntrinsicWidth(), acceptDrawable.getIntrinsicHeight());
        }
    }


    @Override
    public void onSpringUpdate(Spring spring) {
        valueSize = (float) mSpringSize.getCurrentValue();
        invalidate();
    }

    @Override
    public void onSpringAtRest(Spring spring) {
        if(!visible) {
            setVisibility(INVISIBLE);
        }
    }

    @Override
    public void onSpringActivate(Spring spring) {}

    @Override
    public void onSpringEndStateChange(Spring spring) {}

    public void hide() {
        visible = false;
        mSpringSize.setEndValue(0.0f);
    }

    public void show() {
        visible = true;
        mSpringSize.setEndValue(1.0f);
    }

    public Spring getSpringSize() {
        return mSpringSize;
    }

}
