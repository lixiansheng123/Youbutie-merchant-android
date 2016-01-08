package com.yuedong.youbutie_merchant_android.mouble.bmob.bean;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobGeoPoint;

public class User extends BmobUser {
    private String nickname; //昵称
    private String photo; //头像
    private Integer age;// 年龄
    private BmobGeoPoint location;// 坐标
    private String address;//所在地址
    //    private Car car;//车型
    private String carNumber;//车牌号
    private String idnumber;//识别号（后6位）
    private String VIN;//车架号（后4位）
    private String strokelength;//车行程（单位km）
    private Integer totalMoney;//总油点
    private Integer type;//用户类型（用户＝1，商家＝2）
    private String carString; // 车型描述
    private Double cash; // 可提现金额
    private Double drawTotalCash; // 提现总金额
    private Integer drawCount; // 提现次数
    private String bankCard; // 提现银行卡
    private String cardName; // 银行卡持卡人姓名
    private String bankName; // 开户行名称

    public Double getCash() {
        return cash;
    }

    public void setCash(Double cash) {
        this.cash = cash;
    }

    public Double getDrawTotalCash() {
        return drawTotalCash;
    }

    public void setDrawTotalCash(Double drawTotalCash) {
        this.drawTotalCash = drawTotalCash;
    }

    public Integer getDrawCount() {
        return drawCount;
    }

    public void setDrawCount(Integer drawCount) {
        this.drawCount = drawCount;
    }

    public String getBankCard() {
        return bankCard;
    }

    public void setBankCard(String bankCard) {
        this.bankCard = bankCard;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }


    //    public User() {
//        setTableName("_User");
//    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getCarString() {
        return carString;
    }

    public void setCarString(String carString) {
        this.carString = carString;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public Integer getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(Integer totalMoney) {
        this.totalMoney = totalMoney;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public BmobGeoPoint getLocation() {
        return location;
    }

    public void setLocation(BmobGeoPoint location) {
        this.location = location;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

//    public Car getCar() {
//        return car;
//    }
//
//    public void setCar(Car car) {
//        this.car = car;
//    }


    public String getIdnumber() {
        return idnumber;
    }

    public void setIdnumber(String idnumber) {
        this.idnumber = idnumber;
    }

    public String getVIN() {
        return VIN;
    }

    public void setVIN(String VIN) {
        this.VIN = VIN;
    }

    public String getStrokelength() {
        return strokelength;
    }

    public void setStrokelength(String strokelength) {
        this.strokelength = strokelength;
    }


    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }


    @Override
    public String toString() {
        return "User{" +
                "nickname='" + nickname + '\'' +
                ", photo='" + photo + '\'' +
                ", location=" + location +
                ", address='" + address + '\'' +
//                ", car=" + car +
                ", carNumber='" + carNumber + '\'' +
                ", idnumber='" + idnumber + '\'' +
                ", VIN='" + VIN + '\'' +
                ", strokelength='" + strokelength + '\'' +
                ", totalMoney=" + totalMoney +
                ", type=" + type +
                '}';
    }
}
