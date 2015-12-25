package com.yuedong.youbutie_merchant_android.bean;

import java.io.Serializable;

/**
 * 服务详细
 */
public class ServiceInfoDetailBean implements Serializable {
    public String name;
    public String icon;
    public Double state, price;
    public String objectId;

    @Override
    public String toString() {
        return "ServiceInfoDetailBean{" +
                "name='" + name + '\'' +
                ", icon='" + icon + '\'' +
                ", state=" + state +
                ", price=" + price +
                ", objectId='" + objectId + '\'' +
                '}';
    }
}
