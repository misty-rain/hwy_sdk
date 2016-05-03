package com.bojoy.bjsdk_mainland_new.app.tools;

import android.content.Context;
import android.os.Build;

import com.bojoy.bjsdk_mainland_new.congfig.SysConstant;
import com.bojoy.bjsdk_mainland_new.model.entity.BaseAppPassport;
import com.bojoy.bjsdk_mainland_new.model.entity.PayOrderData;
import com.bojoy.bjsdk_mainland_new.model.entity.QuestionData;
import com.bojoy.bjsdk_mainland_new.model.entity.RechargeData;
import com.bojoy.bjsdk_mainland_new.model.entity.RechargeOrderDetail;
import com.bojoy.bjsdk_mainland_new.utils.AccountSharePUtils;
import com.bojoy.bjsdk_mainland_new.utils.SpUtil;
import com.bojoy.bjsdk_mainland_new.utils.StringUtility;
import com.bojoy.bjsdk_mainland_new.utils.Utility;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wutao on 2016/1/28.
 * 请求参数组装 工具类  ，此类中组装各种不同业务模块的 参数集合
 */
public class ParamsTools {
    private static PayTools payTools = PayTools.getInstance();
    private static ParamsTools paramsTools;


    public static ParamsTools getInstance() {
        if (paramsTools == null) {
            synchronized (ParamsTools.class) {
                if (paramsTools == null) {
                    paramsTools = new ParamsTools();
                }
            }
        }
        return paramsTools;
    }

    /**
     * 获得基础参数集合 ，主要包括实例化appid、platformid、token、uuid
     *
     * @return
     */
    private Map<String, String> getFoundationParamsMap(Context context) {
        Map<String, String> params = new HashMap<String, String>();
        final String time = String.valueOf(System.currentTimeMillis());
        String uuid = SpUtil.getStringValue(context, "uuid", "");
        if (uuid != "") {
            params.put("uuid", uuid);
        }
        if (BJMGFSDKTools.getInstance().getCurrentPassPort() != null)
            params.put("token", BJMGFSDKTools.getInstance().getCurrentPassPort().getToken());
        params.put("appid", BaseAppPassport.appId);
        params.put("platformid", BaseAppPassport.platformId);
        return params;
    }


    /**
     * 获得基本参数集合 ，主要用来组装基本参数的集合，避免重复在每个方法内重复组装
     *
     * @param context             上下文
     * @param isContainDeviceInfo 此变量 主要用来判断 是否组合设备信息的参数，主要包括mac、device、model 参数
     * @return
     */
    public Map<String, String> getBaseParamsMap(Context context, boolean isContainDeviceInfo) {
        Map<String, String> params = getFoundationParamsMap(context);
        if (isContainDeviceInfo) {
            params.put("mac", BJMGFSDKTools.getInstance().getBJMGFMac(context));
            params.put("device", Utility.getDeviceIMEI(context));
            params.put("model", Build.MODEL);
        }
        return params;
    }

    /**
     * 用户中心 业务单元的基本参数
     *
     * @param context
     * @return
     */
    public Map<String, String> getUserCenterParamsMap(Context context) {
        Map<String, String> params = getFoundationParamsMap(context);
        final String time = String.valueOf(System.currentTimeMillis());
        params.put("stepMd5", "1");
        params.put("stepWeb", "1");
        params.put("sign", StringUtility.md5(params.get("uuid") + BaseAppPassport.appId + time + BaseAppPassport.appKey + BaseAppPassport.APP_SECRET));
        return params;
    }

    /**
     * 初始化SDK 业务单元的基本参数 ，主要包括初始化SDK 、sdk检测
     *
     * @param context
     * @return
     */
    public Map<String, String> getInitSDKParamsMap(Context context) {
        Map<String, String> params = getFoundationParamsMap(context);
        final String time = String.valueOf(System.currentTimeMillis());
        params.put("version", BaseAppPassport.version);
        String md5Str;
        if (StringUtility.isEmpty(params.get("uuid")))
            md5Str = BaseAppPassport.appId + time + BaseAppPassport.appKey + BaseAppPassport.APP_SECRET;
        else
            md5Str = params.get("uuid") + BaseAppPassport.appId + time + BaseAppPassport.appKey + BaseAppPassport.APP_SECRET;
        params.put("sign", StringUtility.md5(md5Str));
        return params;
    }

    /**
     * 余额充足时支付所需要的参数
     * @param context  上下文
     * @param payOrderData 充值数据类
     * @return
     */
    public Map<String,String> getPayOrderParams(Context context, PayOrderData payOrderData){
    Map<String, String> params = getFoundationParamsMap(context);
    final String time = String.valueOf(System.currentTimeMillis());
    params.put("sign", StringUtility.md5(params.get("uuid") + BaseAppPassport.appId + time + BaseAppPassport.appKey + BaseAppPassport.APP_SECRET));
    params.put("apporderno", payOrderData.getAppOrderNumber() + "");
    params.put("amount", payOrderData.getAmount() + "");
    params.put("ext", payOrderData.getExt());
    params.put("sendurl", payOrderData.getSendUrl());
    params.put("goodsname", payOrderData.getProductName());
    params.put("cash", String.valueOf(payOrderData.getCash()));
   params.put("ordertype", payOrderData.getOrderType());
   params.put("userid", "");
   params.put("extend", payOrderData.getExtend());
    params.put("ordertype", SysConstant.PAYMENT_ORDERTYPE);
    params.put("formatObjType", SysConstant.PAYMENT_FORMATOBJTYPE);
    params.put("channel", "");
    params.put("appsid", "");
    params.put("vtime", time);
    params.put("version", BaseAppPassport.version);
    params.put("serverid",payOrderData.getAppServerID()+"");
    return params;
}

    /**
     * 余额不足充值时 支付业务 业务单元的基本参数 ，主要包括支付、提交订单
     *
     * @param context
     * @return
     */
    public Map<String, String> getPaymentParamsMap(Context context, RechargeOrderDetail rechargeOrderDetail) {
        Map<String, String> params = getFoundationParamsMap(context);
        final String time = String.valueOf(System.currentTimeMillis());
        params.put("sign", StringUtility.md5(params.get("uuid") + BaseAppPassport.appId + time + BaseAppPassport.appKey + BaseAppPassport.APP_SECRET));
        params.put("apporderno", rechargeOrderDetail.getAppOrderNumber() + "");
        params.put("money", rechargeOrderDetail.getMoney() + "");
        params.put("amount", rechargeOrderDetail.getAmount() + "");
        params.put("payid", rechargeOrderDetail.getPayID());
        params.put("ext", rechargeOrderDetail.getExt());
        params.put("sendurl", rechargeOrderDetail.getSendUrl());
        params.put("goodsname", rechargeOrderDetail.getProductName());
        params.put("cash", String.valueOf(rechargeOrderDetail.getCash()));
        params.put("ordertype", rechargeOrderDetail.getOrderType());
        params.put("userid", "");
        params.put("extend", rechargeOrderDetail.getExtend());
        params.put("ordertype", SysConstant.PAYMENT_ORDERTYPE);
        params.put("formatObjType", SysConstant.PAYMENT_FORMATOBJTYPE);
        params.put("channel", "");
        params.put("appsid", "088");
        params.put("vtime", time);
        params.put("extend2", "18661576287");
        params.put("version", BaseAppPassport.version);
        params.put("payuserid", BJMGFSDKTools.getInstance().getCurrentPassPort().getUid());
        return params;
    }

    /**
     * 提交问题业务单元的基本参数 ，主要应用在提交问题场景
     *
     * @param context
     * @return
     */
    public Map<String, String> getQuestionParamsMap(Context context, QuestionData questionData) {
        Map<String, String> params = getBaseParamsMap(context, true);
        params.put("gameid", questionData.gameId);
        params.put("serverid", questionData.serverID);
        params.put("sort", questionData.sort);
        params.put("type", String.valueOf(questionData.type));
        params.put("subtype", String.valueOf(questionData.subType));
        params.put("title", questionData.title);
        params.put("content", questionData.content);
        // params.put("occur", "");
        params.put("passport", BJMGFSDKTools.getInstance().getCurrentPassPort().getPp());
        params.put("roleid", questionData.roleID);
        params.put("rolename", questionData.roleName);
        params.put("clienttype", "2");
/*        params.put("phone", "15599002125");
        params.put("qq", );
        params.put("email", "");
        params.put("clienttype", "");
        params.put("os", "");
        params.put("ext", rechargeOrderDetail.getExtend());*/
        params.put("uid", BJMGFSDKTools.getInstance().getCurrentPassPort().getUid());
        return params;
    }

    /**
     * 网页充值的基本参数
     *
     * @param context 上下文
     * @return
     */
    public Map<String, String> getWapRecharge(Context context) {
        RechargeData rechargeData = payTools.getRechargeData();
        Map<String, String> params = getFoundationParamsMap(context);
        final String time = String.valueOf(System.currentTimeMillis());
        params.put("sign", StringUtility.md5(params.get("uuid") + BaseAppPassport.appId + time + BaseAppPassport.appKey + BaseAppPassport.APP_SECRET));
        params.put("apporderno", rechargeData.getOrderSerial());
        params.put("money", rechargeData.getMoney() + "");
        params.put("amount", String.valueOf((int) (rechargeData.getMoney() * payTools.PAY_RATIO)));
        params.put("vtime", time);
        params.put("payuserid", BJMGFSDKTools.getInstance().getCurrentPassPort().getUid());
        params.put("userid", "");
        params.put("ext", rechargeData.getServerId() + "");
        params.put("sendurl", "");
        params.put("goodsname", rechargeData.getProductName());
        params.put("channel", "");
        return params;

    }

    /**
     * 问题详情追加回复
     *
     * @param context
     * @param fid     问题记录ID(不能为空)
     * @param content 追加回复的内容(不能为空。最多100个汉字)
     * @return
     */
    public Map<String, String> getAppendQuestionParamsMap(Context context, String fid, String content) {
        Map<String, String> params = getBaseParamsMap(context, true);
        params.put("fid", fid);
        params.put("content", content);
        return params;
    }
}
