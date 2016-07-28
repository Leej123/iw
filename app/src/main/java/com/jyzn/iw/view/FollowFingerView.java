package com.jyzn.iw.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/7/27 0027.
 */
public class FollowFingerView extends View{
    private PointF fingerPoint;
    private Paint paint;
    private Path path;
    private Path redrawPath;
    private List<PointF> points;
    private boolean reDraw = false;
    private PointF lastPoint;
    private int pointIndex = 0;
    private OnPathCreatedListener listener;
    private Handler handler;

    public FollowFingerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FollowFingerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    public void setOnPathCreatedListener(OnPathCreatedListener listener) {
        this.listener = listener;
    }

    public void redrawPath() {
        if (points.size() > 1) {
            reDraw = true;
            pointIndex = 1;
            redrawPath.reset();
            redrawPath.moveTo(points.get(0).x, points.get(0).y);
            invalidate();
        }
    }

    private void init() {
        fingerPoint = new PointF();
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5f);
        path = new Path();
        points = new ArrayList<>();
        lastPoint = new PointF();
        reDraw = false;
        handler = new Handler();
        redrawPath = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        paint.setColor(Color.BLUE);
        canvas.drawPath(path, paint);
        if (reDraw) {
            if (pointIndex < points.size()) {

                lastPoint.x = points.get(pointIndex).x;
                lastPoint.y = points.get(pointIndex).y;

                redrawPath.lineTo(lastPoint.x, lastPoint.y);

                paint.setColor(Color.GREEN);
                canvas.drawPath(redrawPath, paint);
                paint.setStyle(Paint.Style.FILL);
                canvas.drawCircle(lastPoint.x, lastPoint.y, 10, paint);
                paint.setStyle(Paint.Style.STROKE);
                pointIndex ++;
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (pointIndex < points.size()) {
                            invalidate();
                        } else {
                            reDraw = false;
                            pointIndex = 0;
                        }
                    }
                }, 50);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        fingerPoint.x = event.getX();
        fingerPoint.y = event.getY();
        if (event.getAction() == MotionEvent.ACTION_DOWN ) {
            path.reset();
            points.clear();
            invalidate();
            path.moveTo(fingerPoint.x, fingerPoint.y);
            points.add(new PointF(fingerPoint.x, fingerPoint.y));
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            path.lineTo(fingerPoint.x, fingerPoint.y);
            points.add(new PointF(fingerPoint.x, fingerPoint.y));
            invalidate();
        } else if (event.getAction() == MotionEvent.ACTION_CANCEL || event.getAction() == MotionEvent.ACTION_UP) {
            if (listener != null)
                listener.onPathCreated(points);
        }
        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public interface OnPathCreatedListener {
        void onPathCreated(List<PointF> pointF);
    }
}
