package com.bojoy.bjsdk_mainland_new.model.entity;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import com.bojoy.bjsdk_mainland_new.utils.AccountSharePUtils;

/**
 * Created by wutao on 2015/12/24.
 * 临时passport 实体 ，主要用于试玩用户信息的存储
 */
public class TempPassPort implements Parcelable {
    /**
     * 试玩用户填写的正式账号
     * tryChange接口参数1
     */
    private String tempPassport;
    /**
     * 试玩用户填写的密码
     * tryChange接口参数2
     */
    private String tempPwd;


    /**
     * 获取账号后两位
     * @return
     */
    public static String getTryPassportPostfix(Context context) {
        String ppStr = "";
        String tempStr = ppStr.substring(ppStr.length() - 2, ppStr.length());
        return tempStr;
    }


    public String getTempPassport() {
        return tempPassport;
    }

    public void setTempPassport(String tempPassport) {
        this.tempPassport = tempPassport;
    }

    public String getTempPwd() {
        return tempPwd;
    }

    public void setTempPwd(String tempPwd) {
        this.tempPwd = tempPwd;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.tempPassport);
        dest.writeString(this.tempPwd);
    }

    public TempPassPort() {
    }

    private TempPassPort(Parcel in) {
        this.tempPassport = in.readString();
        this.tempPwd = in.readString();
    }

    public static final Creator<TempPassPort> CREATOR = new Creator<TempPassPort>() {
        public TempPassPort createFromParcel(Parcel source) {
            return new TempPassPort(source);
        }

        public TempPassPort[] newArray(int size) {
            return new TempPassPort[size];
        }
    };
}
