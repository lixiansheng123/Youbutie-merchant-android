package com.yuedong.youbutie_merchant_android.bean;

import com.yuedong.youbutie_merchant_android.model.bmob.bean.User;

public class InviteMemberListBean extends User {
    public String remark;// 备注
    public boolean regist; // 是否加入了油补贴
    public int bg;// 对应背景

    public void setCreatedAt(String createdAt) {
        super.setCreatedAt(createdAt);
    }
}
