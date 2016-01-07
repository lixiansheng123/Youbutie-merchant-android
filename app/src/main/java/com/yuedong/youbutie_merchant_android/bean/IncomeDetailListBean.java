package com.yuedong.youbutie_merchant_android.bean;

public class IncomeDetailListBean {
    private int month;
    private String dayDes;
    private int orderNumber;
    private double totalMoney;

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

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

    public double getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(double totalMoney) {
        this.totalMoney = totalMoney;
    }
}
