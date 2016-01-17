package com.yuedong.youbutie_merchant_android.bean;

import android.net.Uri;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/12/31.
 */
public class PhoneAddressBookBean implements Serializable {
    private String phoneId;
    private String contactName;
    private String contactid;
    private String photoid;
    private Uri contactPhoto;
    private String phoneNumber;
    private String sortLetters;

    public String getPhoneId() {
        return phoneId;
    }

    public void setPhoneId(String phoneId) {
        this.phoneId = phoneId;
    }

    public String getSortLetters() {
        return sortLetters;
    }

    public void setSortLetters(String sortLetters) {
        this.sortLetters = sortLetters;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactid() {
        return contactid;
    }

    public void setContactid(String contactid) {
        this.contactid = contactid;
    }

    public String getPhotoid() {
        return photoid;
    }

    public void setPhotoid(String photoid) {
        this.photoid = photoid;
    }

    public Uri getContactPhoto() {
        return contactPhoto;
    }

    public void setContactPhoto(Uri contactPhoto) {
        this.contactPhoto = contactPhoto;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return "PhoneAddressBookBean{" +
                "phoneId='" + phoneId + '\'' +
                ", contactName='" + contactName + '\'' +
                ", contactid='" + contactid + '\'' +
                ", photoid='" + photoid + '\'' +
                ", contactPhoto=" + contactPhoto +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", sortLetters='" + sortLetters + '\'' +
                '}';
    }
}
