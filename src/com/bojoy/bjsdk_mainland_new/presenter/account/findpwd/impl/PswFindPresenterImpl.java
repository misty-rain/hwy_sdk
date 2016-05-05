package com.bojoy.bjsdk_mainland_new.presenter.account.findpwd.impl;

import java.util.Map;

import android.content.Context;

import com.bojoy.bjsdk_mainland_new.congfig.ErrorCodeConstants;
import com.bojoy.bjsdk_mainland_new.eventhandler.event.BaseRequestEvent;
import com.bojoy.bjsdk_mainland_new.eventhandler.event.BaseResultCallbackListener;
import com.bojoy.bjsdk_mainland_new.model.IAccountModel;
import com.bojoy.bjsdk_mainland_new.model.entity.BackResultBean;
import com.bojoy.bjsdk_mainland_new.model.impl.AccountModelImpl;
import com.bojoy.bjsdk_mainland_new.presenter.account.findpwd.IPswFindPresenter;
import com.bojoy.bjsdk_mainland_new.support.eventbus.EventBus;
import com.bojoy.bjsdk_mainland_new.support.fastjson.JSON;
import com.bojoy.bjsdk_mainland_new.ui.view.account.findpwd.IPswFindView;
import com.bojoy.bjsdk_mainland_new.utils.LogProxy;
import com.bojoy.bjsdk_mainland_new.utils.StringUtility;

import okhttp3.Call;


/**
 * 密码找回 控制器实现
 * @author zhouhonghai @time 2016/1/12
 *
 */
public class PswFindPresenterImpl implements IPswFindPresenter, BaseResultCallbackListener {
	
	IPswFindView mIPswFindView;
	private IAccountModel mIAccountModel;
    EventBus eventBus = EventBus.getDefault();
    Context context;
    private final String TAG = PswFindPresenterImpl.class.getSimpleName();


    public PswFindPresenterImpl(Context context, IPswFindView iPswFindView) {
    	mIAccountModel = new AccountModelImpl();
        this.mIPswFindView = iPswFindView;
        this.context = context;
    }


    @Override
    public void onSuccess(Object response, int requestSessionEvent) {
    	BackResultBean backResultBean= JSON.parseObject((String)response,BackResultBean.class);
        switch (requestSessionEvent) {
            case BaseRequestEvent.REQUEST_FIND_PASSWORD_VERIFY_CODE://通过验证码找回密码事件回调
            	 if (backResultBean.getCode() == ErrorCodeConstants.ERROR_CODE_SUCCESS) {
            		 String mobileKey  = JSON.parseObject(backResultBean.getObj(), Map.class).get("mobileKey").toString();
            		 if(!StringUtility.isEmpty(mobileKey)){
            			 mIPswFindView.mobileFindSuccess(mobileKey);
            		 }
            		 
            	 }else{
            		 mIPswFindView.showError(backResultBean.getMsg());
            	 }
                break;
        }
    }

    @Override
    public void onError(Call call, Exception exception, int requestSessionEvent) {

    }


	@Override
	public void getVerifyCode(Context context, String account) {
		LogProxy.i(TAG, "getVerifyCode:accout:" + account);
		mIAccountModel.mobileFindPsw(context, account, this);
	}
}
