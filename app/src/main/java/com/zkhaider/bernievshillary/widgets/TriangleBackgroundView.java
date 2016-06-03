package com.zkhaider.bernievshillary.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import com.zkhaider.bernievshillary.R;

/**
 * Created by ZkHaider on 5/29/16.
 */

public class TriangleBackgroundView extends View {

    private static final int DEFAULT_BERNIE_COLOR = Color.parseColor("#3174BF");
    private static final int DEFAULT_HILLARY_COLOR = Color.parseColor("#4DA096");

    /**
     * Paint
     */
    private Paint mPaint;
    private Paint mBackgroundPaint;

    /**
     * Path
     */
    private Path mPath;

    /**
     * Colors
     */
    private int mForegroundColor;
    private int mBackgroundColor;

    /**
     * Ratios
     */
    private float mHeightRatio1 = 0.39f;
    private float mHeightRatio2 = 0.65f;

    public TriangleBackgroundView(Context context) {
        super(context);
        init();
    }

    public TriangleBackgroundView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public TriangleBackgroundView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setStrokeWidth(1.0f);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeCap(Paint.Cap.SQUARE);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(DEFAULT_HILLARY_COLOR);

        mBackgroundPaint = new Paint();
        mBackgroundPaint.setStyle(Paint.Style.FILL);
        mBackgroundPaint.setColor(DEFAULT_BERNIE_COLOR);

        mPath = new Path();
    }

    private void init(Context context, AttributeSet attrs) {

        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TriangleBackgroundView);
        try {
            mHeightRatio1 = a.getFloat(R.styleable.TriangleBackgroundView_ratio1, mHeightRatio1);
            mHeightRatio2 = a.getFloat(R.styleable.TriangleBackgroundView_ratio2, mHeightRatio2);
            mForegroundColor = a.getColor(R.styleable.TriangleBackgroundView_foregroundColor, DEFAULT_HILLARY_COLOR);
            mBackgroundColor = a.getColor(R.styleable.TriangleBackgroundView_backgroundColor, DEFAULT_BERNIE_COLOR);
        } finally {
            if (a != null)
                a.recycle();
        }

        mPaint = new Paint();
        mPaint.setStrokeWidth(1.0f);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeCap(Paint.Cap.SQUARE);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mBackgroundColor);

        mBackgroundPaint = new Paint();
        mBackgroundPaint.setStyle(Paint.Style.FILL);
        mBackgroundPaint.setColor(mForegroundColor);

        mPath = new Path();
    }

    public void resetPath() {
        mPath.reset();
    }

    public void setHeightRatio1(float heightRatio1) {
        this.mHeightRatio1 = heightRatio1;
    }

    public void setHeightRatio2(float heightRatio2) {
        this.mHeightRatio2 = heightRatio2;
    }

    public float getHeightRatio1() {
        return mHeightRatio1;
    }

    public float getHeightRatio2() {
        return mHeightRatio2;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int h = getMeasuredHeight();
        int w = getMeasuredWidth();

        canvas.drawRect(0, 0, w, h, mBackgroundPaint);

        mPath.moveTo(w, h * mHeightRatio1);
        mPath.lineTo(w, h);
        mPath.lineTo(0, h);
        mPath.lineTo(0, h * mHeightRatio2);
        mPath.lineTo(w, h * mHeightRatio1);
        mPath.close();
        canvas.drawPath(mPath, mPaint);
    }

}
