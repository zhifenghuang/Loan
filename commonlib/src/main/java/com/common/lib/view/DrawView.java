package com.common.lib.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.common.lib.utils.BaseUtils;

import java.util.ArrayList;

/**
 * Created by gigabud on 15-12-1.
 */
public class DrawView extends View {

    private PointF mPreviousPoint;
    private PointF mStartPoint;
    private PointF mCurrentPoint;

    private Paint mPaint;
    private Path mPath;

    private ArrayList<DrawLineInfo> mDrawPathsList;
    private DrawLineInfo mDrawLineInfo;

    private boolean mIsEnable;

    private OnDrawListener mOnDrawListener;

    public static class DrawLineInfo {   //手指一次按下抬起的绘画信息
        ArrayList<DrawPath> drawPaths;
    }


    public static class DrawPath {
        Path path;
        boolean isDrawPoint;
        Point point;
    }

    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPath = new Path();
        mIsEnable = true;
        mDrawPathsList = new ArrayList<>();
        setBackgroundColor(Color.TRANSPARENT);
        resetDrawPaint(Color.BLACK);
    }

    public void resetDrawPaint(int paintColor) {
        int strokeWidth = BaseUtils.StaticParams.dp2px(getContext(), 4);
        CornerPathEffect cornerPathEffect = new CornerPathEffect(BaseUtils.StaticParams.dp2px(getContext(), 5));
        if (mPaint == null) {
            mPaint = new Paint();
            mPaint.reset();
            mPaint.setAntiAlias(true);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeJoin(Paint.Join.ROUND);
            mPaint.setStrokeCap(Paint.Cap.ROUND);
        }
        mPaint.setStrokeWidth(strokeWidth);
        mPaint.setPathEffect(cornerPathEffect);
        mPaint.setColor(paintColor);
    }

    public void setEnable(boolean isEnable) {
        mIsEnable = isEnable;
    }

    public boolean isEnable() {
        return mIsEnable;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!mIsEnable) {
            return super.onTouchEvent(event);
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDrawLineInfo = new DrawLineInfo();
                mDrawLineInfo.drawPaths = new ArrayList<>();
                mDrawPathsList.add(mDrawLineInfo);
                mCurrentPoint = new PointF(event.getX(), event.getY());
                mPreviousPoint = mCurrentPoint;
                mStartPoint = mPreviousPoint;
                drawLine(mPreviousPoint, mStartPoint, mCurrentPoint, true);
                break;
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_UP:
                mStartPoint = mPreviousPoint;
                mPreviousPoint = mCurrentPoint;
                mCurrentPoint = new PointF(event.getX(), event.getY());
                drawLine(mPreviousPoint, mStartPoint, mCurrentPoint, false);
                break;
            default:
                break;
        }
        return true;
    }


    public synchronized void unDoDraw() {
        if (isDrawPathsListEmpty()) {
            return;
        }
        mDrawPathsList.remove(mDrawPathsList.size() - 1);
        resetDrawView();
    }

    public void resetDrawView() {
        invalidate();
    }

    public boolean isDrawPathsListEmpty() {
        return mDrawPathsList == null || mDrawPathsList.isEmpty();
    }


    public boolean isDrawLineInfoInvalid(DrawLineInfo drawLineInfo) {   //判断DrawLineInfo是否无效
        return drawLineInfo == null || drawLineInfo.drawPaths == null || drawLineInfo.drawPaths.isEmpty();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (DrawLineInfo drawLineInfo : mDrawPathsList) {
            if (isDrawLineInfoInvalid(drawLineInfo)) {
                continue;
            }
            for (DrawPath dp : drawLineInfo.drawPaths) {
                if (dp.isDrawPoint) {
                    canvas.drawPoint(dp.point.x, dp.point.y,
                            mPaint);
                } else {
                    canvas.drawPath(dp.path, mPaint);
                }
            }
        }
        if (mOnDrawListener != null) {
            mOnDrawListener.onDraw(!isDrawPathsListEmpty());
        }
    }


    private void drawLine(PointF previous, PointF start, PointF end, boolean isDrawPoint) {
        DrawPath dp = new DrawPath();
        dp.point = new Point((int) (end.x + 0.5), (int) (end.y + 0.5));
        if (isDrawPoint) {
            dp.isDrawPoint = true;
        } else {
            PointF mid1 = midPoint(previous, start);
            PointF mid2 = midPoint(end, previous);
            mPath = new Path();
            mPath.reset();
            mPath.moveTo(mid1.x, mid1.y);
            mPath.quadTo(previous.x, previous.y, mid2.x, mid2.y);
            dp.path = mPath;
            dp.isDrawPoint = false;
        }
        mDrawLineInfo.drawPaths.add(dp);
        invalidate();
    }

    private PointF midPoint(PointF p1, PointF p2) {
        return new PointF((p1.x + p2.x) * 0.5f, (p1.y + p2.y) * 0.5f);
    }

    public void setOnDrawListener(OnDrawListener listener) {
        mOnDrawListener = listener;
    }

    public interface OnDrawListener {
        public void onDraw(boolean isDrawed);
    }
}

