package com.yuedong.youbutie_merchant_android.view;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.OnScaleGestureListener;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageView;

/**
 * 自定义的ImageView 自动把图片填充到空间大小并居中显示 双点缩放触点缩放
 *
 * @author Administrator
 */
public class ZoomImageView extends ImageView implements
        OnGlobalLayoutListener/*
                             * 全局布局完成会调用这个接口的回调
							 */, OnScaleGestureListener, OnTouchListener

{

    private boolean mOne = true;

    /**
     * 初始的缩放值
     */
    private float mInitScale;
    /**
     * 极限放大比例值
     */
    private float mMaxScale;
    /**
     * 双击放大的比例值
     */
    private float mMidScale;
    private Matrix mScaleMatrix;

    /**
     * 捕获用户多指触控时缩放的比例
     */
    private ScaleGestureDetector mScaleGestureDetector;

    // ------自由移动
    /**
     * 记录上一次移动的手指数量
     */
    private int mLastPointerCount;
    private float mLastX, mLastY;
    private int mTouchSlop;
    private boolean isCanDrag;
    private boolean isCheckLeftRight, isCheckTopBottom;

    // 双击放大缩小
    private GestureDetector mGestureDetector;
    private boolean isAuto;
    /**
     * 图片原始高度
     */
    private int picOriginalHeight;
    /**
     * 图片原始宽度
     */
    private int picOriginalWidth;

    public ZoomImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setScaleType(ScaleType.MATRIX);
        mScaleMatrix = new Matrix();
        mScaleGestureDetector = new ScaleGestureDetector(context, this);
        setOnTouchListener(this);
        // 拿到比较值
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();

        mGestureDetector = new GestureDetector(context,
                new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onDoubleTap(MotionEvent e) {
                        if (isAuto) {
                            return true;
                        }
                        // 以这个点进行放大缩小
                        float x = e.getX();
                        float y = e.getY();
                        // 如果当前图片的缩放值比定义的双击放大值小 则让他放大
                        if (getScale() < mMidScale) {
                            // mScaleMatrix.postScale(mMidScale / getScale(),
                            // mMidScale / getScale(), x, y);
                            // setImageMatrix(mScaleMatrix);
                            postDelayed(new AutoScaleRunnable(mMidScale, x, y),
                                    16);
                            isAuto = true;
                        } else {
                            // mScaleMatrix.postScale(mInitScale / getScale(),
                            // mInitScale / getScale(), x, y);
                            // setImageMatrix(mScaleMatrix);
                            postDelayed(
                                    new AutoScaleRunnable(mInitScale, x, y), 16);
                            isAuto = true;
                        }
                        return true;
                    }
                });
    }

    public class AutoScaleRunnable implements Runnable {
        // 缩放的目标值
        private float targerScale;
        // 放大缩小中心点
        private float x;
        private float y;
        private final float BIGGER = 1.07f;
        private final float SMALL = 0.93f;
        private float tempScale;

        public AutoScaleRunnable(float targerScale, float x, float y) {
            this.targerScale = targerScale;
            this.x = x;
            this.y = y;

            if (getScale() < targerScale) {
                tempScale = BIGGER;
            }

            if (getScale() > targerScale) {
                tempScale = SMALL;
            }
        }

        @Override
        public void run() {
            // 进行缩放
            mScaleMatrix.postScale(tempScale, tempScale, x, y);
            checkBoundAndCenterWhenScale();
            setImageMatrix(mScaleMatrix);

            float currentScale = getScale();
            if ((tempScale > 1.0f && currentScale < targerScale)
                    || (tempScale < 1.0f && currentScale > targerScale)) {
                postDelayed(this, 16);
            } else {
                // 设置目标值
                float scale = targerScale / currentScale;
                mScaleMatrix.postScale(scale, scale, x, y);
                checkBoundAndCenterWhenScale();
                setImageMatrix(mScaleMatrix);
                isAuto = false;
            }
        }
    }

    public ZoomImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ZoomImageView(Context context) {
        this(context, null);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        // 注册接口
        getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        // 移除接口
        getViewTreeObserver().removeGlobalOnLayoutListener(this);
    }

    /**
     * 获取ImageVIew加载完成的图片
     */
    @Override
    public void onGlobalLayout() {
        if (mOne) {
            // 获取ImageView的宽高
            int width = getWidth();
            int height = getHeight();

            Drawable d = getDrawable();
            if (d == null)
                return;
            // 获取图片的宽高
            picOriginalWidth = d.getIntrinsicWidth();
            picOriginalHeight = d.getIntrinsicHeight();
            imageCenterDisplay(width, height, picOriginalWidth,
                    picOriginalHeight);

            mOne = false;
        }

    }

    /**
     * 让图片居中显示
     *
     * @param width
     * @param height
     * @param dw
     * @param dh
     */
    private void imageCenterDisplay(int width, int height, int dw, int dh) {
        float scale = 1.0f;
		/*
		 * 如果图片的宽度大于控件宽度但是高度小于控件高度 缩小
		 */
        if (dw > width && dh < height) {
            scale = width * 1.0f / dw;
        }

		/*
		 * 如果图片的高度大于控件高度但是宽度小于控件宽度 缩小
		 */
        if (dw < width && dh > height) {
            scale = height * 1.0f / dh;
        }

		/*
		 * 如果图片的宽高都大于或小于控件的宽高 放大缩小
		 */
        if ((dw > width && dh > height) || (dw < width && dh < height)) {
            scale = Math.min(width * 1.0f / dw, height * 1.0f / dh);
        }

        /**
         * 得到初始化的值
         */
        mInitScale = scale;
        mMidScale = scale * 2;
        mMaxScale = scale * 4;

        // 将图片移动至空间中心
        int dx = getWidth() / 2 - dw / 2;
        int dy = getHeight() / 2 - dh / 2;
        mScaleMatrix.postTranslate(dx, dy);
        // 参数： x缩放比例 y缩放比例 x参考点 y参照点
        mScaleMatrix.postScale(mInitScale, mInitScale, width / 2, height / 2);
        setImageMatrix(mScaleMatrix);
    }

    /**
     * 获取当前图片的缩放值
     *
     * @return
     */
    public float getScale() {
        float[] values = new float[9];
        mScaleMatrix.getValues(values);
        return values[Matrix.MSCALE_X];
    }

    // mInitScale ->mMaxScale 缩放区间
    @Override
    public boolean onScale(ScaleGestureDetector arg0) {
        float scale = getScale();
        float scaleFactor = arg0.getScaleFactor();

        Drawable d = getDrawable();
        if (d == null)
            return true;
        // 缩放范围控制
        if ((scale < mMaxScale && scaleFactor > 1.0f)
                || (scale > mInitScale && scaleFactor < 1.0f)) {
            if (scale * scaleFactor < mInitScale) {
                scaleFactor = mInitScale / scale;
            }

            if (scale * scaleFactor > mMaxScale) {
                scaleFactor = mMaxScale / scale;
            }
            mScaleMatrix.postScale(scaleFactor, scaleFactor, arg0.getFocusX(),
                    arg0.getFocusX());
            checkBoundAndCenterWhenScale();
            setImageMatrix(mScaleMatrix);
        }

        return true;
    }

    /**
     * 获得图片放大缩小以后的款和高 l t r b;
     *
     * @return
     */
    private RectF getMatrixRectF() {
        RectF f = new RectF();
        Matrix matrix = mScaleMatrix;
        Drawable d = getDrawable();
        if (d != null) {
            f.set(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
            matrix.mapRect(f);
        }
        return f;
    }

    /**
     * 在缩放的时候控制边界和位置
     */
    private void checkBoundAndCenterWhenScale() {
        RectF f = getMatrixRectF();
        float dValueX = 0;
        float dValueY = 0;
        int width = getWidth();
        int height = getHeight();
        // 缩放时进行边界检测，防止出现白边
        if (f.width() >= width) {
            if (f.left > 0) {
                dValueX = -f.left;
            }

            if (f.right < width) {
                dValueX = width - f.right;
            }
        }

        if (f.height() >= height) {
            if (f.top > 0) {
                dValueY = -f.top;
            }
            if (f.bottom < height) {
                dValueY = height - f.bottom;
            }
        }

        // 如果宽度或者高度 小于控件的宽或者高 让其居中
        if (f.width() < width) {
            dValueX = width / 2 - f.right + f.width() / 2;
        }
        if (f.height() < height) {
            dValueY = height / 2 - f.bottom + f.height() / 2;
        }

        mScaleMatrix.postTranslate(dValueX, dValueY);

    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector arg0) {
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector arg0) {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if (mGestureDetector.onTouchEvent(event)) {
            return true;
        }

        // 把触摸事件交给这个对象处理
        mScaleGestureDetector.onTouchEvent(event);

        float x = 0;
        float y = 0;
        int pointerCount = event.getPointerCount();
        for (int i = 0; i < pointerCount; i++) {
            x += event.getX(i);
            y += event.getY(i);
        }

        x /= pointerCount;
        y /= pointerCount;
        // 触摸过程中用户手指发生变化
        if (mLastPointerCount != pointerCount) {
            isCanDrag = false;
            mLastX = x;
            mLastY = y;
        }
        mLastPointerCount = pointerCount;
        RectF f = getMatrixRectF();
        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                if (getParent() instanceof ViewPager) {

                    // 请求不拦截触摸时间
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                break;

            case MotionEvent.ACTION_MOVE:

                if (getParent() instanceof ViewPager) {
                    Log.e("Leo", "come me");
                    // 请求不拦截触摸事件
                    getParent().requestDisallowInterceptTouchEvent(true);
                }

                float dx = x - mLastX;
                float dy = y - mLastY;
                if (!isCanDrag) {
                    isCanDrag = isMoveAction(dx, dy);
                }
                if (isCanDrag) {
                    RectF matrixRectF = getMatrixRectF();
                    if (getDrawable() != null) {

                        isCheckLeftRight = isCheckTopBottom = true;
                        // 如果宽度小于控件宽度 不允许横向移动
                        if (matrixRectF.width() < getWidth()) {
                            dx = 0;
                            isCheckLeftRight = false;
                        }
                        // 如果高度小于控件高度 不允许纵向移动
                        if (matrixRectF.height() < getHeight()) {
                            dy = 0;
                            isCheckTopBottom = false;
                        }

                        mScaleMatrix.postTranslate(dx, dy);
                        checkBoundWhenTranScale();

                        setImageMatrix(mScaleMatrix);
                    }

                    mLastX = x;
                    mLastY = y;

                }

                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mLastPointerCount = 0;
                break;
        }

        return true;

    }

    /**
     * 移动时进行边界检查
     */
    private void checkBoundWhenTranScale() {
        RectF f = getMatrixRectF();
        float dValueX = 0;
        float dValueY = 0;

        int width = getWidth();
        int height = getHeight();

        if (f.top > 0 && isCheckTopBottom) {
            dValueY = -f.top;
        }

        if (f.bottom < height && isCheckTopBottom) {
            dValueY = height - f.bottom;
        }

        if (f.left > 0 && isCheckLeftRight) {
            dValueX = -f.left;
        }

        if (f.right < width && isCheckLeftRight) {
            dValueX = width - f.right;
        }

        mScaleMatrix.postTranslate(dValueX, dValueY);

    }

    /**
     * 判断是否足以是move
     */
    private boolean isMoveAction(float dx, float dy) {
        return Math.sqrt(dx * dx + dy * dy) > mTouchSlop;
    }

}
