package com.bojoy.bjsdk_mainland_new.model.entity;


import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import com.bojoy.bjsdk_mainland_new.utils.StringUtility;

/**
 * 用户信息数据实体
 */
public class UserData implements Parcelable {

	/**
	 *  昵称
	 */
	public String nick;
	
	/**
	 * 生日 yyyyMMdd
	 */
	public String birth;
	
	/**
	 * 性别
	 */
	public String sex;

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getBirth() {
		return birth;
	}

	public void setBirth(String birth) {
		this.birth = birth;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getFaceUrl() {
		return faceUrl;
	}

	public void setFaceUrl(String faceUrl) {
		this.faceUrl = faceUrl;
	}

	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	/**
	 * 图片名称
	 */
	public String faceUrl;
	
	/**
	 * 用户id号
	 */
	public long uid;


	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.nick);
		dest.writeString(this.birth);
		dest.writeString(this.sex);
		dest.writeString(this.faceUrl);
		dest.writeLong(this.uid);
	}

	public UserData() {
	}

	private UserData(Parcel in) {
		this.nick = in.readString();
		this.birth = in.readString();
		this.sex = in.readString();
		this.faceUrl = in.readString();
		this.uid = in.readLong();
	}

	public static final Creator<UserData> CREATOR = new Creator<UserData>() {
		public UserData createFromParcel(Parcel source) {
			return new UserData(source);
		}

		public UserData[] newArray(int size) {
			return new UserData[size];
		}
	};
}
