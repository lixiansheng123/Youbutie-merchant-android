package com.yuedong.youbutie_merchant_android.mouble.bmob.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobDate;

/**
 * 会员
 */
public class Vips extends BmobObject {
    private BmobDate validity; // 有效日期
    private Merchant merchant;
    private User user;
    private Car car;

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public BmobDate getValidity() {
        return validity;
    }

    public void setValidity(BmobDate validity) {
        this.validity = validity;
    }

    public Merchant getMerchant() {
        return merchant;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
