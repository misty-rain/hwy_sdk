package com.bojoy.bjsdk_mainland_new.model.entity;

import android.os.Parcel;
import android.os.Parcelable;
import com.bojoy.bjsdk_mainland_new.support.fastjson.annotation.JSONCreator;
import com.bojoy.bjsdk_mainland_new.support.fastjson.annotation.JSONField;
import com.bojoy.bjsdk_mainland_new.support.fastjson.annotation.JSONType;

import java.util.HashMap;
import java.util.Map;

/**
 * 返回消息json 基类
 * Created by wutao on 2016/1/4.
 */
public class BackResultBean implements Parcelable {
    private Integer code;
    private String msg;
    private String obj;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getObj() {
        return obj;
    }

    public void setObj(String  obj) {
        this.obj = obj;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.code);
        dest.writeString(this.msg);
        dest.writeString(this.obj);
    }

    public BackResultBean() {
    }

    private BackResultBean(Parcel in) {
        this.code = in.readInt();
        this.msg = in.readString();
        this.obj = in.readString();
    }

    public static final Creator<BackResultBean> CREATOR = new Creator<BackResultBean>() {
        public BackResultBean createFromParcel(Parcel source) {
            return new BackResultBean(source);
        }

        public BackResultBean[] newArray(int size) {
            return new BackResultBean[size];
        }
    };
}
