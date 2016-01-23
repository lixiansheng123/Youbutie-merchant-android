package com.yuedong.youbutie_merchant_android.model.bmob.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2015/11/27.
 * 服务表
 *
 * @author 俊鹏
 */
public class ServiceInfo extends BmobObject {
    private String name; // 服务名称
    private String icon; // 服务icon

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }


    @Override
    public String toString() {
        return "ServiceInfo{" +
                "name='" + name + '\'' +
                ", icon='" + icon + '\'' +
                '}';
    }
}
