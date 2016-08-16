package org.flamie.flamethrower.ui.objects;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;

import static org.flamie.flamethrower.util.DimenUtils.dp;

public class CropBottomPanel extends View {

    private Paint panelBackgroundPaint;
    private RectF panelBackground;

    public CropBottomPanel(Context context) {
        super(context);

        panelBackgroundPaint = new Paint();
        panelBackgroundPaint.setColor(Color.argb(255, 37, 39, 42));
        panelBackgroundPaint.setStyle(Paint.Style.FILL);
        panelBackgroundPaint.setAntiAlias(true);

        panelBackground = new RectF();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        panelBackground.set(0, dp(520), getWidth(), getHeight());
        canvas.drawRect(panelBackground, panelBackgroundPaint);
    }

}
