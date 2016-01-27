package com.yuedong.youbutie_merchant_android.model.bmob.bean;


import com.yuedong.youbutie_merchant_android.bean.ServiceInfoDetailBean;

import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.datatype.BmobGeoPoint;

/**
 * Created by Administrator on 2015/11/27.
 * 门店表
 *
 * @author 俊鹏
 */
public class Merchant extends BmobObject {
    private String name; // 店名
    private String photo; //店面图片
    private String bankCard; //店收款卡号
    private User user;//门店属于哪个用户的
    private String startTime;//开始营业时间
    private String endTime;//结束营业时间
    private String address;//地址
    private Float star;//门店星级评价
    private BmobGeoPoint location;//坐标
    private String telephone;//联系电话
    private List<String> services;//服务表[“objectId”,“objectId”]
    private List<ServiceInfoDetailBean> serviceInfo;//[{“objectId”:服务objectId, “name”:name,  “state”:闲时＝0/忙时=1}]
    private List<String> staffs;//员工表[“objectId”,“objectId”]
    private MerchantType type;//门店类型
    private String introduced; // 门店广告

    public String getIntroduced() {
        return introduced;
    }

    public void setIntroduced(String introduced) {
        this.introduced = introduced;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getBankCard() {
        return bankCard;
    }

    public void setBankCard(String bankCard) {
        this.bankCard = bankCard;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Float getStar() {
        return star;
    }

    public void setStar(Float star) {
        this.star = star;
    }

    public BmobGeoPoint getLocation() {
        return location;
    }

    public void setLocation(BmobGeoPoint location) {
        this.location = location;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public List<String> getServices() {
        return services;
    }

    public void setServices(List<String> services) {
        this.services = services;
    }

    public List<ServiceInfoDetailBean> getServiceInfo() {
        return serviceInfo;
    }

    public void setServiceInfo(List<ServiceInfoDetailBean> serviceInfo) {
        this.serviceInfo = serviceInfo;
    }

    public List<String> getStaffs() {
        return staffs;
    }

    public void setStaffs(List<String> staffs) {
        this.staffs = staffs;
    }

    public MerchantType getType() {
        return type;
    }

    public void setType(MerchantType type) {
        this.type = type;
    }


    @Override
    public String toString() {
        return "Merchant{" +
                "name='" + name + '\'' +
                ", photo='" + photo + '\'' +
                ", bankCard='" + bankCard + '\'' +
                ", user=" + user +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", address='" + address + '\'' +
                ", star='" + star + '\'' +
                ", location=" + location +
                ", telephone='" + telephone + '\'' +
                ", services=" + services +
                ", serviceInfo=" + serviceInfo +
                ", staffs=" + staffs +
                ", type=" + type +
                '}';
    }
}
