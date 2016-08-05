package org.flamie.flamethrower.ui.objects;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;

import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringListener;
import com.facebook.rebound.SpringSystem;

public class BottomPanel extends View implements SpringListener {

    private SpringSystem mSpringSystemOpacity;
    private Spring mSpringOpacity;
    private SpringSystem mSpringSystemPosition;
    private Spring mSpringPositionY;

    private Paint panelBackgroundPaint;
    private RectF panelBackground;
    private int positionY;

    public BottomPanel(Context context) {
        super(context);
        mSpringSystemOpacity = SpringSystem.create();
        mSpringOpacity = mSpringSystemOpacity.createSpring();
        mSpringOpacity.addListener(this);

        mSpringSystemPosition = SpringSystem.create();
        mSpringPositionY = mSpringSystemPosition.createSpring();
        mSpringPositionY.addListener(this);

        panelBackgroundPaint = new Paint();
        panelBackgroundPaint.setAntiAlias(true);
        panelBackgroundPaint.setStyle(Paint.Style.FILL);
        panelBackgroundPaint.setColor(Color.argb(255, 37, 39, 42));

        panelBackground = new RectF();
        mSpringOpacity.setSpringConfig(new SpringConfig(24, 12));
        mSpringOpacity.setCurrentValue(255);

        mSpringPositionY.setCurrentValue(0);
        mSpringPositionY.setSpringConfig(new SpringConfig(100, 24));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        panelBackground.set(0, 1400 + positionY, getWidth(), getHeight() + positionY);
        canvas.drawRect(panelBackground, panelBackgroundPaint);
    }

    @Override
    public void onSpringUpdate(Spring spring) {
        int opacity = (int) mSpringOpacity.getCurrentValue();
        positionY = (int) mSpringPositionY.getCurrentValue();
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

    public Spring getSpringPositionY() {
        return mSpringPositionY;
    }
}
