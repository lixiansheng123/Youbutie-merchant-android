package com.yuedong.youbutie_merchant_android.bean;

import com.yuedong.youbutie_merchant_android.mouble.bmob.bean.User;

public class MoneyContributionBean {
    private User user; // 用户
    private int totalContributionMoney;// 总贡献油点

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getTotalContributionMoney() {
        return totalContributionMoney;
    }

    public void setTotalContributionMoney(int totalContributionMoney) {
        this.totalContributionMoney = totalContributionMoney;
    }
}
