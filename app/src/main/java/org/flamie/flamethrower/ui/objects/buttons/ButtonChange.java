package org.flamie.flamethrower.ui.objects.buttons;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringListener;
import com.facebook.rebound.SpringSystem;

import org.flamie.flamethrower.R;

import static org.flamie.flamethrower.util.DimenUtils.dp;

public class ButtonChange extends View implements SpringListener {

    private Drawable switchDrawable;
    private Paint circlePaint;
    private float valueRadius;

    private SpringSystem springSystemRotate;
    private Spring mSpringRotate;
    private SpringSystem springSystemStroke;
    private Spring mSpringStroke;
    private SpringSystem springSystemRadius;
    private Spring mSpringRadius;
    private SpringSystem springSystemOpacity;
    private Spring mSpringOpacity;

    public ButtonChange(Context context) {
        super(context);
        springSystemRotate = SpringSystem.create();
        mSpringRotate = springSystemRotate.createSpring();
        mSpringRotate.addListener(this);

        springSystemStroke = SpringSystem.create();
        mSpringStroke = springSystemStroke.createSpring();
        mSpringStroke.addListener(this);

        springSystemRadius = SpringSystem.create();
        mSpringRadius = springSystemRadius.createSpring();
        mSpringRadius.addListener(this);

        springSystemOpacity = SpringSystem.create();
        mSpringOpacity = springSystemOpacity.createSpring();
        mSpringOpacity.addListener(this);

        circlePaint = new Paint();
        circlePaint.setAntiAlias(true);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setColor(Color.WHITE);

        mSpringRotate.setEndValue(180);
        mSpringStroke.setEndValue(dp(2));
        mSpringRadius.setEndValue(dp(7));

        switchDrawable = ContextCompat.getDrawable(getContext(), R.drawable.switcher);
        switchDrawable.setBounds(0, 0, switchDrawable.getIntrinsicWidth(), switchDrawable.getIntrinsicHeight());

        mSpringOpacity.setSpringConfig(new SpringConfig(50, 90));
        mSpringOpacity.setCurrentValue(255);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        switchDrawable.draw(canvas);
        canvas.drawCircle(dp(23), dp(19), valueRadius, circlePaint);
    }

    @Override
    public void onMeasure(int measureWidthSpec, int measureHeightSpec) {
        if(switchDrawable != null) {
            setMeasuredDimension(switchDrawable.getIntrinsicWidth(), switchDrawable.getIntrinsicHeight());
        }
    }

    @Override
    public void onSpringUpdate(Spring spring) {
        int valueOpacity = (int) mSpringOpacity.getCurrentValue();
        float valueRotate = (float) mSpringRotate.getCurrentValue();
        float valueStroke = (float) mSpringStroke.getCurrentValue();
        valueRadius = (float) mSpringRadius.getCurrentValue();
        setRotation(valueRotate);
        circlePaint.setStrokeWidth(valueStroke);
        switchDrawable.setAlpha(valueOpacity);
        invalidate();
    }

    @Override
    public void onSpringAtRest(Spring spring) {}

    @Override
    public void onSpringActivate(Spring spring) {}

    @Override
    public void onSpringEndStateChange(Spring spring) {}

    public Spring getSpringRotate() {
        return mSpringRotate;
    }

    public Spring getSpringStroke() {
        return mSpringStroke;
    }

    public Spring getSpringRadius() {
        return mSpringRadius;
    }

    public Spring getSpringOpacity() {
        return mSpringOpacity;
    }
}
