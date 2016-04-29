package com.bojoy.bjsdk_mainland_new.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by wutao on 2015/12/15.
 * 充值数据模型
 * 实现parcelable 序列化接口，相比Serializable ，更使用与Android 平台，效率更高
 */
public class RechargeData  implements Parcelable{
    private String orderSerial;
    private String productId;
    private String productName;
    private int count;
    private double money;
    private String serverId;
    private String roleId;
    private String rechargeUrl;

    public RechargeData(String orderSerial, String productId, String productName, int count,
                        double money, String serverId, String roleId) {
        this.orderSerial = orderSerial;
        this.productId = productId;
        this.productName = productName;
        this.count = count;
        this.money = money;
        this.serverId = serverId;
        this.roleId = roleId;
    }

    public String getOrderSerial() {
        return orderSerial;
    }

    public void setOrderSerial(String orderSerial) {
        this.orderSerial = orderSerial;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getRechargeUrl() {
        return rechargeUrl;
    }

    public void setRechargeUrl(String rechargeUrl) {
        this.rechargeUrl = rechargeUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.orderSerial);
        dest.writeString(this.productId);
        dest.writeString(this.productName);
        dest.writeInt(this.count);
        dest.writeDouble(this.money);
        dest.writeString(this.serverId);
        dest.writeString(this.serverId);
        dest.writeString(this.roleId);
        dest.writeString(this.rechargeUrl);
    }

    private RechargeData(Parcel in) {
        this.orderSerial = in.readString();
        this.productId = in.readString();
        this.productName = in.readString();
        this.count = in.readInt();
        this.money = in.readDouble();
        this.serverId = in.readString();
        this.roleId = in.readString();
        this.rechargeUrl = in.readString();
    }

    public static final Creator<RechargeData> CREATOR = new Creator<RechargeData>() {
        public RechargeData createFromParcel(Parcel source) {
            return new RechargeData(source);
        }

        public RechargeData[] newArray(int size) {
            return new RechargeData[size];
        }
    };
}
