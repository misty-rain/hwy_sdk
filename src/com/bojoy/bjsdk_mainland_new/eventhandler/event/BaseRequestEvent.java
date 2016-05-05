package com.bojoy.bjsdk_mainland_new.eventhandler.event;

/**
 * Created by wutao on 2015/11/26.
 * 应用基本请求回话类型 ，用来处理在Presenter中 处理 不同的返回 结果
 */
public class BaseRequestEvent {
    public static final int REQUEST_INIT                       = 1;
    public static final int REQUEST_APP_CHECK_UPDATE           = 2;
    public static final int REQUEST_REGISTER                   = 3;
    public static final int REQUEST_ONE_KEY_INFO               = 4;
    public static final int REQUEST_ONE_KEY_CHECK              = 5;
    public static final int REQUEST_RECEIVE_CHECK_CODE         = 6;
    public static final int REQUEST_MOBILE_REGISTER_CHECK      = 7;
    public static final int REQUEST_LOGIN                      = 8;
    public static final int REQUEST_AUTO_LOGIN                 = 9;
    public static final int REQUEST_FIND_PASSWORD_VERIFY_CODE  = 10;
    public static final int REQUEST_RESET_PASSWORD             = 11;
    public static final int REQUEST_LOGOUT                     = 12;
    public static final int REQUEST_MOBILE_REGISTER            = 13;
    //public static final int Request_Password_Check_Code        = 14;
    public static final int REQUEST_PASSWORD_MODIFY            = 15;
    public static final int REQUEST_GET_ACCOUNT_INFO           = 16;
    public static final int REQUEST_BINDPHONE                  = 17;
    public static final int REQUEST_BINDPHONE_CHECK_CODE       = 18;
    public static final int REQUEST_UNBINDPHONE_VERIFY_CODE    = 19;
    public static final int GETREQUEST_UNBINDPHONE_VERIFY_CODE_CHECK   = 20;
    public static final int REQUEST_SEND_QUESTION              = 21;
    public static final int REQUEST_GET_QUESTIONDETAIL         = 22;
    public static final int REQUEST_GET_MYQUESTION_LIST        = 23;
    public static final int REQUEST_QUESTION_EVALUATE          = 24;
    public static final int REQUEST_MARK_QUESTION_READED       = 25;
    public static final int REQUEST_APPEND_QUESTION            = 26;
    public static final int REQUEST_PUBLISH_WISH               = 27;
    public static final int REQUEST_MY_WISH_LIST               = 28;
    public static final int REQUEST_PF_USER_INFO               = 29;
    public static final int REQUEST_MY_ATTENT_UID_LIST         = 30;
    public static final int REQUEST_PF_OTHER_USERS_INFO        = 31;
    public static final int REQUEST_PF_MODIFY_USER_INFO        = 32;
    public static final int REQUEST_PF_REGISTER                = 33;
    public static final int REQUEST_PF_UPLOAD_FACE_IMAGE       = 34;
    public static final int REQUEST_MESSAGE_NOTIFY             = 35;
    public static final int REQUEST_MESSAGE_OFFLINE            = 36;
    public static final int REQUEST_FIRST_DEPLOY               = 37;
    public static final int REQUEST_CAN_PUBLISH_WISH           = 38;
    public static final int REQUEST_USER_BALANCE               = 39;
    public static final int REQUEST_PAY_ORDER              	   = 40;
    public static final int REQUEST_RECHARGE_ORDER             = 41;
    public static final int REQUEST_VIP_LEVEL		           = 42;



    public static final int REQUEST_TRY_LOGIN		           = 43;
    public static final int REQUEST_TRY_CHANGE		           = 44;
    public static final int REQUEST_BINDEMAIL                  = 45;
    public static final int REQUEST_EMAILFINDPWD               = 46;
    public static final int REQUEST_MINIGAMEORDERNO            = 47;
    public static final int REQUEST_SENDSMS_CODE               = 48;
    public static final int REQUEST_FAQ_JSON                   = 49;
    public static final int REQUEST_PAYMENT_RECHARGE_CARDS_CONFIG_JSON = 50;
    public static final int REQUEST_MY_QUESTION_JSON           = 51;
    public static final int REQUEST_MY_QUESTION_EVALUATE       = 52;
    public static final int REQUEST_MY_QUESTION_READ_STATE     = 53;
    public static final int REQUEST_MY_QUESTION_GET_DETAIL_RECORD  = 54;
    public static final int REQUEST_MY_QUESTION_ATTACH_IMAGE   = 55;
    public static final int REQUEST_MY_QUESTION_APPEND         = 56;



}
