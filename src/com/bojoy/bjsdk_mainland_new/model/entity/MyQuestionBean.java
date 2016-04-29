package com.bojoy.bjsdk_mainland_new.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by shenliuyong on 2015/12/30.
 * 用户发送问题时的数据
 */
public class MyQuestionBean implements Parcelable {

    private String id;
    private String gameid;
    private String serverid;
    private String type;
    private String subtype;
    private String title;
    private String content;
    private String passport;
    private String rolename;
    private String state;
    private String create;
    private String read;
    private String evaluate;
    private String attachment;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGameid() {
        return gameid;
    }

    public void setGameid(String gameid) {
        this.gameid = gameid;
    }

    public String getServerid() {
        return serverid;
    }

    public void setServerid(String serverid) {
        this.serverid = serverid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSubtype() {
        return subtype;
    }

    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPassport() {
        return passport;
    }

    public void setPassport(String passport) {
        this.passport = passport;
    }

    public String getRolename() {
        return rolename;
    }

    public void setRolename(String rolename) {
        this.rolename = rolename;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCreate() {
        return create;
    }

    public void setCreate(String create) {
        this.create = create;
    }

    public String getRead() {
        return read;
    }

    public void setRead(String read) {
        this.read = read;
    }

    public String getEvaluate() {
        return evaluate;
    }

    public void setEvaluate(String evaluate) {
        this.evaluate = evaluate;
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
        dest.writeString(this.gameid);
        dest.writeString(this.serverid);
        dest.writeString(this.type);
        dest.writeString(this.subtype);
        dest.writeString(this.title);
        dest.writeString(this.content);
        dest.writeString(this.passport);
        dest.writeString(this.rolename);
        dest.writeString(this.state);
        dest.writeString(this.create);
        dest.writeString(this.read);
        dest.writeString(this.evaluate);
        dest.writeString(this.attachment);
    }

    public MyQuestionBean() {
    }

    protected MyQuestionBean(Parcel in) {
        this.id = in.readString();
        this.gameid = in.readString();
        this.serverid = in.readString();
        this.type = in.readString();
        this.subtype = in.readString();
        this.title = in.readString();
        this.content = in.readString();
        this.passport = in.readString();
        this.rolename = in.readString();
        this.state = in.readString();
        this.create = in.readString();
        this.read = in.readString();
        this.evaluate = in.readString();
        this.attachment = in.readString();
    }

    public static final Parcelable.Creator<MyQuestionBean> CREATOR = new Parcelable.Creator<MyQuestionBean>() {
        public MyQuestionBean createFromParcel(Parcel source) {
            return new MyQuestionBean(source);
        }

        public MyQuestionBean[] newArray(int size) {
            return new MyQuestionBean[size];
        }
    };
}
