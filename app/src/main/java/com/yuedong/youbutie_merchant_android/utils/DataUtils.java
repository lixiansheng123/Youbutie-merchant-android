package com.yuedong.youbutie_merchant_android.utils;

import com.yuedong.youbutie_merchant_android.bean.ServiceInfoDetailBean;
import com.yuedong.youbutie_merchant_android.model.bmob.bean.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DataUtils {

    /**
     * 获取24小时
     *
     * @return
     */
    public static List<String> getHours() {
        List<String> data = new ArrayList<String>();
        for (int i = 1; i <= 24; i++) {
            data.add(i + "");
        }
        return data;
    }

    /**
     * 获取分钟集
     *
     * @return
     */
    public static List<String> getMinutes() {
        List<String> data = new ArrayList<String>();
        for (int i = 0; i < 60; i++) {
            String minute = "";
            if (i < 10) {
                minute = "0" + i;
            } else
                minute = i + "";
            data.add(minute);
        }
        return data;
    }


    /**
     * 获取指定年份到当前年份的一个集合
     */
    public static List<String> getYears(int minYear) {
        List<String> years = new ArrayList<String>();
//        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
//        while (currentYear >= minYear) {
//            years.add(0, "" + currentYear);
//            currentYear--;
//        }
        for (int i = 0; i < 3; i++) {
            years.add("" + (minYear + i));
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
        List<ServiceInfoDetailBean> data = new ArrayList<ServiceInfoDetailBean>();
        ServiceInfoDetailBean serviceInfoDetailBean = new ServiceInfoDetailBean();
        serviceInfoDetailBean.name = "洗车";
        // 当一个短信模版内容
        serviceInfoDetailBean.icon = "洗车：亲爱的车主，元旦洗车大酬宾，1月1日——1月10日期间，会员可享受19.9元精致洗车服务 + 车内消毒1次，无须预约。[%s]";
        data.add(serviceInfoDetailBean);
        ServiceInfoDetailBean serviceInfoDetailBean2 = new ServiceInfoDetailBean();
        serviceInfoDetailBean2.name = "保养";
        // 当一个短信模版内容
        serviceInfoDetailBean2.icon = "亲爱的车主，来保养你的爱车吧，原价390，现在只要260，无须预约，即刻享受。[%s]";
        data.add(serviceInfoDetailBean2);

        ServiceInfoDetailBean serviceInfoDetailBean3 = new ServiceInfoDetailBean();
        serviceInfoDetailBean3.name = "美容";
        // 当一个短信模版内容
        serviceInfoDetailBean3.icon = "亲爱的车主，来给你的爱车做个美容吧，原价390的套餐，现在只要260，无须预约，即刻享受。[%s]";
        data.add(serviceInfoDetailBean3);

        ServiceInfoDetailBean serviceInfoDetailBean4 = new ServiceInfoDetailBean();
        serviceInfoDetailBean4.name = "其他服务";
        // 当一个短信模版内容
        serviceInfoDetailBean4.icon = "亲爱的车主,...";
        data.add(serviceInfoDetailBean4);
        return data;
    }

    /**
     * 解析user
     *
     * @param job
     * @return
     * @throws JSONException
     */
    public static User parseUser(JSONObject job) throws JSONException {
        User user = new User();
        boolean nicknameHas = job.has("nickname");
        if (nicknameHas)
            user.setNickname(job.getString("nickname"));
        boolean photoHas = job.has("photo");
        if (photoHas)
            user.setPhoto(job.getString("photo"));
        boolean mobilePhoneNumberHas = job.has("mobilePhoneNumber");
        if (mobilePhoneNumberHas)
            user.setMobilePhoneNumber(job.getString("mobilePhoneNumber"));
        boolean carNumberHas = job.has("carNumber");
        if (carNumberHas)
            user.setCarNumber(job.getString("carNumber"));
        boolean carStringHas = job.has("carString");
        if (carStringHas)
            user.setCarString(job.getString("carString"));
        user.setObjectId(job.getString("objectId"));
        boolean strokeLengthHas = job.has("strokeLength");
        if (strokeLengthHas)
            user.setStrokeLength(job.getInt("strokeLength"));
        boolean totalMoneyHas = job.has("totalMoney");
        if (totalMoneyHas)
            user.setTotalMoney(job.getInt("totalMoney"));
        boolean vinHas = job.has("vin");
        if (vinHas)
            user.setVIN(job.getString("vin"));
        boolean typeHas = job.has("type");
        if (typeHas)
            user.setType(job.getInt("type"));
        boolean addressHas = job.has("address");
        if (addressHas)
            user.setAddress(job.getString("address"));
        boolean ageHas = job.has("age");
        if (ageHas)
            user.setAge(job.getInt("age"));
        boolean bankNameHas = job.has("bankName");
        if (bankNameHas)
            user.setBankName(job.getString("bankName"));
        boolean cardNameHas = job.has("cardName");
        if (cardNameHas)
            user.setCardName(job.getString("cardName"));
        boolean bankCardHas = job.has("bankCard");
        if (bankCardHas)
            user.setBankCard(job.getString("bankCard"));
        return user;
    }
}
