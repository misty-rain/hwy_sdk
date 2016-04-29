package com.bojoy.bjsdk_mainland_new.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by wutao on 2015/12/24.
 * 账户基本信息
 */
public class PassPort implements Parcelable{
    private String token;
    private String pp;
    private String uid;
    private String authType;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPp() {
        return pp;
    }

    public void setPp(String pp) {
        this.pp = pp;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getBm() {
        return bm;
    }

    public void setBm(String bm) {
        this.bm = bm;
    }

    public String getBe() {
        return be;
    }

    public void setBe(String be) {
        this.be = be;
    }

    public String getAuthType() {
        if(authType == null){
            authType = "";
        }
        return authType;
    }

    public void setAuthType(String authType) {
        this.authType = authType;
    }

    private String mobile;
    private String bm;
    private String be;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.token);
        dest.writeString(this.pp);
        dest.writeString(this.uid);
        dest.writeString(this.mobile);
        dest.writeString(this.bm);
        dest.writeString(this.be);
        dest.writeString(this.authType);
    }

    public PassPort() {
    }

    private PassPort(Parcel in) {
        this.token = in.readString();
        this.pp = in.readString();
        this.uid = in.readString();
        this.mobile = in.readString();
        this.bm = in.readString();
        this.be = in.readString();
        this.authType = in.readString();
    }

    public static final Creator<PassPort> CREATOR = new Creator<PassPort>() {
        public PassPort createFromParcel(Parcel source) {
            return new PassPort(source);
        }

        public PassPort[] newArray(int size) {
            return new PassPort[size];
        }
    };
}
