package com.yuedong.youbutie_merchant_android.bean;

public class IncomeDetailListBean {
    private String dayDes;
    private int orderNumber;
    private double totalMoney;
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

    public double getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(double totalMoney) {
        this.totalMoney = totalMoney;
    }

}
