package org.flamie.flamethrower.ui.objects.buttons;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;

import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringListener;
import com.facebook.rebound.SpringSystem;

import org.flamie.flamethrower.ui.MainObjects;

import static org.flamie.flamethrower.util.DimenUtils.dp;

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

    private Runnable mLongPressed;

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

        mSpringOuterX.setCurrentValue(dp(35));
        mSpringOuterY.setCurrentValue(dp(35));
        mSpringInner.setCurrentValue(dp(30));
        mSpringCentral.setCurrentValue(dp(20));
        mSpringSmallRecord.setCurrentValue(0);
        mSpringBigRecord.setCurrentValue(0);

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
                outerCircle.set((getWidth() / 2) - dp(35), 0, (getWidth() / 2) + dp(35), dp(70));
                innerCircle.set((getWidth() / 2) - dp(30), dp(5), (getWidth() / 2) + dp(30), dp(65));
                centralCircle.set((getWidth() / 2) - dp(20), dp(15), (getWidth() / 2) + dp(20), dp(55));
                recordSmallCircle.set((getWidth() / 2), dp(35), (getWidth() / 2), dp(35));
                recordBigCircle.set((getWidth() / 2), dp(35), (getWidth() / 2), dp(35));
                recordRectangle.set((getWidth() / 2), dp(35), (getWidth() / 2), dp(35));

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
        canvas.drawRoundRect(outerCircle, dp(35), dp(35), outerCirclePaint);
        canvas.drawRoundRect(innerCircle, dp(30), dp(30), innerCirclePaint);
        canvas.drawRoundRect(centralCircle, dp(20), dp(20), centralCirclePaint);
        canvas.drawRoundRect(recordSmallCircle, dp(7), dp(7), recordCirclePaint);
        canvas.drawRoundRect(recordBigCircle, dp(35), dp(35), recordCirclePaint);
        canvas.drawRoundRect(recordRectangle, dp(5), dp(5), outerCirclePaint);
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
            outerCircle.set(getWidth() / 2 - valueOuterX, dp(35) - valueOuterY, getWidth() / 2 + valueOuterX, dp(35) + valueOuterY);
        } else {
            outerCircle.set(getWidth() / 2 - valueOuterX, 0, getWidth() / 2 + valueOuterX, dp(70));
        }
        innerCircle.set(getWidth() / 2 - valueInner, dp(35) - valueInner, getWidth() / 2 + valueInner, dp(35) + valueInner);
        centralCircle.set(getWidth() / 2 - valueCentral, dp(35) - valueCentral, getWidth() / 2 + valueCentral, dp(35) + valueCentral);
        recordSmallCircle.set(getWidth() / 2 - valueSmallRecord, dp(35) - valueSmallRecord, getWidth() / 2 + valueSmallRecord, dp(35) + valueSmallRecord);
        recordBigCircle.set(getWidth() / 2 - valueBigRecord, dp(35) - valueBigRecord, getWidth() / 2 + valueBigRecord, dp(35) + valueBigRecord);
        recordRectangle.set(getWidth() / 2 - valueRectangleRecord, dp(35) - valueRectangleRecord, getWidth() / 2 + valueRectangleRecord, dp(35) + valueRectangleRecord);
        invalidate();
    }

    public void transformToVideo() {
        mSpringOuterX.setEndValue(dp(45));
        mSpringInner.setEndValue(0);
        mSpringCentral.setEndValue(0);
        mSpringSmallRecord.setEndValue(dp(7));
    }

    public void transformToPhoto() {
        mSpringOuterX.setEndValue(dp(35));
        mSpringInner.setEndValue(dp(30));
        mSpringCentral.setEndValue(dp(20));
        mSpringSmallRecord.setEndValue(0);
    }

    public void transformToStop() {
        mSpringOuterX.setEndValue(0);
        mSpringOuterY.setEndValue(0);
        mSpringBigRecord.setEndValue(dp(35));
        mSpringRectangleRecord.setEndValue(40f);
    }

    public void transformToVideoFromStop() {
        mSpringOuterX.setEndValue(dp(45));
        mSpringOuterY.setEndValue(dp(35));
        mSpringBigRecord.setEndValue(0);
        mSpringRectangleRecord.setEndValue(0);
    }

//    public void transformFromPhotoToStop() {
//        mSpringOuterX.setEndValue(0);
//        mSpringOuterY.setEndValue(0);
//        mSpringCentral.setEndValue(0);
//        mSpringInner.setEndValue(0);
//        mSpringBigRecord.setEndValue(dp(35));
//        mSpringRectangleRecord.setEndValue(40f);
//    }
//
//    private void transformFromStopToPhoto() {
//        mSpringOuterX.setEndValue(dp(35));
//        mSpringInner.setEndValue(dp(30));
//        mSpringCentral.setEndValue(0);
//        mSpringBigRecord.setEndValue(0);
//        mSpringRectangleRecord.setEndValue(0);
//    }

    @Override
    public void onSpringAtRest(Spring spring) {}

    @Override
    public void onSpringActivate(Spring spring) {}

    @Override
    public void onSpringEndStateChange(Spring spring) {}

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(dp(90), dp(70));
    }

    public void setLongPressed(Runnable mLongPressed) {
        this.mLongPressed = mLongPressed;
    }

    public final Handler handler = new Handler();

    @Override
    public boolean onTouchEvent(MotionEvent event){
        if(event.getAction() == MotionEvent.ACTION_DOWN)
            handler.postDelayed(mLongPressed, 1000);
        if((event.getAction() == MotionEvent.ACTION_MOVE)||(event.getAction() == MotionEvent.ACTION_UP))
            handler.removeCallbacks(mLongPressed);
//            transformFromStopToPhoto();
        return super.onTouchEvent(event);
    }

    public Spring getSpringOuterX() {
        return mSpringOuterX;
    }

    public Spring getSpringOuterY() {
        return mSpringOuterY;
    }

    public Spring getSpringBigRecord() {
        return mSpringBigRecord;
    }

    public Spring getSpringRectangleRecord() {
        return mSpringRectangleRecord;
    }

}