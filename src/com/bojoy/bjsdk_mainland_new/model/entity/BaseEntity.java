package com.bojoy.bjsdk_mainland_new.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by wutao on 2015/11/26.
 *  数据实体基类
 */
public class BaseEntity implements Parcelable {
    private int errorCode;
    private String errorMessage;

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }




    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.errorCode);
        dest.writeString(this.errorMessage);
    }

    public BaseEntity() {
    }

    private BaseEntity(Parcel in) {
        this.errorCode = in.readInt();
        this.errorMessage = in.readString();
    }

    public static final Creator<BaseEntity> CREATOR = new Creator<BaseEntity>() {
        public BaseEntity createFromParcel(Parcel source) {
            return new BaseEntity(source);
        }

        public BaseEntity[] newArray(int size) {
            return new BaseEntity[size];
        }
    };
}
