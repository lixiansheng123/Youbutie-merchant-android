package com.yuedong.youbutie_merchant_android.mouble.bmob.bean;

import cn.bmob.v3.BmobObject;

public class MoneyContribute extends BmobObject {
    private Advertisement advertisement; //广告
    private User user; // 用户
    private Merchant merchant; // 门店
    private Integer money; // 油点

    public Advertisement getAdvertisement() {
        return advertisement;
    }

    public void setAdvertisement(Advertisement advertisement) {
        this.advertisement = advertisement;
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

    public Integer getMoney() {
        return money;
    }

    public void setMoney(Integer money) {
        this.money = money;
    }
}
