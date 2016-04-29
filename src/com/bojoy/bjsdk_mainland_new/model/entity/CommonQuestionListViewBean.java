package com.bojoy.bjsdk_mainland_new.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class CommonQuestionListViewBean {
	private String ID;
	private String Question;
	private String Answer;

	public CommonQuestionListViewBean() {
	}

	public CommonQuestionListViewBean(String id, String question, String answer) {
		this.ID = id;
		this.Question = question;
		this.Answer = answer;
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getQuestion() {
		return Question;
	}

	public void setQuestion(String question) {
		Question = question;
	}

	public String getAnswer() {
		return Answer;
	}

	public void setAnswer(String answer) {
		Answer = answer;
	}

}
