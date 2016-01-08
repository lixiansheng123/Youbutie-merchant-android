package com.yuedong.youbutie_merchant_android.mouble.bmob.bean;

import android.renderscript.BaseObj;

import cn.bmob.v3.BmobObject;

public class DrawMoneyRecord extends BmobObject {
    private Integer money; // 提现额度
    private Merchant merchant;// 门店
    private Integer state; //提现状态（0=处理中，1=提现成功，2=提现失败）

    public Integer getMoney() {
        return money;
    }

    public void setMoney(Integer money) {
        this.money = money;
    }

    public Merchant getMerchant() {
        return merchant;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
}
