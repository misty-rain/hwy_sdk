package com.bojoy.bjsdk_mainland_new.congfig;


/**
 * Created by xiniu_wutao on 15/6/30. 系统常量类
 */
public class SysConstant {

    /**
     * 头像尺寸大小定义
     */
    public static final String AVATAR_APPEND_32 = "_32x32.jpg";
    public static final String AVATAR_APPEND_100 = "_100x100.jpg";
    public static final String AVATAR_APPEND_120 = "_100x100.jpg";// 头像120*120的pic

    /**
     * 系统配置
     */
    // 试玩账号标识符
    public static final String TRY_LOGIN_PASSPORT_POSTFIX = "@t";
    // 屏幕方向
    public static final int BJMGF_Screen_Orientation_Landscape = 0;
    // 屏幕方向
    public static final int BJMGF_Screen_Orientation_Portrait = 1;
    // 小游戏标识
    public static final int PLATFORM_GAME_ID = 3;
    // 读取磁盘上文件， 分支判断其类型
    public static final int FILE_SAVE_TYPE_IMAGE = 0X00013;
    public static final int FILE_SAVE_TYPE_AUDIO = 0X00014;


    /**
     * 性别代码
     *  1代表男 0代表 女
     */
    public static final String SEX_MALE = "1";
    public static final String SEX_FEMALE = "0";

    /**
     * json key
     */
    //支付卡充值json文件
    public static final String PAYMENT_RECHARGE_CARDS_CONFIG = "rechargecardsConfig.json";
    //常见问题页面json数据文件
    public static final String FAQ_JSON_FILE_NAME = "bjmgf_faq.json";
    //我的问题界面json数据文件
    public static final String MY_QUESTION_JSON_FILE_NAME = "bjmgf_my_question.json";
    // 好玩友CDN
    public static final String CDN_JSON_FILE_NAME = "BJGMSDK_CDN_JSON";

    /**
     * 账户中心接口请求header 类别代码
     */
    // 查询自己的个人信息
    public static final int USERCENTER_HEADER_REQUEST_CODE_GET_USERINFO_FOR_SELF = 24003;
    // 修改个人信息
    public static final int USERCENTER_HEADER_REQUEST_CODE_EDIT_USERINFO_FOR_SELF = 24002;
    // 好玩友平台注册
    public static final int USERCENTER_HEADER_REQUEST_CODE_HWYPLATFORM_REGISTER = 24001;
    // 未读消息
    public static final int USERCENTER_HEADER_REQUEST_CODE_UNREAD_MESSAGE = 21001;
    //根据uid 获取用户信息
    public static final int USERCENTER_HEADER_REQUEST_CODE_GET_OTHER_USERINFO = 24004;
    //轮询消息
    public static final int USERCENTER_HEADER_REQUEST_CODE_GET_OFFLINE_MSG = 21007;

    /**
     * 短信验证码 轮询超时任务
     */
    // 短信发送间隔时间
    public static final int SMS_POLLING_PERIOD_TIME = 1000;
    // 短信发送延迟时间
    public static final int SMS_POLLING_DELAY_TIME = 0;
    // 短信发送超时时间
    public static final int SMS_POLLING_MAX_TIME = 1000 * 60 * 1;
    // 短信生命周期
    public static final int SMS_LIFE_CYCLE = 60 * 1;

    /**
     * 支付类型
     */
    // 支付宝
    public static final String PAYMENT_ALIPAY_TYPE = "115";
    // 大额短信支付
    public static final String PAYMEBNT_SMSPAY_TYPE = "116";
    // 斯凯短信充值sdk
    public static final String PAYMENT_SK_SMSPAY_TYPE = "114";
    // 银联支付
    public static final String PAYMENT_UNIONPAY_TYPE = "117";
    // 移动卡支付
    public static final String PAYMENT_CM_CARD_PAY_TYPE = "111";
    // 联通卡支付
    public static final String PAYMENT_CU_CARD_PAY_TYPE = "112";
    // 电信卡支付
    public static final String PAYMENT_CT_CARD_PAY_TYPE = "113";
    // 微信支付
    public static final String PAYMENT_WXPAY_TYPE = "118";
    // 支付格式化OBJ
    public static final String PAYMENT_FORMATOBJTYPE = "object";
    // 消费类型参数
    public static final String PAYMENT_ORDERTYPE = "2";

    /**
     * 拉取个人消息的类型
     */
    //基本信息
    public static final int GET_USERINFO_TYPE_BASE = 1;
    //详细信息
    public static final int GET_USERINFO_TYPE_DETAIL = 2;
    //全部信息
    public static final int GET_USERINFO_TYPE_ALL = 3;

    /**
     * image and imageloader properties  and file
     */
    public static final int SIZE_MAX = 800;
    public static final int JPG_QUALITY = 80;
    //聊天界面，下载图片，文件名
    public static final String MY_QUESTION_DETAIL_ATTACH_FILE_NAME = "attach_image.jpg";

    /**
     * 悬浮窗相关配置
     */
    public static  final int DOCK_TIME_OPEN_WAIT_MAX = 10000;
    public static  final int DOCK_TIME_OPEN_HIDE_MAX = 200;
    public static final int SDK_NORMAL_TYPE = 0;
    public static final int SDK_DOCK_TYPE = 1;
    public static final int SDK_DOCK_SNS_TYPE = 2;
    public static final int SDK_DOCK_SNS_WISH_POLLMSG_TYPE = 50;

    public static final String MIUI_WARN_FLAG = "MIUI_WARN_FLAG";
    //用来判断是否为修改密码 后弹出的登陆对话框
    public static final String ISMODIFYPWDFLAGFORDIALOG = "ISMODIFYPWDFLAGFORDIALOG";
    //用来判断当起邮箱绑定状态 1= 已认证
    public static final String EMAIL_BIND_STATUS = "EMAIL_BIND_STATUS";
   //当前用户邮箱and 手机状态 本地缓存文件存储前缀
    public static final String CURRENT_USER_EMAILANDPHONE_INFO_HEADER = "CURRENT_USER_EMAILANDPHONE_INFO_HEADER";
   //用来标识当前用户已绑定邮箱但未验证
    public static final String CURRENT_USER_EMAIL_BIND_HEADER = "CURRENT_USER_EMAIL_BIND_HEADER";





}

