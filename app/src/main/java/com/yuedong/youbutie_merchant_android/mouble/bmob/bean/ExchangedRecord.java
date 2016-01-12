package com.yuedong.youbutie_merchant_android.mouble.bmob.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobDate;

/**
 * 兑换记录
 */
public class ExchangedRecord extends BmobObject {
    private Goods goods;//兑换的商品
    private User user; // 兑换的用户
    private BmobDate exchangedTime;// 兑换时间
    private Integer count;// 兑换数量
    private Integer totalMoney;// 兑换油点
    private String recordNumber; // 兑换号
    private Integer state = 1; // 兑换状态 1未领取 2已领取

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getRecordNumber() {
        return recordNumber;
    }

    public void setRecordNumber(String recordNumber) {
        this.recordNumber = recordNumber;
    }

    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BmobDate getExchangedTime() {
        return exchangedTime;
    }

    public void setExchangedTime(BmobDate exchangedTime) {
        this.exchangedTime = exchangedTime;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(Integer totalMoney) {
        this.totalMoney = totalMoney;
    }
}
