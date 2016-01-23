package com.yuedong.youbutie_merchant_android.model.bmob.bean;

import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobDate;

/**
 * Created by Administrator on 2015/12/3.
 */
public class Ticket extends BmobObject {
    private String title; //标题
    private String introduce; //使用规则描述
    private BmobDate endTime;//有效期
    private Integer price; //价值
    private List<String> detailTickets; //ticket详细列表[ objectid1，objectid2，…]


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public BmobDate getEndTime() {
        return endTime;
    }

    public void setEndTime(BmobDate endTime) {
        this.endTime = endTime;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public List<String> getDetailTickets() {
        return detailTickets;
    }

    public void setDetailTickets(List<String> detailTickets) {
        this.detailTickets = detailTickets;
    }


    @Override
    public String toString() {
        return "Ticket{" +
                "title='" + title + '\'' +
                ", introduce='" + introduce + '\'' +
                ", endTime=" + endTime +
                ", price=" + price +
                ", detailTickets=" + detailTickets +
                '}';
    }
}
