package org.flamie.flamethrower.ui.objects;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;

import org.flamie.flamethrower.ui.MainObjects;

import static org.flamie.flamethrower.util.DimenUtils.dp;

public class CropRectangle extends View {

    private int left;
    private int right;
    private int top;
    private int bottom;
    private float x;
    private float y;
    private float width;
    private float height;

    private RectF cropRectangle;

    private Paint paint;
    private Paint gridPaint;
    private Paint cornerPaint;

    private int currentCorner = -1;
    private Path corners;
    private int cornerStrokeWidth = dp(1.5f);
    private float cornerSize = dp(10);

    public CropRectangle(Context context) {
        super(context);
        corners = new Path();
        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
        paint.setAntiAlias(true);

        gridPaint = new Paint();
        gridPaint.setColor(Color.argb(150, 255, 255, 255));
        gridPaint.setStyle(Paint.Style.STROKE);
        gridPaint.setStrokeWidth(3);
        gridPaint.setAntiAlias(true);

        cornerPaint = new Paint();
        cornerPaint.setColor(Color.WHITE);
        cornerPaint.setStyle(Paint.Style.STROKE);
        cornerPaint.setStrokeWidth(cornerStrokeWidth);
        cornerPaint.setAntiAlias(true);

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
        setGrid(canvas);
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
        if(x > cropRectangle.left && x < (cropRectangle.left + width / 3) &&
           y > cropRectangle.top && y < (cropRectangle.top + height / 3)) {
            // top left
            currentCorner = 0;
        } else if(x < cropRectangle.right && x > (cropRectangle.right - width / 3) &&
                  y < cropRectangle.bottom && y > (cropRectangle.bottom - height / 3)) {
            // bottom right
            currentCorner = 1;
        } else if(x < cropRectangle.right && x > (cropRectangle.right - width / 3) &&
                  y > cropRectangle.top && y < (cropRectangle.top + height / 3)) {
            // top right
             currentCorner = 2;
        } else if(x > cropRectangle.left && x < (cropRectangle.left + width / 3) &&
                  y < cropRectangle.bottom && y > (cropRectangle.bottom - height / 3)) {
            // bottom left
             currentCorner = 3;
        }

    }

    public void setGrid(Canvas canvas) {
        // ты теперь не соня, ты индус нахуй
        switch (currentCorner) {
            case 0:
                canvas.drawLine(cropRectangle.left, y + height / 3, cropRectangle.right, y + height / 3, gridPaint);
                canvas.drawLine(cropRectangle.left, y + (height / 3 * 2), cropRectangle.right, y + (height / 3 * 2), gridPaint);
                canvas.drawLine(x + width / 3, cropRectangle.top, x + width / 3, cropRectangle.bottom, gridPaint);
                canvas.drawLine(x + (width / 3 * 2), cropRectangle.top, x + (width / 3 * 2), cropRectangle.bottom, gridPaint);
                break;
            case 1:
                canvas.drawLine(cropRectangle.left, y - height / 3, cropRectangle.right, y - height / 3, gridPaint);
                canvas.drawLine(cropRectangle.left, y - (height / 3 * 2), cropRectangle.right, y - (height / 3 * 2), gridPaint);
                canvas.drawLine(x - width / 3, cropRectangle.top, x - width / 3, cropRectangle.bottom, gridPaint);
                canvas.drawLine(x - (width / 3 * 2), cropRectangle.top, x - (width / 3 * 2), cropRectangle.bottom, gridPaint);
                break;
            case 2:
                canvas.drawLine(cropRectangle.left, y + height / 3, cropRectangle.right, y + height / 3, gridPaint);
                canvas.drawLine(cropRectangle.left, y + (height / 3 * 2), cropRectangle.right, y + (height / 3 * 2), gridPaint);
                canvas.drawLine(x - width / 3, cropRectangle.top, x - width / 3, cropRectangle.bottom, gridPaint);
                canvas.drawLine(x - (width / 3 * 2), cropRectangle.top, x - (width / 3 * 2), cropRectangle.bottom, gridPaint);
                break;
            case 3:
                canvas.drawLine(cropRectangle.left, y - height / 3, cropRectangle.right, y - height / 3, gridPaint);
                canvas.drawLine(cropRectangle.left, y - (height / 3 * 2), cropRectangle.right, y - (height / 3 * 2), gridPaint);
                canvas.drawLine(x + width / 3, cropRectangle.top, x + width / 3, cropRectangle.bottom, gridPaint);
                canvas.drawLine(x + (width / 3 * 2), cropRectangle.top, x + (width / 3 * 2), cropRectangle.bottom, gridPaint);
                break;
        }

        setCorners();
        canvas.drawPath(corners, cornerPaint);
    }

    public void setCorners() {
        corners.reset();
        corners.moveTo(cropRectangle.left - cornerStrokeWidth, cropRectangle.top - cornerStrokeWidth);
        corners.rLineTo(cornerSize, 0);
        corners.rLineTo(0, cornerStrokeWidth);
        corners.rLineTo(cornerStrokeWidth - cornerSize, 0);
        corners.rLineTo(0, cornerSize - cornerStrokeWidth);
        corners.rLineTo(-cornerStrokeWidth, 0);
        corners.close();

        corners.moveTo(cropRectangle.right + cornerStrokeWidth, cropRectangle.top - cornerStrokeWidth);
        corners.rLineTo(0, cornerSize);
        corners.rLineTo(-cornerStrokeWidth, 0);
        corners.rLineTo(0, cornerStrokeWidth-cornerSize);
        corners.rLineTo(cornerStrokeWidth-cornerSize, 0);
        corners.rLineTo(0, -cornerStrokeWidth);
        corners.close();

        corners.moveTo(cropRectangle.right + cornerStrokeWidth, cropRectangle.bottom + cornerStrokeWidth);
        corners.rLineTo(-cornerSize, 0);
        corners.rLineTo(0, -cornerStrokeWidth);
        corners.rLineTo(cornerSize-cornerStrokeWidth, 0);
        corners.rLineTo(0, cornerStrokeWidth-cornerSize);
        corners.rLineTo(cornerStrokeWidth, 0);
        corners.close();

        corners.moveTo(cropRectangle.left - cornerStrokeWidth, cropRectangle.bottom + cornerStrokeWidth);
        corners.rLineTo(0, -cornerSize);
        corners.rLineTo(cornerStrokeWidth, 0);
        corners.rLineTo(0, (cornerSize - cornerStrokeWidth));
        corners.rLineTo((cornerSize - cornerStrokeWidth), 0);
        corners.rLineTo(0, cornerStrokeWidth);
        corners.close();
    }

//    public Bitmap cropResult() {
//        if (cropRectangle.left < cropRectangle.right && cropRectangle.top < cropRectangle.bottom) {
//            return Bitmap.createBitmap((int) (cropRectangle.right - cropRectangle.left),
//                                       (int) (cropRectangle.bottom - cropRectangle.top), Bitmap.Config.ARGB_8888);
//        }
//        return null;
//    }

}
