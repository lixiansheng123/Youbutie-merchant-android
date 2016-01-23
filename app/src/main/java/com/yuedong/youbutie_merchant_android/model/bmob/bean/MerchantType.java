package com.yuedong.youbutie_merchant_android.model.bmob.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2015/11/27.
 * 门店类型表
 *
 * @author 俊鹏
 */
public class MerchantType extends BmobObject {
    private String name; // 名称

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
