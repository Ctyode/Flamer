package org.flamie.flamethrower.ui.objects;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class Wheel extends View {

    private Paint linesPaint;

    public Wheel(Context context) {
        super(context);

        linesPaint = new Paint();
        linesPaint.setColor(Color.WHITE);
        linesPaint.setStyle(Paint.Style.STROKE);
        linesPaint.setStrokeWidth(3);
        linesPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }



}
