package com.yuedong.youbutie_merchant_android.mouble.bmob.bean;

import java.util.List;

import cn.bmob.v3.BmobObject;

public class Advertisement extends BmobObject {
    private Integer money; //油点
    private String photo; // 广告缩略图
    private Integer readCount; //看过的人
    private String url; //广告详情页
    private Ticket ticket; //卡券
    private String title; // 标题
    private List<String> relatedADs; //相关的广告[ad objectid1,ad objectid2,… ]

    public Integer getMoney() {
        return money;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setMoney(Integer money) {
        this.money = money;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Integer getReadCount() {
        return readCount;
    }

    public void setReadCount(Integer readCount) {
        this.readCount = readCount;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public List<String> getRelatedADs() {
        return relatedADs;
    }

    public void setRelatedADs(List<String> relatedADs) {
        this.relatedADs = relatedADs;
    }

    @Override
    public String toString() {
        return "Advertisement{" +
                "money=" + money +
                ", photo='" + photo + '\'' +
                ", readCount=" + readCount +
                ", url='" + url + '\'' +
                ", ticket=" + ticket +
                ", relatedAD=" + relatedADs +
                '}';
    }
}
