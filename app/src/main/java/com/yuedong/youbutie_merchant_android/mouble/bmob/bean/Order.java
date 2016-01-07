package com.yuedong.youbutie_merchant_android.mouble.bmob.bean;


import com.yuedong.youbutie_merchant_android.bean.ServiceInfoDetailBean;

import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobDate;

public class Order extends BmobObject {
    private String orderNumber; // 订单号
    private User user; // 用户
    private Merchant merchant;// 门店
    private List<ServiceInfoDetailBean> services;// 需要的服务
    private BmobDate orderTime; // 订单时间
    private Double price; // 单价
    private Integer state; // 状态  1下单 2商家接单 3服务完成 4已取车(收款成功) 5收款失败
    private Integer payWay; // 支付类型（1=支付宝，2=微信）
    private List<String> serviceIds;// 服务的Id


    public List<String> getServiceIds() {
        return serviceIds;
    }

    public void setServiceIds(List<String> serviceIds) {
        this.serviceIds = serviceIds;
    }

    public int buyNum; // 来店次数


    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Merchant getMerchant() {
        return merchant;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    public List<ServiceInfoDetailBean> getServices() {
        return services;
    }

    public void setServices(List<ServiceInfoDetailBean> services) {
        this.services = services;
    }

    public BmobDate getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(BmobDate orderTime) {
        this.orderTime = orderTime;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getPayWay() {
        return payWay;
    }

    public void setPayWay(Integer payWay) {
        this.payWay = payWay;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderNumber='" + orderNumber + '\'' +
                ", user=" + user +
                ", merchant=" + merchant +
                ", services=" + services +
                ", orderTime=" + orderTime +
                ", price=" + price +
                ", state=" + state +
                ", payWay=" + payWay +
                ", buyNum=" + buyNum +
                '}';
    }
}
