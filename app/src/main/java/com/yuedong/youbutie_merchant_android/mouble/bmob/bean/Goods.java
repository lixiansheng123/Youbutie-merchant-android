package com.yuedong.youbutie_merchant_android.mouble.bmob.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobDate;

/**
 * Created by Administrator on 2015/12/7.
 */
public class Goods extends BmobObject {
    private String title; //标题
    private String photo; //商品图片url
    private Integer money; //兑换需要油点
    private Double price; //市场价格
    private String url; //商品详情Url
    private BmobDate endTime; //下架日期
    private Integer totalCount; //该商品总数量
    private Integer remainCount; //该商品剩余数量
    private String code;// 商品标号


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Integer getMoney() {
        return money;
    }

    public void setMoney(Integer money) {
        this.money = money;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public BmobDate getEndTime() {
        return endTime;
    }

    public void setEndTime(BmobDate endTime) {
        this.endTime = endTime;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Integer getRemainCount() {
        return remainCount;
    }

    public void setRemainCount(Integer remainCount) {
        this.remainCount = remainCount;
    }

    @Override
    public String toString() {
        return "Goods{" +
                "title='" + title + '\'' +
                ", photo='" + photo + '\'' +
                ", money=" + money +
                ", price=" + price +
                ", url='" + url + '\'' +
                ", endTime=" + endTime +
                ", totalCount=" + totalCount +
                ", remainCount=" + remainCount +
                '}';
    }
}
