package com.yuedong.youbutie_merchant_android.bean;

import com.yuedong.youbutie_merchant_android.mouble.bmob.bean.Order;

public class IncomeDetailListBean {
    private String dayDes;
    private int orderNumber;
    private int totalMoney;
    private String createdAt;

    public String getDayDes() {
        return dayDes;
    }

    public void setDayDes(String dayDes) {
        this.dayDes = dayDes;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    public int getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(int totalMoney) {
        this.totalMoney = totalMoney;
    }

}
