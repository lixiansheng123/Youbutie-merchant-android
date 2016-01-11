package com.yuedong.youbutie_merchant_android.utils;

import android.annotation.SuppressLint;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 时间工具类
 */
@SuppressLint("SimpleDateFormat")
public final class DateUtils {
    public static final String TAG = DateUtils.class.getSimpleName();
    public static final long MINUTE = 60L;// 每分钟的秒数
    public static final long HOUR = MINUTE * 60;// 每小时的秒数
    public static final long DAY = HOUR * 24;// 每天的秒
    public static final long MONTH = DAY * 30;// 每月(30天计)的秒
    public static final long YEAR = DAY * 365;// 每年(365天计)的秒

    public static final String DATE_TIME_yyyy_MM_dd = "yyyy-MM-dd";

    public static final String DATE_TIME_yyyy_M_d_H_mm = "yyyy-M-d H:mm";

    public static final String DATE_TIME_yyyy_M_d_H_mm_ss = "yyyy-M-d H:mm:ss";

    public static final String DATE_TIME_yyyy_MM_dd_HH_mm = "yyyy-MM-dd HH:mm";

    public static final String DATE_TIME_yyyy_MM_dd_HH_mm_ss = "yyyy-MM-dd HH:mm:ss";

    public static final String DATETIMEyyyyMMddHHmmss = "yyyyMMddHHmmss";

    public static final String DATE_FORMAT_YEAR_MONTH = "yyyy-MM";

    public static final String DATE_FORMAT_yyyyMMdd = "yyyyMMdd";

    public static final SimpleDateFormat FORMAT_DATE = new SimpleDateFormat("yyyy-MM-dd");
    public static final SimpleDateFormat FORMAT_DATE_TIME = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final SimpleDateFormat FORMAT_YYYYMMDDHHMMSS = new SimpleDateFormat("yyyyMMddHHmmss");

    private DateUtils() {
    }

    /**
     * 当前日期，默认yyyy-MM-dd
     */
    public static String getCurrentDate() {
        return FORMAT_DATE.format(new Date());
    }

    /**
     * 当前时间，默认格式yyyy-MM-dd HH:mm:ss
     */
    public static String getCurrentTime() {
        return FORMAT_DATE_TIME.format(new Date());
    }

    /**
     * 当前日期，按指定的格式
     *
     * @param format 指定的日期格式
     * @return 格式后后的当前日期
     */
    public static String getCurrent(String format) {
        return new SimpleDateFormat(format).format(new Date());
    }

    /**
     * 当前日期，按指定的格式
     *
     * @param format 指定的日期格式
     * @return 格式后后的当前日期
     */
    public static String getCurrent(SimpleDateFormat format) {
        return format.format(new Date());
    }

    /**
     * 获取指定日期的年份
     *
     * @param date 日期
     * @return 年份
     */
    public static String getYearString(Date date) {
        Calendar cal = Calendar.getInstance();
        if (date != null) {
            cal.setTime(date);
        }
        return Integer.toString(cal.get(Calendar.YEAR));
    }

    /**
     * 获取指定日期的月份
     */
    public static String getMonthString(Date date) {
        Calendar cal = Calendar.getInstance();
        if (date != null) {
            cal.setTime(date);
        }
        return Integer.toString(cal.get(Calendar.MONTH) + 1);
    }

    /**
     * 解析成日期
     *
     * @param strDate 字符串.
     * @param format  转换格式如:"yyyy-MM-dd mm:ss"
     * @return 字符串包含的日期
     */
    public static Date parseString(String strDate, SimpleDateFormat format) {
        try {
            return format.parse(strDate);
        } catch (ParseException e) {
        }

        return new Date();
    }

    /**
     * 获取时间字符串中的时间部分 注意: 这个方法需要参数是"yyyy-mm-dd"的格式
     *
     * @param src 时间字符串
     * @return 字符串中的日期部分
     */
    public static String getDate(String src) {
        if (src == null || src.length() < 10) {
            return "";
        }
        return src.substring(0, 10);
    }

    /**
     * 把时间字符串,按照某种格式进行转换
     *
     * @param dateStr    原时间字符串
     * @param srcformat  原格式
     * @param destformat 目标格式
     * @return 使用目标格式进行格式化的时间字符串, 转换失败的话返回空字符串
     */
    public static String convertDateString(String dateStr, SimpleDateFormat srcformat, SimpleDateFormat destformat) {
        try {
            Date date = srcformat.parse(dateStr);
            return destformat.format(date);
        } catch (ParseException e) {
        }
        return "";
    }

    /**
     * 比较两个日期的大小 注意: 需要两个日期的格式都是{@link #FORMAT_DATE_TIME}
     *
     * @param srcDate  第一个日期
     * @param destDate 第二个日期
     * @return 如果第一个日期比第二个日期小, 返回true, 否则返回false.转换失败也返回false
     */
    public static boolean isBefore(String srcDate, String destDate) {
        try {
            Date d1 = FORMAT_DATE_TIME.parse(srcDate);
            Date d2 = FORMAT_DATE_TIME.parse(destDate);
            return d1.getTime() <= d2.getTime();
        } catch (ParseException e) {
            Log.e(TAG, e.toString());
            return false;
        }
    }

    /**
     * 根据时间戳获取描述性时间，如3分钟前，1天前
     *
     * @param timestamp 时间戳 单位为毫秒
     * @return 时间字符串
     */
    public static String getDescriptionTimeFromTimestamp(long timestamp) {
        long currentTime = System.currentTimeMillis();
        long timeGap = (currentTime - timestamp) / 1000;// 与现在时间相差秒数
        String timeStr = null;
        if (timeGap > YEAR) {
            timeStr = timeGap / YEAR + "年前";
        } else if (timeGap > MONTH) {
            timeStr = timeGap / MONTH + "个月前";
        } else if (timeGap > DAY) {// 1天以上
            timeStr = timeGap / DAY + "天前";
        } else if (timeGap > HOUR) {// 1小时-24小时
            timeStr = timeGap / HOUR + "小时前";
        } else if (timeGap > MINUTE) {// 1分钟-59分钟
            timeStr = timeGap / MINUTE + "分钟前";
        } else {// 1秒钟-59秒钟
            timeStr = "刚刚";
        }
        return timeStr;
    }

    /**
     * 格式化日期为字符串函数.
     *
     * @param date   日期.
     * @param format 转换格式."yyyy-MM-dd mm:ss"
     * @return 日期转化来的字符串.
     */
    public static String formatDate(Date date, String format) {
        if (date == null) {
            return "";
        }
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(gc.getTime());
    }

    /**
     * 明天日期，默认yyyy-MM-dd
     */
    public static String getTomorrowDate() {
        Date date = new Date();
        return FORMAT_DATE.format(new Date(date.getTime() + 1 * 24 * 60 * 60 * 1000));
    }

    /**
     * 后天日期，默认yyyy-MM-dd
     */
    public static String getAfterTomorrowDate() {
        Date date = new Date();
        return FORMAT_DATE.format(new Date(date.getTime() + 2 * 24 * 60 * 60 * 1000));
    }

    /**
     * 把格式为“yyyy-MM-dd HH:mm:ss”的时间转成毫秒数
     *
     * @return
     */
    public static long strTimeToLongTime(String time) {
        try {
            Date date = FORMAT_DATE_TIME.parse(time);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return -1;
    }

    /**
     * 根据用户生日计算年龄
     */
    public static int getAgeByBirthday(Date birthday) {
        Calendar cal = Calendar.getInstance();

        if (cal.before(birthday)) {
            throw new IllegalArgumentException("The birthDay is before Now.It's unbelievable!");
        }

        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH) + 1;
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);

        cal.setTime(birthday);
        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH) + 1;
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

        int age = yearNow - yearBirth;

        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {
                // monthNow==monthBirth
                if (dayOfMonthNow < dayOfMonthBirth) {
                    age--;
                }
            } else {
                // monthNow>monthBirth
                age--;
            }
        }
        return age;
    }


    /**
     * 判断两个日期是否是同一周
     *
     * @param date1
     * @return
     */
    public static boolean isSameDate(String date1) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String currentStr = getCurrent(format);
        Date d1 = null;
        Date d2 = null;
        try {
            d1 = format.parse(date1);
            d2 = format.parse(currentStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(d1);
        cal2.setTime(d2);
        int subYear = cal1.get(Calendar.YEAR) - cal2.get(Calendar.YEAR);
        // subYear==0,说明是同一年
        if (subYear == 0) {
            if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR))
                return true;
        }
        // 例子:cal1是"2005-1-1"，cal2是"2004-12-25"
        // java对"2004-12-25"处理成第52周
        // "2004-12-26"它处理成了第1周，和"2005-1-1"相同了
        // 大家可以查一下自己的日历
        // 处理的比较好
        // 说明:java的一月用"0"标识，那么12月用"11"
        else if (subYear == 1 && cal2.get(Calendar.MONTH) == 11) {
            if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR))
                return true;
        }
        // 例子:cal1是"2004-12-31"，cal2是"2005-1-1"
        else if (subYear == -1 && cal1.get(Calendar.MONTH) == 11) {
            if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR))
                return true;

        }
        return false;
    }

    /**
     * 计算两个日期之间相差的天数
     *
     * @param smdate 较小的时间
     * @param bdate  较大的时间
     * @return 相差天数
     * @throws ParseException
     */
    public static int daysBetween(Date smdate, Date bdate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        smdate = sdf.parse(sdf.format(smdate));
        bdate = sdf.parse(sdf.format(bdate));
        Calendar cal = Calendar.getInstance();
        cal.setTime(smdate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(bdate);
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);

        return Integer.parseInt(String.valueOf(between_days));
    }

    /**
     * 字符串的日期格式的计算
     */
    public static int daysBetween(String smdate, String bdate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(sdf.parse(smdate));
        long time1 = cal.getTimeInMillis();
        cal.setTime(sdf.parse(bdate));
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);

        return Integer.parseInt(String.valueOf(between_days));
    }

    /**
     * 指定日期与今天相差多少天
     *
     * @param smData
     * @return
     * @throws ParseException
     */
    public static int daysBetweenWithToday(String smData) throws ParseException {
        Date date = new Date();
        return daysBetween(smData, formatDate(date, DATE_TIME_yyyy_MM_dd_HH_mm_ss));
    }

    /**
     * 获取当前年月日
     *
     * @return 以yyyy-MM-dd格式返回
     */
    public static String getCurYMD() {
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String monthStr = month < 10 ? "0" + month : "" + month;
        String dayStr = day < 10 ? "0" + day : "" + day;
        return calendar.get(Calendar.YEAR) + "-" + monthStr + "-" + dayStr;
    }

    /**
     * 获取本月开始时间
     *
     * @return
     */
    public static long getCurMonthStartTime() {
        Calendar calendar = Calendar.getInstance();
        int curYear = calendar.get(Calendar.YEAR);
        int curMonth = calendar.get(Calendar.MONTH) + 1;
        String start = curYear + "-" + curMonth + "-01" + " 00:00:00";
        return strTimeToLongTime(start);
    }

    public static long getCurMonthEndTime() {
        Calendar calendar = Calendar.getInstance();
        int curYear = calendar.get(Calendar.YEAR);
        int curMonth = calendar.get(Calendar.MONTH) + 1;
        int curMonthLastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        String end = curYear + "-" + curMonth + "-" + curMonthLastDay + " 23:59:59";
        return strTimeToLongTime(end);
    }

    /**
     * 获取当前年指定月份的开始时间戳，
     *
     * @param month
     * @return
     */
    public static long getAssignMonthStartTime(int month) {
        Calendar calendar = Calendar.getInstance();
        int curYear = calendar.get(Calendar.YEAR);
        String start = curYear + "-" + month + "-01" + " 00:00:00";
        return strTimeToLongTime(start);
    }

    /**
     * 获取当前年的指定月份结束时间戳 注意{@link #getAssignMonthStartTime}
     *
     * @param month
     * @return
     */
    public static long getAssignMonthEndTime(int month) {
        Calendar calendar = Calendar.getInstance();
        int curYear = calendar.get(Calendar.YEAR);
        calendar.set(Calendar.MONTH, month - 1);
        int assignMonthLastDay = calendar.getActualMaximum(Calendar.DATE);
        String end = curYear + "-" + month + "-" + assignMonthLastDay + " 23:59:59";
        return strTimeToLongTime(end);
    }


    public static long getCurDayStartTime() {
        Calendar calendar = Calendar.getInstance();
        int curYear = calendar.get(Calendar.YEAR);
        int curMonth = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String start = curYear + "-" + curMonth + "-" + day + " 00:00:00";
        return strTimeToLongTime(start);
    }


    public static long getCurDayEndTime() {
        Calendar calendar = Calendar.getInstance();
        int curYear = calendar.get(Calendar.YEAR);
        int curMonth = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String end = curYear + "-" + curMonth + "-" + day + " 23:59:59";
        return strTimeToLongTime(end);
    }


}
