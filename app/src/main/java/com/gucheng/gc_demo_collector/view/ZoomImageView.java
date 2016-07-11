package com.gucheng.gc_demo_collector.view;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

/**
 * Created by gc on 2016/7/11.
 */
public class ZoomImageView extends ImageView implements ViewTreeObserver.OnGlobalLayoutListener ,View.OnTouchListener, ScaleGestureDetector.OnScaleGestureListener {

    private boolean mOnce = false;

    /**
     * 初始化缩放值
     */
    private float mInitScale;
    /**
     * 双击缩放值
     */
    private float mMidScale;
    /**
     * 缩放最大值
     */
    private float mMaxScale;

    private Matrix mScaleMatrix;

    /**
     * 捕获用户多指缩放比例
     */
    private ScaleGestureDetector mScaleGestureDetector;

    public ZoomImageView(Context context) {
        this(context,null);
    }

    public ZoomImageView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ZoomImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mScaleMatrix = new Matrix();

        setScaleType(ScaleType.MATRIX);

        mScaleGestureDetector = new ScaleGestureDetector(context,this);

        setOnTouchListener(this);
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            getViewTreeObserver().removeOnGlobalLayoutListener(this);
        }else{
            getViewTreeObserver().removeGlobalOnLayoutListener(this);
        }
    }

    @Override
    public void onGlobalLayout() {

        if (!mOnce) {
            int width = getWidth();
            int height = getHeight();

            Drawable drawable = getDrawable();
            if (drawable == null)
                return;

            int dw = drawable.getIntrinsicWidth();
            int dh = drawable.getIntrinsicHeight();

            /**
             * 缩放
             */
            float scale = 1.0f;

            if (dw > width && dh < height) {
                scale = width * 1.0f / dh;
            } else if (dw < width && dh > height) {
                scale = height * 1.0f / dh;
            } else if ((dw < width && dh < height) || (dw > width && dh > height)) {
                scale = Math.min(width * 1.0f / dw, height * 1.0f / dh);
            }

            /**
             * 得到初始化缩放比例
             */
            mInitScale = scale;
            mMaxScale = scale * 4;
            mMidScale = scale * 2;

            /**
             * 将图片居中控件
             */
            int dx = width / 2 - dw / 2;
            int dy = height / 2 - dh / 2;

            mScaleMatrix.postTranslate(dx , dy);

            mScaleMatrix.postScale(mInitScale,mInitScale,width/2,height/2);

            setImageMatrix(mScaleMatrix);

            mOnce = true;
        }


    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        mScaleGestureDetector.onTouchEvent(event);
        return true;
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        float scaleFactor = detector.getScaleFactor();




        return false;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {

    }
}
