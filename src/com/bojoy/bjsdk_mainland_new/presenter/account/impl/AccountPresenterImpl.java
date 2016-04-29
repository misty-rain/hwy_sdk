package com.bojoy.bjsdk_mainland_new.presenter.account.impl;


import android.content.Context;

import com.bojoy.bjsdk_mainland_new.app.tools.BJMGFSDKTools;
import com.bojoy.bjsdk_mainland_new.congfig.ErrorCodeConstants;
import com.bojoy.bjsdk_mainland_new.congfig.SysConstant;
import com.bojoy.bjsdk_mainland_new.eventhandler.event.BJMGFSdkEvent;
import com.bojoy.bjsdk_mainland_new.eventhandler.event.BaseRequestEvent;
import com.bojoy.bjsdk_mainland_new.eventhandler.event.BaseResultCallbackListener;
import com.bojoy.bjsdk_mainland_new.model.IAccountModel;
import com.bojoy.bjsdk_mainland_new.model.entity.BackResultBean;
import com.bojoy.bjsdk_mainland_new.model.entity.PassPort;
import com.bojoy.bjsdk_mainland_new.model.impl.AccountModelImpl;
import com.bojoy.bjsdk_mainland_new.presenter.account.IAccountPresenter;
import com.bojoy.bjsdk_mainland_new.support.eventbus.EventBus;
import com.bojoy.bjsdk_mainland_new.support.fastjson.JSON;
import com.bojoy.bjsdk_mainland_new.ui.view.IBaseView;
import com.bojoy.bjsdk_mainland_new.ui.view.login.IAccountLoginListView;
import com.bojoy.bjsdk_mainland_new.utils.AccountSharePUtils;
import com.bojoy.bjsdk_mainland_new.utils.AccountUtil;
import com.bojoy.bjsdk_mainland_new.utils.CompartorUtil;
import com.bojoy.bjsdk_mainland_new.utils.SpUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by wutao on 2015/12/24.
 * 账户登陆， 控制器 实现
 */
public class AccountPresenterImpl implements IAccountPresenter, BaseResultCallbackListener {
    private IAccountModel iAccountModel;
    private IBaseView iBaseView;
    EventBus eventBus = EventBus.getDefault();
    Context context;
    Map<String, String> accountMaps;
    private List<String> list = new ArrayList<String>();
    private final String TAG = AccountPresenterImpl.class.getSimpleName();

    public AccountPresenterImpl(Context context, IBaseView iBaseView) {
        iAccountModel = new AccountModelImpl();
        this.iBaseView = iBaseView;
        this.context = context;
    }


    @Override
    public void onSuccess(Object response, int requestSessionEvent) {
        try {
            BackResultBean backResultBean = JSON.parseObject((String) response, BackResultBean.class);
            if (backResultBean.getCode() == ErrorCodeConstants.ERROR_CODE_SUCCESS) {
                PassPort passPort = null;
                switch (requestSessionEvent) {
                    case BaseRequestEvent.Request_Login://登陆回话事件
                        passPort = JSON.parseObject(backResultBean.getObj(), PassPort.class);
                        BJMGFSDKTools.getInstance().setCurrentPassPort(passPort);//保存当前用户passport
                        BJMGFSDKTools.getInstance().setCurrUserStatusOnLine(true);
                        AccountUtil.remove(context, passPort.getUid());
                        AccountUtil.saveAccount(context, passPort.getUid(), backResultBean.getObj().toString());
                        iAccountModel.getUserInfoForSelf(context, SysConstant.GET_USERINFO_TYPE_BASE, this);//查询用户信息
                        iAccountModel.getAccountInfo(context, this); //查询账户信息
                        eventBus.post(BJMGFSdkEvent.App_Login_Success);
                         iBaseView.showSuccess();
                        break;
                    case BaseRequestEvent.Request_Try_Login: //试玩事件
                        passPort = JSON.parseObject(backResultBean.getObj(), PassPort.class);
                        BJMGFSDKTools.getInstance().setCurrentPassPort(passPort);//保存当前用户passport
                        BJMGFSDKTools.getInstance().setCurrUserStatusOnLine(true);
                        iBaseView.showSuccess();
                        break;
                    case BaseRequestEvent.Request_Try_change:   //修改试玩账号
                        passPort = JSON.parseObject(backResultBean.getObj(), PassPort.class);
                        AccountUtil.remove(context, passPort.getUid());
                        AccountUtil.saveAccount(context, passPort.getUid(), backResultBean.getObj().toString());
                        BJMGFSDKTools.getInstance().setCurrUserStatusOnLine(false);
                        EventBus.getDefault().post(new BJMGFSdkEvent(BJMGFSdkEvent.App_Logout));
                        iBaseView.showSuccess();
                        break;
                    case BaseRequestEvent.Request_PF_User_Info: //获得自己的个人信息
                        BJMGFSDKTools.getInstance().saveCurrentUserInfo(context, backResultBean, (String) response);
                        break;
                    case BaseRequestEvent.Request_Get_Account_Info://获取账号信息事件
                        SpUtil.setStringValue(context, BJMGFSDKTools.getInstance().currentPassPort.getUid(), backResultBean.getObj());
                        break;
                    case BaseRequestEvent.Request_Auto_Login://自动登陆
                        passPort = JSON.parseObject(backResultBean.getObj(), PassPort.class);
                        BJMGFSDKTools.getInstance().setCurrentPassPort(passPort);
                        BJMGFSDKTools.getInstance().setCurrUserStatusOnLine(true);
                        AccountUtil.remove(context, passPort.getUid());
                        AccountUtil.saveAccount(context, passPort.getUid(), backResultBean.getObj().toString());
                        iAccountModel.getUserInfoForSelf(context, SysConstant.GET_USERINFO_TYPE_BASE, this);
                        iAccountModel.getAccountInfo(context, this); //查询账户信息
                        ((IAccountLoginListView) iBaseView).autoLoginSuccess();
                        break;
                    case BaseRequestEvent.Request_Register://平台注册
                        passPort = JSON.parseObject(backResultBean.getObj(), PassPort.class);
                        BJMGFSDKTools.getInstance().setCurrentPassPort(passPort);
                        AccountUtil.remove(context, passPort.getUid());
                        AccountUtil.saveAccount(context, passPort.getUid(), backResultBean.getObj().toString());
                        iAccountModel.getUserInfoForSelf(context, SysConstant.GET_USERINFO_TYPE_BASE, this);//查询用户信息
                        iAccountModel.getAccountInfo(context, this); //查询账户信息
                        iBaseView.showSuccess();
                        break;
                    case BaseRequestEvent.Request_One_Key_Info://发送短信
                        //((ISmsView)iBaseView).showGetInfoSuccess();
                        iBaseView.showSuccess();
                        break;
                    case BaseRequestEvent.Request_One_Key_Check: //一键注册
                        BJMGFSDKTools.getInstance().setCurrentPassPort(passPort); //保存当期用户passport
                        AccountUtil.remove(context, passPort.getUid());
                        AccountUtil.saveAccount(context, passPort.getUid(), backResultBean.getObj().toString());
                        iAccountModel.getUserInfoForSelf(context, SysConstant.GET_USERINFO_TYPE_BASE, this);//查询用户信息
                        iAccountModel.getAccountInfo(context, this); //查询账户信息
                        iBaseView.showSuccess();
                        break;
                }
            } else {
                iBaseView.showError(backResultBean.getMsg());

            }

        } catch (Exception e) {
//            LogProxy.e(TAG, e.getMessage());
            e.printStackTrace();
        }

    }

    @Override
    public void onError(Call call, Exception exception, int requestSessionEvent) {


    }

    /**
     * 平台注册
     *
0  44* @param context  上下文
     * @param userName 用户名
     * @param passWord 密码
     * @param email    邮箱
     */
    @Override
    public void platformRegister(Context context, String userName, String passWord, String email) {
        iAccountModel.platformRegister(context, userName, passWord, email, this);

    }

    /**
     * 登陆操作
     *
     * @param context  上下文
     * @param userName 用户名
     * @param passWord 密码
     */
    @Override
    public void login(Context context, String userName, String passWord) {
        iAccountModel.login(context, userName, passWord, this);
    }

    /**
     * 试玩
     *
     * @param context 上下文
     */
    @Override
    public void tryPlay(Context context) {
        iAccountModel.tryLogin(context, BJMGFSDKTools.getInstance().getTryKey(context), this);
    }

    /**
     * 试玩转正
     *
     * @param context
     * @param newPP
     * @param passWord
     */
    @Override
    public void tryChange(Context context, String newPP, String passWord) {
        iAccountModel.tryChange(context, newPP, passWord, this);
    }

    /**
     * 自动登陆
     *
     * @param context 上下文
     */
    @Override
    public void autoLogin(Context context) {
        iAccountModel.autoLogin(context, this);
    }

    /**
     * 一键注册
     *
     * @param context    上下文
     * @param verifyCode 校验码
     */
    @Override
    public void oneKeyRegister(Context context, String verifyCode) {
        iAccountModel.oneKeyRegister(context, verifyCode, this);


    }

    /**
     * 获取我方短信平台手机号
     *
     * @param context
     */
    @Override
    public void sendInfo(Context context) {
        iAccountModel.sendInfo(context, this);
    }

    /**
     * 加载本地账户列表
     *
     * @param context 上下文
     */
    @Override
    public void loadAccountList(Context context) {
        accountMaps = (Map<String, String>) AccountSharePUtils.getAll(context);
        // 循环将本地账号填入集合中
        for (Map.Entry<String, String> entry : accountMaps
                  .entrySet()) {
            list.add(entry.getValue());

        }
        CompartorUtil compartorUtil = CompartorUtil.getInstance();
        Collections.sort(list, compartorUtil);
        //Collections sort 方法默认排序为升序 ，此处利用reverse方法将所有元素重新反转，变为降序
        Collections.reverse(list);
        List<PassPort> passPortList = new ArrayList<PassPort>();
        for (int i = 0; i < list.size(); i++) {
            String jsonStr = list.get(i).substring(0, list.get(i).indexOf("}") + 1);
            passPortList.add((JSON.parseObject(jsonStr, PassPort.class)));
        }
        ((IAccountLoginListView) iBaseView).loadingAccountList(passPortList, passPortList.get(0));

    }


}
