package com.yuedong.youbutie_merchant_android.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.yuedong.youbutie_merchant_android.R;


/**
 * 圆形ImageView，可设置最多两个宽度不同且颜色不同的圆形边框。和圆角图片 通过type来区别
 */
public class RoundImageView extends ImageView {
    private int mBorderThickness = 0;
    private Context mContext;
    private int defaultColor = 0xFFFFFFFF;
    // 如果只有其中一个有值，则只画一个圆形边框
    private int mBorderOutsideColor = 0;
    private int mBorderInsideColor = 0;
    // 控件默认长、宽
    private int defaultWidth = 0;
    private int defaultHeight = 0;
    /**
     * 当前类型
     */
    private int mType;
    /**
     * 半径
     */
    private float mRadius;

    public final static int TYPE_CIRCLE = 0;
    public final static int TYPE_ROUND = 1;
    private Bitmap mBitmap;

    public RoundImageView(Context context) {
        this(context, null);

    }

    public RoundImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        setCustomAttributes(attrs);
    }

    public RoundImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        setCustomAttributes(attrs);
    }

    private void setCustomAttributes(AttributeSet attrs) {
        TypedArray a = mContext.obtainStyledAttributes(attrs, R.styleable.attRoundImage);
        mBorderThickness = a.getDimensionPixelSize(R.styleable.attRoundImage_roundborderWidth, 0);
        mBorderOutsideColor = a.getColor(R.styleable.attRoundImage_outsideColor, defaultColor);
        mBorderInsideColor = a.getColor(R.styleable.attRoundImage_insideColor, defaultColor);
        mType = a.getInt(R.styleable.attRoundImage_type, TYPE_CIRCLE);
        mRadius = a.getDimension(R.styleable.attRoundImage_radius, 10);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Drawable drawable = getDrawable();
        if (drawable == null) {
            return;
        }

        if (getWidth() == 0 || getHeight() == 0) {
            return;
        }
        this.measure(0, 0);
        if (drawable.getClass() == NinePatchDrawable.class || drawable.getClass() != BitmapDrawable.class)
            return;

        BitmapDrawable bitmapdrawable = (BitmapDrawable) drawable;
        Bitmap b = bitmapdrawable.getBitmap();
        Bitmap bitmap = b.copy(Config.ARGB_8888, true);
        if (defaultWidth == 0) {
            defaultWidth = getWidth();

        }
        if (defaultHeight == 0) {
            defaultHeight = getHeight();
        }
        // 保证重新读取图片后不会因为图片大小而改变控件宽、高的大小（针对宽、高为wrap_content布局的imageview，但会导致margin无效）
        // if (defaultWidth != 0 && defaultHeight != 0) {
        // LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
        // defaultWidth, defaultHeight);
        // setLayoutParams(params);
        // }
        if (mType == TYPE_CIRCLE) {
            int radius = 0;
            if (mBorderInsideColor != defaultColor && mBorderOutsideColor != defaultColor) {// 定义画两个边框，分别为外圆边框和内圆边框
                radius = (defaultWidth < defaultHeight ? defaultWidth : defaultHeight) / 2 - 2 * mBorderThickness;
                // 画内圆
                drawCircleBorder(canvas, radius + mBorderThickness / 2, mBorderInsideColor);
                // 画外圆
                drawCircleBorder(canvas, radius + mBorderThickness + mBorderThickness / 2, mBorderOutsideColor);
            } else if (mBorderInsideColor != defaultColor && mBorderOutsideColor == defaultColor) {// 定义画一个边框
                radius = (defaultWidth < defaultHeight ? defaultWidth : defaultHeight) / 2 - mBorderThickness;
                drawCircleBorder(canvas, radius + mBorderThickness / 2, mBorderInsideColor);
            } else if (mBorderInsideColor == defaultColor && mBorderOutsideColor != defaultColor) {// 定义画一个边框
                radius = (defaultWidth < defaultHeight ? defaultWidth : defaultHeight) / 2 - mBorderThickness;
                drawCircleBorder(canvas, radius + mBorderThickness / 2, mBorderOutsideColor);
            } else {// 没有边框
                radius = (defaultWidth < defaultHeight ? defaultWidth : defaultHeight) / 2;
            }
            Bitmap circleBitmap = getCroppedRoundBitmap(bitmap, radius);
            canvas.drawBitmap(circleBitmap, defaultWidth / 2 - radius, defaultHeight / 2 - radius, null);
        } else {
            Bitmap roundAngleBitmap = getRoundAngleBitmap(b, mRadius);
            canvas.drawBitmap(roundAngleBitmap, 0, 0, null);
        }

    }

    private Bitmap getRoundAngleBitmap(Bitmap bitmap, float radius) {

        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();
        int shouldWidth, shouldHeight = 0;
        double scale = 0;
        // 不让图片小于控件的宽高 小于则等比缩放
        if ((bitmapWidth < defaultWidth && bitmapHeight > defaultHeight)
                || (bitmapWidth < defaultWidth && bitmapHeight == defaultHeight)) {
            scale = (defaultWidth * 1.00) / (bitmapWidth * 1.00);
        } else if ((bitmapWidth > defaultWidth && bitmapHeight < defaultHeight)
                || (bitmapWidth == defaultWidth && bitmapHeight < defaultHeight)) {
            scale = (defaultHeight * 1.00) / (bitmapHeight * 1.00);
        } else if (bitmapWidth < defaultWidth && bitmapHeight < defaultHeight) {
            scale = Math.max((defaultWidth * 1.00) / (bitmapWidth * 1.00),
                    (defaultHeight * 1.00) / (bitmapHeight * 1.00));
        }
        if (scale != 0) {
            shouldWidth = (int) Math.round(scale * bitmapWidth);
            shouldHeight = (int) Math.round(bitmapHeight * scale);
        } else {
            shouldWidth = bitmapWidth;
            shouldHeight = bitmapHeight;
        }
        // 经过等比缩放的bitmap
        Bitmap sourceScaleBitmap = Bitmap.createScaledBitmap(bitmap, shouldWidth, shouldHeight, true);
        // 修正bitmap 取中间的部分
        // Bitmap correctionBitmap = null;
        // // 再取中间部分进行圆角扣出
        // if (shouldWidth < shouldHeight) {
        // correctionBitmap = Bitmap.createBitmap(sourceScaleBitmap, 0,
        // shouldHeight - shouldWidth, shouldWidth,
        // shouldWidth, null, true);
        // } else {
        // correctionBitmap = Bitmap.createBitmap(sourceScaleBitmap, shouldWidth
        // - shouldHeight, 0, shouldHeight,
        // shouldHeight, null, true);
        // }
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        Bitmap outBitmap = Bitmap.createBitmap(defaultWidth, defaultHeight, Config.ARGB_8888);
        Canvas canvas = new Canvas(outBitmap);
        canvas.drawARGB(0, 0, 0, 0);
        RectF f = new RectF(0, 0, defaultWidth, defaultHeight);
        canvas.drawRoundRect(f, mRadius, mRadius, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(sourceScaleBitmap, 0, 0, paint);
        // correctionBitmap = null;
        bitmap = null;
        sourceScaleBitmap = null;
        return outBitmap;
    }

    /**
     * 获取裁剪后的圆形图片
     *
     * @param radius 半径
     */
    public Bitmap getCroppedRoundBitmap(Bitmap bmp, int radius) {
        Bitmap scaledSrcBmp;
        int diameter = radius * 2;

        // 为了防止宽高不相等，造成圆形图片变形，因此截取长方形中处于中间位置最大的正方形图片
        int bmpWidth = bmp.getWidth();
        int bmpHeight = bmp.getHeight();
        int squareWidth = 0, squareHeight = 0;
        int x = 0, y = 0;
        Bitmap squareBitmap;
        if (bmpHeight > bmpWidth) {// 高大于宽
            squareWidth = squareHeight = bmpWidth;
            x = 0;
            y = (bmpHeight - bmpWidth) / 2;
            // 截取正方形图片
            squareBitmap = Bitmap.createBitmap(bmp, x, y, squareWidth, squareHeight);
        } else if (bmpHeight < bmpWidth) {// 宽大于高
            squareWidth = squareHeight = bmpHeight;
            x = (bmpWidth - bmpHeight) / 2;
            y = 0;
            squareBitmap = Bitmap.createBitmap(bmp, x, y, squareWidth, squareHeight);
        } else {
            squareBitmap = bmp;
        }

        if (squareBitmap.getWidth() != diameter || squareBitmap.getHeight() != diameter) {
            scaledSrcBmp = Bitmap.createScaledBitmap(squareBitmap, diameter, diameter, true);

        } else {
            scaledSrcBmp = squareBitmap;
        }
        Bitmap output = Bitmap.createBitmap(scaledSrcBmp.getWidth(), scaledSrcBmp.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, scaledSrcBmp.getWidth(), scaledSrcBmp.getHeight());

        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawCircle(scaledSrcBmp.getWidth() / 2, scaledSrcBmp.getHeight() / 2, scaledSrcBmp.getWidth() / 2,
                paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(scaledSrcBmp, rect, rect, paint);
        // bitmap回收(recycle导致在布局文件XML看不到效果)
        // bmp.recycle();
        // squareBitmap.recycle();
        // scaledSrcBmp.recycle();
        bmp = null;
        squareBitmap = null;
        scaledSrcBmp = null;
        return output;
    }

    /**
     * 边缘画圆
     */
    private void drawCircleBorder(Canvas canvas, int radius, int color) {
        Paint paint = new Paint();
        /* 去锯齿 */
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        paint.setColor(color);
        /* 设置paint的 style 为STROKE：空心 */
        paint.setStyle(Paint.Style.STROKE);
		/* 设置paint的外框宽度 */
        paint.setStrokeWidth(mBorderThickness);
        canvas.drawCircle(defaultWidth / 2, defaultHeight / 2, radius, paint);
    }

    public void setType(int type) {
        this.mType = type;
        requestLayout();
        invalidate();
    }

    public void setRaduisAngle(float radius) {
        this.mRadius = radius;
        invalidate();
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        this.mBitmap = bm;
    }

}
