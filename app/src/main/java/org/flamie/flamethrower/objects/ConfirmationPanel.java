package org.flamie.flamethrower.objects;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;

public class ConfirmationPanel extends View {

    private Paint panelBackgroundPaint;
    private RectF panelBackground;

    public ConfirmationPanel(Context context) {
        super(context);
        panelBackgroundPaint = new Paint();
        panelBackgroundPaint.setAntiAlias(true);
        panelBackgroundPaint.setStyle(Paint.Style.FILL);
        panelBackgroundPaint.setColor(Color.rgb(37, 39, 42));

        panelBackground = new RectF();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        panelBackground.set(0, 1400f, getWidth(), getHeight());
        canvas.drawRect(panelBackground, panelBackgroundPaint);
    }

}