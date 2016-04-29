package com.bojoy.bjsdk_mainland_new.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by wutao on 2015/12/24.
 * 账户基本信息
 */
public class AccountPassPort implements Parcelable{
    private String token;
    private String pp;
    private String uid;

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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.token);
        dest.writeString(this.pp);
        dest.writeString(this.uid);

    }

    public AccountPassPort() {
    }

    private AccountPassPort(Parcel in) {
        this.token = in.readString();
        this.pp = in.readString();
        this.uid = in.readString();

    }

    public static final Creator<AccountPassPort> CREATOR = new Creator<AccountPassPort>() {
        public AccountPassPort createFromParcel(Parcel source) {
            return new AccountPassPort(source);
        }

        public AccountPassPort[] newArray(int size) {
            return new AccountPassPort[size];
        }
    };
}
