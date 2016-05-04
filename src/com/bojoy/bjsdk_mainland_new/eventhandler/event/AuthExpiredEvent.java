package com.bojoy.bjsdk_mainland_new.eventhandler.event;

/**
 * Created by wutao on 2016/5/4.
 */
public class AuthExpiredEvent {
    private String code;

    public AuthExpiredEvent(String code, String msg) {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private String message;


}
