package com.gkxim.timon.widget;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import com.gkxim.timon.labs.R;

public class AnimationView extends View {

    Paint paint;

    Bitmap bm;
    int bm_offsetX, bm_offsetY;

    Path animPath;
    PathMeasure pathMeasure;
    float pathLength;

    float step;   //distance each step
    float distance;  //distance moved
    float curX, curY;

    float curAngle;  //current angle
    float targetAngle; //target angle
    float stepAngle; //angle each step

    float[] pos;
    float[] tan;

    Matrix matrix;

    Path touchPath;

    public AnimationView(Context context) {
        super(context);
        initMyView();
    }

    public AnimationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initMyView();
    }

    public AnimationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initMyView();
    }

    public void initMyView() {
        paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(1);
        paint.setStyle(Paint.Style.STROKE);

        bm = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
        bm_offsetX = bm.getWidth() / 2;
        bm_offsetY = bm.getHeight() / 2;

        animPath = new Path();

        pos = new float[2];
        tan = new float[2];

        matrix = new Matrix();

        touchPath = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {

        if (animPath.isEmpty()) {
            return;
        }

        canvas.drawPath(animPath, paint);

        matrix.reset();

        if ((targetAngle - curAngle) > stepAngle) {
            curAngle += stepAngle;
            matrix.postRotate(curAngle, bm_offsetX, bm_offsetY);
            matrix.postTranslate(curX, curY);
            canvas.drawBitmap(bm, matrix, null);

            invalidate();
        } else if ((curAngle - targetAngle) > stepAngle) {
            curAngle -= stepAngle;
            matrix.postRotate(curAngle, bm_offsetX, bm_offsetY);
            matrix.postTranslate(curX, curY);
            canvas.drawBitmap(bm, matrix, null);

            invalidate();
        } else {
            curAngle = targetAngle;
            if (distance < pathLength) {
                pathMeasure.getPosTan(distance, pos, tan);

                targetAngle = (float) (Math.atan2(tan[1], tan[0]) * 180.0 / Math.PI);
                matrix.postRotate(curAngle, bm_offsetX, bm_offsetY);

                curX = pos[0] - bm_offsetX;
                curY = pos[1] - bm_offsetY;
                matrix.postTranslate(curX, curY);

                canvas.drawBitmap(bm, matrix, null);

                distance += step;

                invalidate();
            } else {
                matrix.postRotate(curAngle, bm_offsetX, bm_offsetY);
                matrix.postTranslate(curX, curY);
                canvas.drawBitmap(bm, matrix, null);
            }
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = event.getAction();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                touchPath.reset();
                touchPath.moveTo(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_MOVE:
                touchPath.lineTo(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_UP:
                touchPath.lineTo(event.getX(), event.getY());
                animPath = new Path(touchPath);

                pathMeasure = new PathMeasure(animPath, false);
                pathLength = pathMeasure.getLength();

                step = 3;
                distance = 0;
                curX = 0;
                curY = 0;

                stepAngle = 1;
                curAngle = 0;
                targetAngle = 0;

                invalidate();

                break;

        }

        return true;
    }

}