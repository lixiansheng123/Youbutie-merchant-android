package com.yuedong.youbutie_merchant_android.mouble.bmob.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobDate;

public class Appraise extends BmobObject {
    private User user; // 评价用户
    private BmobDate appraiseTime; //评价时间
    private Merchant merchant;//被评价的门店
    private Float star; //评价星级
    private String content; // 评价内容
    private Order order;

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BmobDate getAppraiseTime() {
        return appraiseTime;
    }

    public void setAppraiseTime(BmobDate appraiseTime) {
        this.appraiseTime = appraiseTime;
    }

    public Merchant getMerchant() {
        return merchant;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    public Float getStar() {
        return star;
    }

    public void setStar(Float star) {
        this.star = star;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Appraise{" +
                "user=" + user +
                ", appraiseTime=" + appraiseTime +
                ", merchant=" + merchant +
                ", star=" + star +
                ", content='" + content + '\'' +
                '}';
    }
}
