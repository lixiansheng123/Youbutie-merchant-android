package com.yuedong.youbutie_merchant_android.utils;

import android.graphics.Paint;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

/**
 * Created by Administrator on 2015/12/9.
 */
public class TextUtils {


    public static String getText(TextView textView) {
        if (textView == null) return "";
        return textView.getText().toString();
    }

    /**
     * 为文字增加删除线
     *
     * @param textView
     */
    public static void addStrikethrough(TextView textView) {
        if (textView != null) {
            TextPaint textPaint = textView.getPaint();
            textPaint.setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            textPaint.setAntiAlias(true);
        }
    }

}
