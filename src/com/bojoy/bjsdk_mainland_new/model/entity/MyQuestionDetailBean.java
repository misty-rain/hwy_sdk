package com.bojoy.bjsdk_mainland_new.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 处理问题聊天记录
 * Created by shenliuyong on 2016/2/1.
 */
public class MyQuestionDetailBean implements Parcelable {

    //聊天编号
    private String id;
    //问题记录ID(不能为空)
    private String fid;
    //聊天内容
    private String content;
    //发送方
    private String disposer;
    //发送时间
    private String disposetime;
    //图片URL
    private String attachment;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDisposer() {
        return disposer;
    }

    public void setDisposer(String disposer) {
        this.disposer = disposer;
    }

    public String getDisposetime() {
        return disposetime;
    }

    public void setDisposetime(String disposetime) {
        this.disposetime = disposetime;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.fid);
        dest.writeString(this.content);
        dest.writeString(this.disposer);
        dest.writeString(this.disposetime);
        dest.writeString(this.attachment);
    }

    public MyQuestionDetailBean() {
    }

    protected MyQuestionDetailBean(Parcel in) {
        this.id = in.readString();
        this.fid = in.readString();
        this.content = in.readString();
        this.disposer = in.readString();
        this.disposetime = in.readString();
        this.attachment = in.readString();
    }

    public static final Parcelable.Creator<MyQuestionDetailBean> CREATOR = new Parcelable.Creator<MyQuestionDetailBean>() {
        public MyQuestionDetailBean createFromParcel(Parcel source) {
            return new MyQuestionDetailBean(source);
        }

        public MyQuestionDetailBean[] newArray(int size) {
            return new MyQuestionDetailBean[size];
        }
    };

}
