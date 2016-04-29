package com.bojoy.bjsdk_mainland_new.model.entity;

public class MessageNotifyData {

    /** 定义暂作保留  以后可能会用到*/
    /**
     * 消息类型
     */
    public static final String Msg_Type_Normal_Message = "0";
    public static final String Msg_Type_Hi_Message = "1";
    public static final String Msg_Type_System_Message = "2";

    /**
     * 招呼消息类型
     */
    public static final int Hi_Type_Flower_Message = 0;
    public static final int Hi_Type_Normal_Message = 1;
    public static final int Hi_Type_Wish_Message = 2;
    public static final int Hi_Type_New_Spot_Message = 3;
    public static final int Hi_Type_New_Friend_Message = 4;
    public static final int Hi_Type_Keep_Mistress_Message = 5;

    /**
     * 普通消息类型
     */
    public static final int Normal_Type_Text_Message = 1;
    public static final int Normal_Type_Img_Message = 2;
    public static final int Normal_Type_Voice_Message = 3;
    public static final int Normal_Type_Flower_Message = 4;
    public static final int Normal_Type_Name_Fight_Message = 5;
    public static final int Normal_Type_Card_Message = 6;
    public static final int Normal_Type_Game_Message = 7;
    public static final int Normal_Type_Wish_Message = 8;
    public static final int Normal_Type_Gift_Message = 9;

    /**
     * 招呼 “新关注” +　“新好友”　[] 显示
     */
    public static final int Hi_Type_New_Spot_content = 10;
    public static final int Hi_Type_New_Friend_content = 11;

    /**
     * “系统小秘” 标题显示
     */
    public static final int System_Secretary_Title = 12;
    /**
     * 消息uid
     */
    public long uid;

    /**
     * 鲜花数
     */
    public int num;

    /**
     * 招呼类型
     */
    public int type;

    /**
     * 消息类型
     */
    public String msgType;

    /**
     * 消息时间
     */
    public int time;

    /**
     * 最新消息标题
     */
    public String title;

    /**
     * 消息标题内容
     */
    public String mes;

    /**
     * 消息内容
     */
    public String content;


    /**
     * 空构造
     */
    public MessageNotifyData() {

    }

    /**
     * 构造方法
     */
    public MessageNotifyData(long uid, int num, int type, String msgType,
                             String title, int time, String mes, String content) {
        super();
        this.uid = uid;
        this.num = num;
        this.type = type;
        this.msgType = msgType;
        this.title = title;
        this.time = time;
        this.mes = mes;
        this.content = content;
    }


    @Override
    public String toString() {
        return String.format("uid = %d, num = %d, type = %d, msgType = %s" +
                        ", title = %s, time = %d, mes = %s, content = %s",
                uid, num, type, msgType, title, time, mes, content);
    }
}
