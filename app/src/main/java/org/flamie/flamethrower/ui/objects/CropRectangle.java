package org.flamie.flamethrower.ui.objects;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;

import org.flamie.flamethrower.ui.MainObjects;

public class CropRectangle extends View {

    private final int left;
    private final int right;
    private final int top;
    private final int bottom;
    private RectF cropRectangle;
    private Paint paint;
    private float x;
    private float y;

    private int currentCorner = -1;
    private float width;
    private float height;

    public CropRectangle(Context context) {
        super(context);
        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10);
        paint.setAntiAlias(true);

        cropRectangle = new RectF();

        left = 10;
        right = MainObjects.getBitmap().getWidth() / 2;
        top = 10;
        bottom = MainObjects.getBitmap().getHeight() / 2;

        cropRectangle.set(left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        switch (currentCorner) {
            case 0:
                cropRectangle.set(x, y, cropRectangle.right, cropRectangle.bottom);
                break;
            case 1:
                cropRectangle.set(cropRectangle.left, cropRectangle.top, x, y);
                break;
            case 2:
                cropRectangle.set(cropRectangle.left, y, x, cropRectangle.bottom);
                break;
            case 3:
                cropRectangle.set(x, cropRectangle.top, cropRectangle.right, y);
                break;
        }
        canvas.drawRect(cropRectangle, paint);
    }

    @Override
    public boolean onTouchEvent (MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                x = event.getX();
                y = event.getY();
                width = cropRectangle.width();
                height = cropRectangle.height();
                currentCorner();
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                currentCorner = -1;
                break;
        }
        return true;
    }

    public void currentCorner() {
        if(x > cropRectangle.left && x < (cropRectangle.left + width / 2) &&
           y > cropRectangle.top && y < (cropRectangle.top + height / 2)) {
            // top left
            currentCorner = 0;
        } else if(x < cropRectangle.right && x > (cropRectangle.right - width / 2) &&
                  y > cropRectangle.bottom && y > (cropRectangle.bottom - height / 2)) {
            // bottom right
            currentCorner = 1;
        } else if(x < cropRectangle.right && x > (cropRectangle.right - width / 2) &&
                  y > cropRectangle.top && y < (cropRectangle.top + height / 2)) {
            // top right
             currentCorner = 2;
        } else if(x > cropRectangle.left && x < (cropRectangle.left + width / 2) &&
                  y > cropRectangle.bottom && y > (cropRectangle.bottom - height / 2)) {
            // bottom left
             currentCorner = 3;
        }

    }

}
