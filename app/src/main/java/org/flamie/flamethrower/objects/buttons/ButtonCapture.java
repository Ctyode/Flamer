package org.flamie.flamethrower.objects.buttons;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class ButtonCapture extends View {

    private Paint outerCirclePaint;
    private Paint innerCirclePaint;
    private Paint centralCirclePaint;

    public ButtonCapture(Context context) {
        super(context);
        outerCirclePaint = new Paint();
        outerCirclePaint.setAntiAlias(true);
        outerCirclePaint.setStyle(Paint.Style.FILL);
        outerCirclePaint.setColor(Color.WHITE);

        innerCirclePaint = new Paint();
        innerCirclePaint.setAntiAlias(true);
        innerCirclePaint.setStyle(Paint.Style.FILL);
        innerCirclePaint.setColor(Color.rgb(61, 150, 227));

        centralCirclePaint = new Paint();
        centralCirclePaint.setAntiAlias(true);
        centralCirclePaint.setStyle(Paint.Style.FILL);
        centralCirclePaint.setColor(Color.rgb(80, 168, 245));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawCircle(getWidth() / 2, 1555f, 100f, outerCirclePaint);
        canvas.drawCircle(getWidth() / 2, 1555f, 90f, innerCirclePaint);
        canvas.drawCircle(getWidth() / 2, 1555f, 60f, centralCirclePaint);
    }

}
