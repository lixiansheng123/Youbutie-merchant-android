package com.yuedong.youbutie_merchant_android.view;

/**
 * 折线图
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.yuedong.youbutie_merchant_android.app.App;
import com.yuedong.youbutie_merchant_android.utils.DimenUtils;
import com.yuedong.youbutie_merchant_android.utils.L;

import java.util.jar.Attributes;

public class LineChatView extends View {
    /**
     * 原点的X坐标
     */
    public int XPoint;
    /**
     * 原点的Y坐标
     */
    public int YPoint;
    /**
     * X的刻度长度
     */
    public int XScale;
    /**
     * Y的刻度长度
     */
    public int YScale;
    /**
     * X轴的长度
     */
    public int XLength;
    /**
     * Y轴的长度
     */
    public int YLength;
    /**
     * 　X的刻度
     */
    public String[] XLabel;
    /**
     * Y的刻度
     */
    public String[] YLabel;
    /**
     * 数据
     */
    public String[] Data;
    /**
     * 显示的标题
     */
    public String Title;

    private Paint mainPaint;
    private Paint zhouPaint;
    private Paint descPaint;
    private Paint xulinePaint;
    private Paint pointPaint;
    private RectF bgRect;
    private int width, height;
    private String yDesc = "人";
    private String yDesc2 = "数";
    private String xDesc = "月份";
    private String ySign = "%";


    public LineChatView(Context context) {
        this(context, null);
    }

    public LineChatView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void init() {
        mainPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mainPaint.setColor(Color.parseColor("#00d096"));
        mainPaint.setStyle(Paint.Style.FILL);
        zhouPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        zhouPaint.setColor(Color.WHITE);
        zhouPaint.setStyle(Paint.Style.STROKE);
        descPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        descPaint.setColor(Color.WHITE);
        descPaint.setStyle(Paint.Style.STROKE);
        xulinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        xulinePaint.setColor(Color.WHITE);
        xulinePaint.setStyle(Paint.Style.STROKE);
        pointPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        pointPaint.setStyle(Paint.Style.FILL);


    }


    public void SetInfo(String[] XLabels, String[] YLabels, String[] AllData, String strTitle) {
        XLabel = XLabels;
        YLabel = YLabels;
        Data = AllData;
        Title = strTitle;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);//重写onDraw方法
        if (XLabel == null || YLabel == null || Data == null) return;
        if (width == 0 || height == 0) {
            width = getMeasuredWidth();
            height = getMeasuredHeight();
            bgRect = new RectF(0, 0, width, height);
            XPoint = (int) (0.1 * width);
            YPoint = (int) (0.87 * height);
            XLength = (int) (0.88 * width);
            YLength = (int) (0.79 * height);
            XScale = (int) (0.13 * width);
            YScale = (int) (0.123 * height);
            descPaint.setTextSize(Math.round(0.05 * height));
        }
        // 设置背景
        canvas.drawRoundRect(bgRect, 10.0f, 10.0f, mainPaint);
        // 绘制x轴和y轴
        canvas.drawLine(XPoint, YPoint - YLength, XPoint, YPoint, zhouPaint);
        canvas.drawLine(XPoint, YPoint, XPoint + XLength, YPoint, zhouPaint);
        // 测量字体的宽高
        Rect textBounds = new Rect();
        descPaint.getTextBounds(yDesc, 0, yDesc.length(), textBounds);
        canvas.drawText(yDesc, XPoint - Math.round(0.048 * width), YPoint - YLength - textBounds.top, descPaint);
        canvas.drawText(yDesc2, XPoint - Math.round(0.048 * width), YPoint - YLength + textBounds.height() - textBounds.top, descPaint);
        descPaint.getTextBounds(xDesc, 0, xDesc.length(), textBounds); // + Math.round(0.034 * height)
        canvas.drawText(xDesc, XLength + XPoint - textBounds.width(), (float) (YPoint - textBounds.top + 0.03 * height), descPaint);
        // 设置Y
        for (int i = 0; i * YScale < YLength; i++) {
            try {
                String label = !YLabel[i].equals("") ? YLabel[i] + ySign : "";
                descPaint.getTextBounds(label, 0, label.length(), textBounds);
                canvas.drawText(label, XPoint - textBounds.width() - 5, YPoint - i * YScale + 5, descPaint);  //文字
                if (i != 0) {
                    // 绘制虚线
                    Path xuLinePath = new Path();
                    xuLinePath.moveTo(XPoint + 5, YPoint - i * YScale);
                    xuLinePath.lineTo(XPoint + XLength - 15, YPoint - i * YScale);
                    PathEffect effects = new DashPathEffect(new float[]{4, 4, 4, 4}, 1);
                    xulinePaint.setPathEffect(effects);
                    canvas.drawPath(xuLinePath, xulinePaint);
                }
            } catch (Exception e) {
            }
        }
        // 设置X
        for (int i = 0; i * XScale < XLength; i++) {
            try {
                String lable = XLabel[i];
                descPaint.getTextBounds(lable, 0, lable.length(), textBounds);
                canvas.drawText(lable, XPoint + i * XScale - 9, (float) (YPoint - textBounds.top + 0.03 * height), descPaint);  //文字
            } catch (Exception e) {
            }
        }
        // 设置数据 0的位置不画
        for (int i = 0; i * XScale / 2 < XLength; i++) {
            try {
                //数据值
                if (i > 0 && YCoord(Data[i - 1]) != -999 && YCoord(Data[i]) != -999)  //保证有效数据
                    canvas.drawLine(XPoint + (i - 1) * XScale / 2 + XScale / 2, YCoord(Data[i - 1]), XPoint + i * XScale / 2 + XScale / 2, YCoord(Data[i]), descPaint);
                pointPaint.setColor(Color.parseColor("#66ffffff"));
                canvas.drawCircle(XPoint + i * XScale / 2 + XScale / 2, YCoord(Data[i]), Math.round(0.026 * width / 2), pointPaint);
                pointPaint.setColor(Color.WHITE);
                canvas.drawCircle(XPoint + i * XScale / 2 + XScale / 2, YCoord(Data[i]), Math.round(0.015 * width / 2), pointPaint);
            } catch (Exception e) {

            }
        }
    }

    private int YCoord(String y0)  //计算绘制时的Y坐标，无数据时返回-999
    {
        int y;
        try {
            y = Integer.parseInt(y0);
        } catch (Exception e) {
            return -999;    //出错则返回-999
        }
        try {
            return YPoint - y * YScale / Integer.parseInt(YLabel[1]);
        } catch (Exception e) {
        }
        return y;
    }
}