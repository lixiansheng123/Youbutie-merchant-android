package com.yuedong.youbutie_merchant_android.model.bmob.bean;

import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2015/11/27.
 * 车类表
 *
 * @author 俊鹏
 */
public class Car extends BmobObject {
    private String name;  // 名称

    private String photo; // 车标志icon

    private String letter; // 大写字母

    private List<String> series; // [carSeries Id,carSeries id,carSeries id]

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    public List<String> getSeries() {
        return series;
    }

    public void setSeries(List<String> series) {
        this.series = series;
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


    @Override
    public String toString() {
        return "Car{" +
                "name='" + name + '\'' +
                ", photo='" + photo + '\'' +
                ", letter='" + letter + '\'' +
                ", series=" + series +
                '}';
    }
}
