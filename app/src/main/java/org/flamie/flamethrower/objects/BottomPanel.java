package org.flamie.flamethrower.objects;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;

import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringListener;
import com.facebook.rebound.SpringSystem;

public class BottomPanel extends View implements SpringListener {

    private SpringSystem mSpringSystem;
    private Spring mSpringOpacity;

    private Paint panelBackgroundPaint;
    private RectF panelBackground;

    public BottomPanel(Context context) {
        super(context);
        mSpringSystem = SpringSystem.create();
        mSpringOpacity = mSpringSystem.createSpring();
        mSpringOpacity.addListener(this);

        panelBackgroundPaint = new Paint();
        panelBackgroundPaint.setAntiAlias(true);
        panelBackgroundPaint.setStyle(Paint.Style.FILL);

        panelBackground = new RectF();
        mSpringOpacity.setCurrentValue(255);
        panelBackgroundPaint.setColor(Color.argb(255, 37, 39, 42));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        panelBackground.set(0, 1400f, getWidth(), getHeight());
        canvas.drawRect(panelBackground, panelBackgroundPaint);
    }

    @Override
    public void onSpringUpdate(Spring spring) {
        int opacity = (int) mSpringOpacity.getCurrentValue();
        panelBackgroundPaint.setColor(Color.argb(opacity, 37, 39, 42));
        invalidate();
    }

    @Override
    public void onSpringAtRest(Spring spring) {}

    @Override
    public void onSpringActivate(Spring spring) {}

    @Override
    public void onSpringEndStateChange(Spring spring) {}

    public Spring getSpringOpacity() {
        return mSpringOpacity;
    }

}
