package com.yuedong.youbutie_merchant_android.utils;

import android.text.TextUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Random;
import java.util.regex.Pattern;

/**
 * 字符串操作工具类
 */
public final class StringUtil {

    private StringUtil() {
    }

    /**
     * 判断是否中文字符串
     *
     * @param str 待判断的字符串
     * @return 如果包含中文返回true, 否则false
     */
    public static boolean isChinese(CharSequence str) {
        if (StringUtil.isEmpty(str)) {
            return false;
        }
        return str.toString().matches("[\\u4e00-\\u9fa5]+");
    }

    /**
     * 判断是否是手机号
     *
     * @param mobiles
     * @return
     */
    public static boolean isMobileNO(String mobiles) {
        // 移动号段越来越多 去掉限制
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(mobiles).matches();
    }

    /**
     * 生成N位随机数
     *
     * @param length 随机数长度
     * @return String 生成的随机数
     */
    public static String getRandomNumbers(int length) {
        if (length <= 0) {
            return "";
        }

        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    /**
     * 是否为null或空字符串
     *
     * @param str 字符串
     * @return boolean 如果是null或长度为0,则返回true,否则返回false
     */
    public static boolean isEmpty(CharSequence str) {
        return TextUtils.isEmpty(str);
    }

    /**
     * 是否非null或非空字符串
     *
     * @param str 字符串
     * @return boolean 如果是null或长度为0,则返回false,否则返回true
     */
    public static boolean isNotEmpty(CharSequence str) {
        return !isEmpty(str);
    }

    public static boolean isNotEmpty(CharSequence... strs) {
        boolean flag = true;
        for (CharSequence charSequence : strs) {
            flag = !isEmpty(charSequence);
        }
        return flag;
    }


    /**
     * 判断字符串是否是整数
     *
     * @param value 待判断的字符串
     * @return boolean 如果是整数格式返回true,否则返回false
     */
    public static boolean isInteger(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * 判断字符串是否是整数
     *
     * @param value 待判断的字符串
     * @return boolean 如果是整数格式返回true,否则返回false
     */
    public static boolean isLong(String value) {
        try {
            Long.parseLong(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * 判断字符串是否是浮点形
     *
     * @param value 待判断的字符串
     * @return boolean 如果是浮点数格式返回true,否则返回false
     */
    public static boolean isDouble(String value) {
        try {
            Double.parseDouble(value);
            return value.contains(".");
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * 判断字符串是否是数字
     *
     * @param value 待判断的字符串
     * @return boolean 如果是数字格式返回true,否则返回false
     */
    public static boolean isNumber(String value) {
        return isInteger(value) || isDouble(value);
    }

    /**
     * double值保留两位小数
     *
     * @param value
     * @return
     */
    public static String setDoubleRetain2Decimal(double value) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
        return bd.toString();
    }

    /**
     * 获取请求流水串
     *
     * @return String 流水串
     */
    public static String getSerialNumber() {
        return DateUtils.getCurrent(DateUtils.FORMAT_YYYYMMDDHHMMSS) + getRandomNumbers(5);
    }

    /**
     * 字符串转float
     */
    public static float getFloat(String str) {

        if (isEmpty(str)) {
            return 0;
        }
        try {
            return Float.parseFloat(str.trim());
        } catch (NumberFormatException e) {
            return 0;
        }

    }

    /**
     * 字符串转整数
     */
    public static int getInteger(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * 字符串转长整形
     */
    public static double getDouble(String value) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return 0d;
        }
    }

    /**
     * 单位分转化成元，并保留两位小数
     *
     * @param penny
     * @return
     */
    public static String PennyToDollor(String penny) {
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format((StringUtil.getDouble(penny)) / 100);
    }

    /**
     * 保留两位小数
     *
     * @param str
     * @return
     */
    public static String formatDoubleDot(String str) {
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format((StringUtil.getDouble(str)));
    }
}
