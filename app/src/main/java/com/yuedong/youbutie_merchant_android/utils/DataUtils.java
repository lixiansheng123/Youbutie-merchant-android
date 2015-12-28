package com.yuedong.youbutie_merchant_android.utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DataUtils {


    /**
     * 获取指定年份到当前年份的一个集合
     */
    public static List<String> getYears(int minYear) {
        List<String> years = new ArrayList<String>();
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        while (currentYear >= minYear) {
            years.add(0, "" + currentYear);
            currentYear--;
        }
        return years;
    }

    /**
     * 获取月份
     */
    public static List<String> getMonth() {
        List<String> month = new ArrayList<String>();
        for (int i = 1; i <= 12; i++) {
            if (i < 10) {
                month.add("0" + i);
            } else {
                month.add("" + i);
            }
        }
        return month;
    }

    /**
     * 获取日子 根据年份和月份
     */
    public static List<String> getDays(int year, int month) {
        List<String> days = new ArrayList<String>();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONDAY, month - 1);
        int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int i = 1; i <= maxDay; i++) {
            if (i < 10) {
                days.add("0" + i);
            } else {
                days.add("" + i);
            }
        }
        return days;
    }

}
