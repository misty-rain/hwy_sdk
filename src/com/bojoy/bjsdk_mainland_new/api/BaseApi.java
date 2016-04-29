package com.bojoy.bjsdk_mainland_new.api;

/**
 * Created by wutao on 2015/12/11.
 */
public class BaseApi {

    //系统级请求前缀
    public static final String BASE_INIT_AND_CHECK_ROOT = "/gf/app/sdk/";
    //账户类请求前缀
    public static final String BASE_ROOT = "/gf/pp/sdk/";
    //短信类请求前缀
    public static final String BASE_SMS_ROOT = "/gf/sms/sdk/";
    //用户中心请求前缀
    public static final String BASE_USERCENTER_ROOT = "/gf/m/uc/";
    //上传类业务请求前缀
    public static final String BASE_UPLOAD_ROOT = "/gf/m/upload/";
    //IM类业务请求前缀
    public static final String BASE_IM_ROOT = "/gf/im/manager/";
    //支付业务请求前缀
    public static final String BASE_PAY_ROOT = "/gf/pay/sdk/";
    //客服中心
    public static final String BASE_CS_ROOT = "/service/feedback/";


    //初始化SDK 请求
    public static final String INIT = BASE_INIT_AND_CHECK_ROOT + "init.do";
    //APP信息检测
    public static final String APPCHECK = BASE_INIT_AND_CHECK_ROOT + "appCheck.do";
    //平台登录
    public static final String APP_PLATFORM_LOGIN = BASE_ROOT + "login.do";
    //退出
    public static final String APP_PLATFORM_LOGOUT = BASE_ROOT + "logout.do";
    //平台注册
    public static final String APP_PLATFORM_REGISTER = BASE_ROOT + "register.do";
    //一键注册
    public static final String APP_ONEKEY_CHECK = BASE_ROOT + "oneKeyCheck.do";
    //发送手机验证码
    public static final String APP_SEND_PHONE_CODE = BASE_SMS_ROOT + "send.do";
    //输入手机验证码注册
    public static final String APP_MOBILE_REG_CHECK = BASE_ROOT + "mobileRegCheck.do";
    //自动登陆
    public static final String APP_AUTO_LOGIN = BASE_ROOT + "autoLogin.do";
    //手机找回密码
    public static final String APP_MOBILE_FIND_PSW = BASE_ROOT + "mobileFindPwd.do";
    //手机验证码重置密码
    public static final String APP_MOBILE_RESET_PSW = BASE_ROOT + "mobileResetPwd.do";
    //邮箱找回密码
    public static final String APP_EMAIL_FIND_PSW = BASE_ROOT + "emailfindpwd.do";
    //获得账户信息
    public static final String APP_GET_ACCOUNT_INFO = BASE_ROOT + "info.do";
    //获得用户会员信息
    public static final String APP_GET_USERVIP_INFO = BASE_ROOT + "vipinfo.do";
    //个人中心相关业务请求
    public static final String APP_USERCERTER_REQUEST = BASE_USERCENTER_ROOT + "3ovotcto.do";
    //上传用户头像
    public static final String APP_UPLOAD_USER_FACE = BASE_UPLOAD_ROOT + "face.do";
    //修改密码
    public static final String APP_MODIFY_PWD = BASE_ROOT + "changePwd.do";
    //绑定邮箱
    public static final String APP_BIND_EMAIL = BASE_ROOT + "emailbind.do";
    //获取绑定手机 时的短信验证码
    public static final String APP_GET_SMSCODE_BY_BIND_PHONE = BASE_ROOT + "mobileBindKey.do";
    //绑定手机号
    public static final String APP_BIND_PHONE = BASE_ROOT + "mobileBind.do";
    //web 签名
    public static final String APP_WEB_SIGN = BASE_ROOT + "identitytoweb.do?";
    //IM 类相关业务请求 包括未读消息、支付、领取
    public static final String APP_IM_REQUEST = BASE_IM_ROOT + "dispatcher.do";
    //试玩登录
    public static final String APP_TRY_LOGIN = BASE_ROOT + "trylogin.do";
    //修改试玩账号
    public static final String APP_TRY_CHANGE = BASE_ROOT + "trychange.do";
    //获取我方短信平台手机号
    public static final String APP_SEND_INFO = BASE_SMS_ROOT + "info.do";
    //提交订单
    public static final String APP_SUBMIT_ORDER = BASE_PAY_ROOT + "mobileorder.do";
    //U币查询
    public static final String APP_U_BLANCE = BASE_PAY_ROOT + "balance.do";
    //支付U币
    public static final String APP_PAY_ORDER = BASE_PAY_ROOT + "reduce.do";
    //提交问题
    public static final String APP_SUBMIT_QUES = BASE_CS_ROOT + "save.do";
    //获取 我的问题 数据
    public static final String APP_MY_QUESTION_LIST = BASE_CS_ROOT + "list.do?";
    //问题详情查询
    public static final String APP_MY_QUESTION_GET_DETAIL_RECORD = BASE_CS_ROOT + "record.do?";
    //发送 我的问题 评价
    public static final String APP_MY_QUESTION_EVALUATE = BASE_CS_ROOT + "evaluate.do?";
    public static final String APP_MY_QUESTION_READ_STATE = BASE_CS_ROOT + "read.do?";
    //获取离线消息
    public static final String APP_OFFLINE_MSG = BASE_IM_ROOT + "dispatcher.do";
   //网页充值
    public static final String APP_WAP_RECHARGE = BASE_PAY_ROOT + "init.do";

    //请求doman cdn 配置 信息
    public static final String APP_COLLOCATION_INFO = "http://123.59.67.156/version/HWY_cn_cn/client/sdk_android/1/clientConf.json?";
//    public static final String APP_COLLOCATION_INFO = "http://192.168.0.26:801/version/HWY_cn_cn/client/sdk_android/1/clientConf.json?";
    public static final String APP_CS_DOMAIN = "http://m-service.haowanyou.com";
    //问题详情追加回复
    public static final String APP_MY_QUESTION_APPEND = BASE_CS_ROOT + "append.do?";

}
