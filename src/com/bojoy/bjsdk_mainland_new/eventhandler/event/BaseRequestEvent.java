package com.bojoy.bjsdk_mainland_new.eventhandler.event;

/**
 * Created by wutao on 2015/11/26.
 * 应用基本请求回话类型 ，用来处理在Presenter中 处理 不同的返回 结果
 */
public class BaseRequestEvent {
    public static final int Request_Init                       = 1;
    public static final int Request_App_Check_Update           = 2;
    public static final int Request_Register                   = 3;
    public static final int Request_One_Key_Info               = 4;
    public static final int Request_One_Key_Check              = 5;
    public static final int Request_Receive_Check_Code         = 6;
    public static final int Request_Mobile_Register_Check      = 7;
    public static final int Request_Login                      = 8;
    public static final int Request_Auto_Login                 = 9;
    public static final int Request_Find_Password_Verify_Code  = 10;
    public static final int Request_Reset_Password             = 11;
    public static final int Request_Logout                     = 12;
    public static final int Request_Mobile_Register            = 13;
    //public static final int Request_Password_Check_Code        = 14;
    public static final int Request_Password_Modify            = 15;
    public static final int Request_Get_Account_Info           = 16;
    public static final int Request_BindPhone                  = 17;
    public static final int Request_BindPhone_Check_Code       = 18;
    public static final int Request_UnBindPhone_Verify_Code    = 19;
    public static final int Request_UnBingPhone_Check_Code     = 20;
    public static final int Request_Send_Question              = 21;
    public static final int Request_Get_QuestionDetail         = 22;
    public static final int Request_Get_MyQuestion_List        = 23;
    public static final int Request_Question_Evaluate          = 24;
    public static final int Request_Mark_Question_Readed       = 25;
    public static final int Request_Append_Question            = 26;
    public static final int Request_Publish_Wish               = 27;
    public static final int Request_My_Wish_List               = 28;
    public static final int Request_PF_User_Info               = 29;
    public static final int Request_My_Attent_UID_List         = 30;
    public static final int Request_PF_Other_Users_Info        = 31;
    public static final int Request_PF_Modify_User_Info        = 32;
    public static final int Request_PF_Register                = 33;
    public static final int Request_PF_Upload_Face_Image       = 34;
    public static final int Request_Message_Notify             = 35;
    public static final int Request_Message_Offline            = 36;
    public static final int Request_First_Deploy               = 37;
    public static final int Request_Can_Publish_Wish           = 38;
    public static final int Request_User_Balance               = 39;
    public static final int Request_Pay_Order              	   = 40;
    public static final int Request_Recharge_Order             = 41;
    public static final int Request_VIP_Level		           = 42;



    public static final int Request_Try_Login		           = 43;
    public static final int Request_Try_change		           = 44;
    public static final int Request_BindEmail                  = 45;
    public static final int Request_EmailFindPwd               = 46;
    public static final int Request_MiniGameOrderNo            = 47;
    public static final int Request_SENDSMS_CODE               = 48;
    public static final int Request_FAQ_JSON                   = 49;
    public static final int Request_PAYMENT_RECHARGE_CARDS_CONFIG_JSON = 50;
    public static final int Request_MY_QUESTION_JSON           = 51;
    public static final int Request_MY_QUESTION_EVALUATE       = 52;
    public static final int Request_MY_QUESTION_READ_STATE     = 53;
    public static final int Request_MY_QUESTION_GET_DETAIL_RECORD  = 54;
    public static final int Request_MY_QUESTION_ATTACH_IMAGE   = 55;
    public static final int Request_MY_QUESTION_APPEND         = 56;



}
