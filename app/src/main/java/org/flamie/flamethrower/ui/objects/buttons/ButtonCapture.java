package org.flamie.flamethrower.ui.objects.buttons;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.view.View;
import android.view.ViewTreeObserver;

import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringListener;
import com.facebook.rebound.SpringSystem;

import org.flamie.flamethrower.ui.MainObjects;

public class ButtonCapture extends View implements SpringListener {

    private final RectF outerCircle;
    private final RectF innerCircle;
    private final RectF centralCircle;
    private final RectF recordSmallCircle;
    private final RectF recordBigCircle;
    private final RectF recordRectangle;

    private Paint outerCirclePaint;
    private Paint innerCirclePaint;
    private Paint centralCirclePaint;
    private Paint recordCirclePaint;

    private SpringSystem mSpringSystemOuterX;
    private SpringSystem mSpringSystemOuterY;

    private SpringSystem mSpringSystemInner;
    private SpringSystem mSpringSystemCentral;
    private SpringSystem mSpringSystemSmallRecord;
    private SpringSystem mSpringSystemBigRecord;
    private SpringSystem mSpringSystemRectangleRecord;

    private Spring mSpringOuterX;
    private Spring mSpringOuterY;

    private Spring mSpringInner;
    private Spring mSpringCentral;
    private Spring mSpringSmallRecord;
    private Spring mSpringBigRecord;
    private Spring mSpringRectangleRecord;

    public ButtonCapture(Context context) {
        super(context);
        mSpringSystemOuterX = SpringSystem.create();
        mSpringOuterX = mSpringSystemOuterX.createSpring();
        mSpringOuterX.addListener(this);

        mSpringSystemOuterY = SpringSystem.create();
        mSpringOuterY = mSpringSystemOuterY.createSpring();
        mSpringOuterY.addListener(this);

        mSpringSystemInner = SpringSystem.create();
        mSpringInner = mSpringSystemInner.createSpring();
        mSpringInner.addListener(this);

        mSpringSystemCentral = SpringSystem.create();
        mSpringCentral = mSpringSystemCentral.createSpring();
        mSpringCentral.addListener(this);

        mSpringSystemSmallRecord = SpringSystem.create();
        mSpringSmallRecord = mSpringSystemSmallRecord.createSpring();
        mSpringSmallRecord.addListener(this);

        mSpringSystemBigRecord = SpringSystem.create();
        mSpringBigRecord = mSpringSystemBigRecord.createSpring();
        mSpringBigRecord.addListener(this);

        mSpringSystemRectangleRecord = SpringSystem.create();
        mSpringRectangleRecord = mSpringSystemRectangleRecord.createSpring();
        mSpringRectangleRecord.addListener(this);

        outerCirclePaint = new Paint();
        outerCirclePaint.setAntiAlias(true);
        outerCirclePaint.setStyle(Paint.Style.FILL);
        outerCirclePaint.setColor(Color.WHITE);

        outerCircle = new RectF();
        innerCircle = new RectF();
        centralCircle = new RectF();
        recordSmallCircle = new RectF();
        recordBigCircle = new RectF();
        recordRectangle = new RectF();

        mSpringOuterX.setCurrentValue(100f);
        mSpringOuterY.setCurrentValue(100f);
        mSpringInner.setCurrentValue(90f);
        mSpringCentral.setCurrentValue(60f);
        mSpringSmallRecord.setCurrentValue(0f);
        mSpringBigRecord.setCurrentValue(0f);

        innerCirclePaint = new Paint();
        innerCirclePaint.setAntiAlias(true);
        innerCirclePaint.setStyle(Paint.Style.FILL);
        innerCirclePaint.setColor(Color.rgb(61, 150, 227));

        centralCirclePaint = new Paint();
        centralCirclePaint.setAntiAlias(true);
        centralCirclePaint.setStyle(Paint.Style.FILL);
        centralCirclePaint.setColor(Color.rgb(80, 168, 245));

        recordCirclePaint = new Paint();
        recordCirclePaint.setAntiAlias(true);
        recordCirclePaint.setStyle(Paint.Style.FILL);
        recordCirclePaint.setColor(Color.RED);

        mSpringOuterX.setSpringConfig(new SpringConfig(100, 18));
        mSpringOuterY.setSpringConfig(new SpringConfig(100, 18));
        mSpringBigRecord.setSpringConfig(new SpringConfig(100, 18));

        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                outerCircle.set((getWidth() / 2) - 100f, 0f, (getWidth() / 2) + 100f, 200f);
                innerCircle.set((getWidth() / 2) - 90f, 10f, (getWidth() / 2) + 90f, 190f);
                centralCircle.set((getWidth() / 2) - 60f, 40f, (getWidth() / 2) + 60f, 160f);
                recordSmallCircle.set((getWidth() / 2), 100f, (getWidth() / 2), 100f);
                recordBigCircle.set((getWidth() / 2), 100f, (getWidth() / 2), 100f);
                recordRectangle.set((getWidth() / 2), 100f, (getWidth() / 2), 100f);

                if(Build.VERSION.SDK_INT >= 16) {
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            }
        });

    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawRoundRect(outerCircle, 100f, 100f, outerCirclePaint);
        canvas.drawRoundRect(innerCircle, 90f, 90f, innerCirclePaint);
        canvas.drawRoundRect(centralCircle, 60f, 60f, centralCirclePaint);
        canvas.drawRoundRect(recordSmallCircle, 20f, 20f, recordCirclePaint);
        canvas.drawRoundRect(recordBigCircle, 100f, 100f, recordCirclePaint);
        canvas.drawRoundRect(recordRectangle, 15f, 15f, outerCirclePaint);
    }

    @Override
    public void onSpringUpdate(Spring spring) {
        float valueOuterX = (float) mSpringOuterX.getCurrentValue();
        float valueOuterY = (float) mSpringOuterY.getCurrentValue();

        float valueInner = (float) mSpringInner.getCurrentValue();
        float valueCentral = (float) mSpringCentral.getCurrentValue();
        float valueSmallRecord = (float) mSpringSmallRecord.getCurrentValue();
        float valueBigRecord = (float) mSpringBigRecord.getCurrentValue();
        float valueRectangleRecord = (float) mSpringRectangleRecord.getCurrentValue();
        if(MainObjects.videoMode) {
            outerCircle.set(getWidth() / 2 - valueOuterX, 100f - valueOuterY, getWidth() / 2 + valueOuterX, 100f + valueOuterY);
        } else {
            outerCircle.set(getWidth() / 2 - valueOuterX, 0, getWidth() / 2 + valueOuterX, 200f);
        }
        innerCircle.set(getWidth() / 2 - valueInner, 100f - valueInner, getWidth() / 2 + valueInner, 100f + valueInner);
        centralCircle.set(getWidth() / 2 - valueCentral, 100f - valueCentral, getWidth() / 2 + valueCentral, 100f + valueCentral);
        recordSmallCircle.set(getWidth() / 2 - valueSmallRecord, 100f - valueSmallRecord, getWidth() / 2 + valueSmallRecord, 100f + valueSmallRecord);
        recordBigCircle.set(getWidth() / 2 - valueBigRecord, 100f - valueBigRecord, getWidth() / 2 + valueBigRecord, 100f + valueBigRecord);
        recordRectangle.set(getWidth() / 2 - valueRectangleRecord, 100f - valueRectangleRecord, getWidth() / 2 + valueRectangleRecord, 100f + valueRectangleRecord);
        invalidate();
    }

    @Override
    public void onSpringAtRest(Spring spring) {}

    @Override
    public void onSpringActivate(Spring spring) {}

    @Override
    public void onSpringEndStateChange(Spring spring) {}

    public Spring getSpringOuterX() {
        return mSpringOuterX;
    }

    public Spring getSpringOuterY() {
        return mSpringOuterY;
    }


    public Spring getSpringInner() {
        return mSpringInner;
    }

    public Spring getSpringCentral() {
        return mSpringCentral;
    }

    public Spring getSpringSmallRecord() {
        return mSpringSmallRecord;
    }

    public Spring getSpringBigRecord() {
        return mSpringBigRecord;
    }

    public Spring getSpringRectangleRecord() {
        return mSpringRectangleRecord;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(260, 200);
    }
}