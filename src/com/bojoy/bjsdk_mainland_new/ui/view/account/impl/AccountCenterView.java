package com.bojoy.bjsdk_mainland_new.ui.view.account.impl;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bojoy.bjsdk_mainland_new.app.BJMGFSdk;
import com.bojoy.bjsdk_mainland_new.app.tools.BJMGFSDKTools;
import com.bojoy.bjsdk_mainland_new.app.tools.DockTypeTools;
import com.bojoy.bjsdk_mainland_new.presenter.account.IAccountCenterPresenter;
import com.bojoy.bjsdk_mainland_new.presenter.account.impl.AccountCenterPresenterImpl;
import com.bojoy.bjsdk_mainland_new.support.fastjson.JSON;
import com.bojoy.bjsdk_mainland_new.support.fastjson.JSONObject;
import com.bojoy.bjsdk_mainland_new.ui.activity.base.BJMGFActivity;
import com.bojoy.bjsdk_mainland_new.ui.page.PageManager;
import com.bojoy.bjsdk_mainland_new.ui.page.base.BaseActivityPage;
import com.bojoy.bjsdk_mainland_new.ui.view.about.impl.AboutPage;
import com.bojoy.bjsdk_mainland_new.ui.view.account.IAccountCenterView;
import com.bojoy.bjsdk_mainland_new.ui.view.account.bindmail.BindEmailView;
import com.bojoy.bjsdk_mainland_new.ui.view.account.bindmail.DisplayEmailView;
import com.bojoy.bjsdk_mainland_new.ui.view.account.bindphone.impl.GetBindPhoneSmsCodeView;
import com.bojoy.bjsdk_mainland_new.ui.view.account.bindphone.impl.ModifyBindPhoneView;
import com.bojoy.bjsdk_mainland_new.ui.view.account.findpwd.impl.FindPwdSplashPage;
import com.bojoy.bjsdk_mainland_new.ui.view.login.impl.AccountLoginView;
import com.bojoy.bjsdk_mainland_new.utils.AccountSharePUtils;
import com.bojoy.bjsdk_mainland_new.utils.DomainUtility;
import com.bojoy.bjsdk_mainland_new.utils.ImageLoaderUtil;
import com.bojoy.bjsdk_mainland_new.utils.LogProxy;
import com.bojoy.bjsdk_mainland_new.utils.ReflectResourceId;
import com.bojoy.bjsdk_mainland_new.utils.Resource;
import com.bojoy.bjsdk_mainland_new.utils.SpUtil;
import com.bojoy.bjsdk_mainland_new.utils.StringUtility;
import com.bojoy.bjsdk_mainland_new.utils.ToastUtil;
import com.bojoy.bjsdk_mainland_new.widget.dialog.BJMGFDialog;

import java.util.HashMap;
import java.util.Map;

import static com.bojoy.bjsdk_mainland_new.utils.ReflectResourceId.getLayoutId;
import static com.bojoy.bjsdk_mainland_new.utils.Resource.id.bjmgf_sdk_float_account_info_id;
import static com.bojoy.bjsdk_mainland_new.utils.Resource.layout.bjmgf_sdk_dock_account_page;

/**
 * Created by wutao on 2016/1/11.
 * 账户中心 (管理) 视图
 */
public class AccountCenterView extends BaseActivityPage implements IAccountCenterView {

    private final String TAG = AccountCenterView.class.getSimpleName();
    IAccountCenterPresenter iAccountCenterPresenter;
    private ImageLoaderUtil imageLoaderUtil = ImageLoaderUtil.getInstance();
    private BaseActivityPage baseActivityPage;

    private RelativeLayout mModifyPwdLayout, mSwitchAccountLayout,
              mAccountInfo, mVipLayout, mBindPhoneLayout, mAboutLayout, mBindEmailLayout, mAuthentLayout;
    private ImageView faceImage, arrowAccount, arrowEmail, authentImage;
    private TextView mNickName, mUserId, vipLevelText,
              bindPhoneText, bindEmailText, authentText, enAuthText;
    private View authentLine;
    private String vipUrl;
    private boolean isBindPhone, isBindEmail;
    private String phoneNum = "";
    private int roundRectSize;

    /**
     * 初始化视图
     *
     * @param
     * @param context
     * @param manager
     * @param activity
     *******************/
    public AccountCenterView(Context context, PageManager manager, BJMGFActivity activity) {
        super(getLayoutId(context,
                  bjmgf_sdk_dock_account_page), context, manager,
                  activity);
        if (DockTypeTools.getInstance().isNormalDockType()) {
            //bjmgfData.setUserPersonalData(new UserData());
        }
        handler = new Handler(activity.getMainLooper());
        imageLoaderUtil.init(context);
    }


    @Override
    public void onCreateView(View view) {
        mModifyPwdLayout = getView(Resource.id.bjmgf_sdk_float_account_manager_modifyPwdLlId);
        mSwitchAccountLayout = getView(Resource.id.bjmgf_sdk_float_account_manager_changeAccountLlId);
        mVipLayout = getView(Resource.id.bjmgf_sdk_float_account_manager_vipItemId);
        mBindPhoneLayout = getView(Resource.id.bjmgf_sdk_float_account_manager_phoneId);
        mAboutLayout = getView(Resource.id.bjmgf_sdk_float_account_manager_aboutLlId);
        mBindEmailLayout = getView(Resource.id.bjmgf_sdk_float_account_manager_modifyEmailLlId);
        mAuthentLayout = getView(Resource.id.bjmgf_sdk_float_account_manager_authenticationId);
        faceImage = getView(Resource.id.bjmgf_sdk_float_account_manager_headiconImageId);
        mAccountInfo = getView(bjmgf_sdk_float_account_info_id);
        arrowEmail = getView(Resource.id.bjmgf_sdk_float_account_manager_email_contentId);
        mNickName = getView(Resource.id.bjmgf_sdk_account_nickname);
        mUserId = getView(Resource.id.bjmgf_sdk_account_id);
        bindPhoneText = getView(Resource.id.bjmgf_sdk_float_account_manager_bindPhoneContentId);
        arrowAccount = getView(Resource.id.bjmgf_sdk_float_account_manager_headicon_arrowImageId);
        vipLevelText = getView(Resource.id.bjmgf_sdk_float_account_manager_vip_contentId);
        bindEmailText = getView(Resource.id.bjmgf_sdk_float_account_manager_bindEmailTextViewId);
        authentText = getView(Resource.id.bjmgf_sdk_float_account_manager_authenticationTextViewId);
        enAuthText = getView(Resource.id.bjmgf_sdk_float_account_manager_enAuthenticationTextViewId);
        authentLine = getView(Resource.id.bjmgf_sdk_gap_line_authentication);
        authentImage = getView(Resource.id.bjmgf_sdk_float_account_manager_authentication_contentId);


        //设置实名认证选项状态

        if ((Integer.valueOf(DomainUtility.getInstance().getRealConfirm(context)) & 0x2) != 0) {
            if (BJMGFSDKTools.getInstance().getCurrentPassPort().getAuthType().equals("0")) {
                setAuthenticationStatus(2);
            } else {
                setAuthenticationStatus(1);
            }
        } else {
            setAuthenticationStatus(0);
        }

        //用户信息
        mAccountInfo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (DockTypeTools.getInstance().isNormalDockType()) {
                    arrowAccount.setVisibility(View.GONE);
                } else {
                    arrowAccount.setVisibility(View.VISIBLE);
                    baseActivityPage = new UserInfoView(context,
                              manager, activity);
                    manager.addViewForResult(AccountCenterView.this, baseActivityPage, 0);
                }
            }
        });

        //修改密码
        mModifyPwdLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                baseActivityPage = new ModifyPasswordView(context,
                          manager, activity);
                manager.addPage(baseActivityPage);
            }
        });

        //绑定邮箱
        mBindEmailLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (isBindEmail) {
                    baseActivityPage = new DisplayEmailView(context, manager, activity);
                    Map<String, Object> params = new HashMap<String, Object>();
                    params.put("email", bindEmailText.getText().toString());
                    baseActivityPage.putParams(params);
                    manager.addPage(baseActivityPage);
                } else {
                    baseActivityPage = new BindEmailView(context,
                              manager, activity);
                    manager.addPage(baseActivityPage);
                }
            }
        });

        //切换账号
        mSwitchAccountLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //activity.setNeedOpenDock(false);
                activity.setNeedOpenDock(true);
                quit();

          /*      Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {



                        if (AccountSharePUtils.getLocalAccountList(context).size() > 0) {
                            BJMGFDialog bjmgfDialog = new BJMGFDialog(context, (Activity) context, BJMGFDialog.Page_AccountLogin);
                            bjmgfDialog.show();

                        } else {
                            BJMGFDialog bjmgfDialog = new BJMGFDialog(context, (Activity) context, BJMGFDialog.Page_Login);
                            bjmgfDialog.show();
                        }
                    }
                }, 500);*/

                //
               // BJMGFSdk.getDefault().switchAccount(context);

            }
        });

        //会员
        mVipLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Uri levelUri = Uri.parse(vipUrl);
                Intent intent = new Intent(Intent.ACTION_VIEW, levelUri);
                activity.startActivity(intent);
            }
        });

        //绑定手机号
        mBindPhoneLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (!isBindPhone) {
                    //绑定
                    baseActivityPage = new GetBindPhoneSmsCodeView(context, manager, activity);
                    manager.addPage(baseActivityPage);
                } else {
                    //修改绑定
                    baseActivityPage = new ModifyBindPhoneView(context, manager, activity);
                    Map<String, Object> maps = new HashMap<String, Object>();
                    maps.put("phone", phoneNum);
                    baseActivityPage.putParams(maps);
                    manager.addPage(baseActivityPage);
                }
            }
        });

        //实名认证
        mAuthentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogProxy.d(TAG,BJMGFSDKTools.getInstance().getAuthenticaionUrl(context));
                Uri levelUri = Uri.parse(BJMGFSDKTools.getInstance().getAuthenticaionUrl(context));
                Intent intent = new Intent(Intent.ACTION_VIEW, levelUri);
                activity.startActivity(intent);
            }
        });

        //关于
        mAboutLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                baseActivityPage = new AboutPage(context, manager,
                          activity);
                manager.addPage(baseActivityPage);
            }
        });

        if (bindEmailText.getText().equals(SpUtil.getStringValue(context, "bindEmail", "")))
            arrowEmail.setVisibility(View.INVISIBLE);


        roundRectSize = (int) context.getResources().getDimension(
                  ReflectResourceId.getDimenId(context,
                            Resource.dimen.bjmgf_sdk_user_head_icon_size));
        //显示头像
        // setFaceImage();

       /* if (bjmgfData.isNormalDockType()) {
            arrowAccount.setVisibility(View.GONE);
        } else {
            arrowAccount.setVisibility(View.VISIBLE);
        }*/

        // 需要完善资料
        // if (userData.isNeedPerfectInfo() && bjmgfData.isDockSnsType()) {
        // titleText.setText(getString(Resource.string.bjmgf_sdk_dock_dialog_btn_fillInfo_str));
        // }

        super.onCreateView(view);
    }


    @Override
    public void setView() {
        hideRightBtn();
        setBackListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                quit();
            }
        });

        iAccountCenterPresenter = new AccountCenterPresenterImpl(context, this);
        showProgressDialog();
        showUserInfo();
        showAccountInfo();


    }


    @Override
    public void onViewResult(int sign) {
        String facePath = mBundle.getString("facePath");
        if (!StringUtility.isEmpty(facePath)) {
            imageLoaderUtil.loadImageUrl(context, faceImage, facePath, null, roundRectSize);

        }

    }


    /**
     * 显示账户信息
     */

    @Override
    public void showAccountInfo() {
        JSONObject jsonObject = JSON.parseObject(SpUtil.getStringValue(context, BJMGFSDKTools.getInstance().getCurrentPassPort().getUid(), ""));
        if (!jsonObject.get("bindMobile").equals("")) {
            phoneNum = jsonObject.get("bindMobile").toString();
            bindPhoneText.setText(jsonObject.get("bindMobile").toString());
            isBindPhone = true;
        }
        if (!jsonObject.get("bindEmail").equals("")) {
            bindEmailText.setText(jsonObject.get("bindEmail").toString());
            isBindEmail = true;
        }
        mNickName.setText(BJMGFSDKTools.getInstance().getCurrentPassPort().getPp());
        mUserId.setText(getString(Resource.string.bjmgf_sdk_hwynumber) + jsonObject.get("uid"));

    }

    /**
     * 切换账户
     */
    @Override
    public void switchAccount() {


    }

    /**
     * 显示用户会员信息
     *
     * @param map
     */
    @Override
    public void showUserVipInfo(Map<String, String> map) {
        String[] vipLevelNames = context.getResources().getStringArray(ReflectResourceId.getArrayId(context, Resource.array.bjmgf_sdk_user_vip_display_text_array));
        dismissProgressDialog();
        vipUrl = BJMGFSDKTools.getInstance().getIdentityUrl(context, 1);//1为链接拼接 VIP的redirect
        vipLevelText.setText(vipLevelNames[Integer.parseInt(map.get("vipLevel"))]);
    }

    /**
     * 显示用户信息包括头像、昵称、生日、性别
     */
    @Override
    public void showUserInfo() {
        iAccountCenterPresenter.getAccountVipInfo(context);
        if (BJMGFSDKTools.getInstance().getCurrUserData().getFaceUrl() != null) {
            imageLoaderUtil.loadImageUrl(context, faceImage, BJMGFSDKTools.getInstance().getCurrUserData().faceUrl, null, roundRectSize);
        }
    }

    @Override
    public void showError(String message) {
        dismissProgressDialog();
        ToastUtil.showMessage(context, message);

    }

    @Override
    public void showSuccess() {

    }


    /**
     * 设置实名认证选项栏状态
     *
     * @param statusCode 0:选项栏关闭	1:已认证	2:未认证
     */
    private void setAuthenticationStatus(int statusCode) {
        switch (statusCode) {
            case 0:
                mAuthentLayout.setVisibility(View.GONE);
                authentLine.setVisibility(View.GONE);
                mAuthentLayout.setEnabled(false);
                break;
            case 1:
                mAuthentLayout.setVisibility(View.VISIBLE);
                authentLine.setVisibility(View.VISIBLE);
                authentImage.setVisibility(View.GONE);
                mAuthentLayout.setEnabled(false);
                enAuthText.setVisibility(View.VISIBLE);
                authentText.setVisibility(View.GONE);
                break;
            case 2:
                mAuthentLayout.setVisibility(View.VISIBLE);
                authentLine.setVisibility(View.VISIBLE);
                authentImage.setVisibility(View.VISIBLE);
                mAuthentLayout.setEnabled(true);
                enAuthText.setVisibility(View.GONE);
                authentText.setVisibility(View.VISIBLE);
                break;

            default:
                break;
        }
    }


}
