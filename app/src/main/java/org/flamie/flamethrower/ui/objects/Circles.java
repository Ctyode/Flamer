package org.flamie.flamethrower.ui.objects;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;

import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringListener;
import com.facebook.rebound.SpringSystem;

public class Circles extends View implements SpringListener {

    private SpringSystem firstCircleSystem;
    private SpringSystem secondCircleSystem;
    private SpringSystem firstTransparentCircleSystem;
    private SpringSystem secondTransparentCircleSystem;

    private Spring springFirstCircle;
    private Spring springSecondCircle;
    private Spring springFirstTransparentCircle;
    private Spring springSecondTransparentCircle;

    private Paint circlePaint;
    private Paint transparentPaint;

    private RectF transparentRoundedRectangle;

    private float x;
    private float startX;
    private float endX;

    private boolean isRight;
    private boolean isLeft;

    public Circles(Context context) {
        super(context);
        firstCircleSystem = SpringSystem.create();
        springFirstCircle = firstCircleSystem.createSpring();
        springFirstCircle.addListener(this);

        secondCircleSystem = SpringSystem.create();
        springSecondCircle = secondCircleSystem.createSpring();
        springSecondCircle.addListener(this);

        firstTransparentCircleSystem = SpringSystem.create();
        springFirstTransparentCircle = firstTransparentCircleSystem.createSpring();
        springFirstTransparentCircle.addListener(this);

        secondTransparentCircleSystem = SpringSystem.create();
        springSecondTransparentCircle = secondTransparentCircleSystem.createSpring();
        springSecondTransparentCircle.addListener(this);

        circlePaint = new Paint();
        circlePaint.setStyle(Paint.Style.FILL);
        circlePaint.setColor(Color.WHITE);
        circlePaint.setAntiAlias(true);

        transparentPaint = new Paint();
        transparentPaint.setStyle(Paint.Style.FILL);
        transparentPaint.setColor(Color.argb(120, 255, 255, 255));
        transparentPaint.setAntiAlias(true);

        transparentRoundedRectangle = new RectF();
        startX = 0f;
        endX = 100f;
        transparentRoundedRectangle.set(startX, 90f, 20f, 110f);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawCircle(x, 100f, 10f, circlePaint);
        if(startX == 0f) {
            transparentRoundedRectangle.set(startX, 90f, x, 110f);
        } else if(startX == endX) {
            transparentRoundedRectangle.set(x, 90f, endX, 110f);
        }
        canvas.drawRoundRect(transparentRoundedRectangle, 10f, 10f, transparentPaint);
    }

    @Override
    public void onSpringUpdate(Spring spring) {
    }

    @Override
    public void onSpringAtRest(Spring spring) {}

    @Override
    public void onSpringActivate(Spring spring) {}

    @Override
    public void onSpringEndStateChange(Spring spring) {}

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                x = clamp(event.getX()/ 10, 0, 100);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                if(x >= 80f) {
                    startX = endX;
                    x = 100f;
                    isRight = true;
                    isLeft = false;
                } else if(x <= 20f) {
                    startX = 0f;
                    x = 0f;
                    isLeft = true;
                    isRight = false;
                }
                invalidate();
                break;
        }
        return true;
    }

    public float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(max, value));
    }
}
