package com.bojoy.bjsdk_mainland_new.presenter.account.findpwd.impl;

import java.util.Map;

import android.content.Context;

import com.bojoy.bjsdk_mainland_new.congfig.ErrorCodeConstants;
import com.bojoy.bjsdk_mainland_new.eventhandler.event.BaseRequestEvent;
import com.bojoy.bjsdk_mainland_new.eventhandler.event.BaseResultCallbackListener;
import com.bojoy.bjsdk_mainland_new.model.IAccountModel;
import com.bojoy.bjsdk_mainland_new.model.entity.BackResultBean;
import com.bojoy.bjsdk_mainland_new.model.impl.AccountModelImpl;
import com.bojoy.bjsdk_mainland_new.presenter.account.findpwd.IPswFindByEmailPresenter;
import com.bojoy.bjsdk_mainland_new.support.eventbus.EventBus;
import com.bojoy.bjsdk_mainland_new.support.fastjson.JSON;
import com.bojoy.bjsdk_mainland_new.ui.view.account.findpwd.IPswFindByEmailView;
import com.bojoy.bjsdk_mainland_new.utils.LogProxy;
import com.bojoy.bjsdk_mainland_new.utils.StringUtility;

import okhttp3.Call;


/**
 * 密码找回 控制器实现
 * @author zhouhonghai @time 2016/1/12
 *
 */
public class PswFindByEmailPresenterImpl implements IPswFindByEmailPresenter, BaseResultCallbackListener {
	
	IPswFindByEmailView mIPswFindByEmailView;
	private IAccountModel mIAccountModel;
    EventBus eventBus = EventBus.getDefault();
    Context context;
    private final String TAG = PswFindByEmailPresenterImpl.class.getSimpleName();


    public PswFindByEmailPresenterImpl(Context context, IPswFindByEmailView iPswFindByEmailView) {
    	mIAccountModel = new AccountModelImpl();
        this.mIPswFindByEmailView = iPswFindByEmailView;
        this.context = context;
    }


    @Override
    public void onSuccess(Object response, int requestSessionEvent) {
    	BackResultBean backResultBean= JSON.parseObject((String)response,BackResultBean.class);
        switch (requestSessionEvent) {
            case BaseRequestEvent.REQUEST_EMAILFINDPWD://通过验证码找回密码事件回调
            	 if (backResultBean.getCode() == ErrorCodeConstants.ERROR_CODE_SUCCESS) {
            		 String email  = JSON.parseObject(backResultBean.getObj(), Map.class).get("email").toString();
            		 if(!StringUtility.isEmpty(email)){
            			 mIPswFindByEmailView.emailFindSuccess(email);
            		 }
            		 
            	 }else{
            		 mIPswFindByEmailView.showError(backResultBean.getMsg());
            	 }
                break;
        }
    }

    @Override
    public void onError(Call call, Exception exception, int requestSessionEvent) {

    }


	@Override
	public void getEmailVerifyCode(Context context, String account, String email) {
		LogProxy.i(TAG, "getVerifyCode:accout:" + account);
		mIAccountModel.emailFindPsw(context, account, email, this);
	}
}
