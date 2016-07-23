package org.flamie.flamethrower.objects.buttons;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.view.View;
import android.view.ViewTreeObserver;

import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringListener;
import com.facebook.rebound.SpringSystem;

public class ButtonCapture extends View implements SpringListener {

    private final RectF outerCircle;
    private final RectF innerCircle;
    private final RectF centralCircle;
    private final RectF recordCircle;

    private Paint outerCirclePaint;
    private Paint innerCirclePaint;
    private Paint centralCirclePaint;
    private Paint recordCirclePaint;

    private SpringSystem mSpringSystemOuter;
    private SpringSystem mSpringSystemInner;
    private SpringSystem mSpringSystemCentral;
    private SpringSystem mSpringSystemRecord;

    private Spring mSpringOuter;
    private Spring mSpringInner;
    private Spring mSpringCentral;
    private Spring mSpringRecord;

    public ButtonCapture(Context context) {
        super(context);
        mSpringSystemOuter = SpringSystem.create();
        mSpringOuter = mSpringSystemOuter.createSpring();
        mSpringOuter.addListener(this);

        mSpringSystemInner = SpringSystem.create();
        mSpringInner = mSpringSystemInner.createSpring();
        mSpringInner.addListener(this);

        mSpringSystemCentral = SpringSystem.create();
        mSpringCentral = mSpringSystemCentral.createSpring();
        mSpringCentral.addListener(this);

        mSpringSystemRecord = SpringSystem.create();
        mSpringRecord = mSpringSystemRecord.createSpring();
        mSpringRecord.addListener(this);

        outerCirclePaint = new Paint();
        outerCirclePaint.setAntiAlias(true);
        outerCirclePaint.setStyle(Paint.Style.FILL);
        outerCirclePaint.setColor(Color.WHITE);

        outerCircle = new RectF();
        innerCircle = new RectF();
        centralCircle = new RectF();
        recordCircle = new RectF();

        mSpringOuter.setCurrentValue(100f);
        mSpringInner.setCurrentValue(90f);
        mSpringCentral.setCurrentValue(60f);
        mSpringRecord.setCurrentValue(0f);

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

        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                outerCircle.set((getWidth() / 2) - 100f, 0f, (getWidth() / 2) + 100f, 200f);
                innerCircle.set((getWidth() / 2) - 90f, 10f, (getWidth() / 2) + 90f, 190f);
                centralCircle.set((getWidth() / 2) - 60f, 40f, (getWidth() / 2) + 60f, 160f);
                recordCircle.set((getWidth() / 2), 100f, (getWidth() / 2), 100f);

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
        canvas.drawRoundRect(recordCircle, 20f, 20f, recordCirclePaint);
    }

    @Override
    public void onSpringUpdate(Spring spring) {
        float valueOuter = (float) mSpringOuter.getCurrentValue();
        float valueInner = (float) mSpringInner.getCurrentValue();
        float valueCentral = (float) mSpringCentral.getCurrentValue();
        float valueRecord = (float) mSpringRecord.getCurrentValue();
        outerCircle.set(getWidth() / 2 - valueOuter, 0, getWidth() / 2 + valueOuter, 200f);
        innerCircle.set(getWidth() / 2 - valueInner, 100f - valueInner, getWidth() / 2 + valueInner, 100f + valueInner);
        centralCircle.set(getWidth() / 2 - valueCentral, 100f - valueCentral, getWidth() / 2 + valueCentral, 100f + valueCentral);
        recordCircle.set(getWidth() / 2 - valueRecord, 100f - valueRecord, getWidth() / 2 + valueRecord, 100f + valueRecord);
        invalidate();
    }

    @Override
    public void onSpringAtRest(Spring spring) {}

    @Override
    public void onSpringActivate(Spring spring) {}

    @Override
    public void onSpringEndStateChange(Spring spring) {}

    public Spring getSpringOuter() {
        return mSpringOuter;
    }

    public Spring getSpringInner() {
        return mSpringInner;
    }

    public Spring getSpringCentral() {
        return mSpringCentral;
    }

    public Spring getSpringRecord() {
        return mSpringRecord;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(260, 200);
    }
}