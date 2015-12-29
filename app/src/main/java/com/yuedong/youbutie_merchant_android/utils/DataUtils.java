package com.yuedong.youbutie_merchant_android.utils;

import com.yuedong.youbutie_merchant_android.bean.ServiceInfoDetailBean;

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
        calendar.set(Calendar.MONTH, month - 1);
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

    /**
     * 获取短信模版item
     *
     * @return
     */
    public static List<ServiceInfoDetailBean> getSmsSelectItem() {
        List<ServiceInfoDetailBean> serviceInfoDetailBeans = new ArrayList<ServiceInfoDetailBean>();
        ServiceInfoDetailBean serviceInfoDetailBean = new ServiceInfoDetailBean();
        serviceInfoDetailBean.name = "洗车";
        // 当一个短信模版内容
        serviceInfoDetailBean.icon = "洗车短信模版";
        serviceInfoDetailBeans.add(serviceInfoDetailBean);
        ServiceInfoDetailBean serviceInfoDetailBean2 = new ServiceInfoDetailBean();
        serviceInfoDetailBean2.name = "保养";
        // 当一个短信模版内容
        serviceInfoDetailBean2.icon = "保养短信模版";
        serviceInfoDetailBeans.add(serviceInfoDetailBean2);

        ServiceInfoDetailBean serviceInfoDetailBean3 = new ServiceInfoDetailBean();
        serviceInfoDetailBean3.name = "美容";
        // 当一个短信模版内容
        serviceInfoDetailBean3.icon = "美容短信模版";
        serviceInfoDetailBeans.add(serviceInfoDetailBean3);

        ServiceInfoDetailBean serviceInfoDetailBean4 = new ServiceInfoDetailBean();
        serviceInfoDetailBean4.name = "其他服务";
        // 当一个短信模版内容
        serviceInfoDetailBean4.icon = "其他服务短信模版";
        serviceInfoDetailBeans.add(serviceInfoDetailBean4);
        return serviceInfoDetailBeans;
    }
}
