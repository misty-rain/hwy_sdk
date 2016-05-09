package com.bojoy.bjsdk_mainland_new.ui.view.login.impl;

import android.content.Context;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bojoy.bjsdk_mainland_new.app.tools.BJMGFSDKTools;
import com.bojoy.bjsdk_mainland_new.congfig.SysConstant;
import com.bojoy.bjsdk_mainland_new.eventhandler.event.BaseAdapterCalllback;
import com.bojoy.bjsdk_mainland_new.model.entity.PassPort;
import com.bojoy.bjsdk_mainland_new.presenter.account.IAccountPresenter;
import com.bojoy.bjsdk_mainland_new.presenter.account.impl.AccountPresenterImpl;
import com.bojoy.bjsdk_mainland_new.support.eventbus.EventBus;
import com.bojoy.bjsdk_mainland_new.ui.adapter.AccountLoginListAdapter;
import com.bojoy.bjsdk_mainland_new.ui.page.PageManager;
import com.bojoy.bjsdk_mainland_new.ui.page.base.BaseDialogPage;
import com.bojoy.bjsdk_mainland_new.ui.view.login.IAccountLoginListView;
import com.bojoy.bjsdk_mainland_new.utils.AccountUtil;
import com.bojoy.bjsdk_mainland_new.utils.ReflectResourceId;
import com.bojoy.bjsdk_mainland_new.utils.Resource;
import com.bojoy.bjsdk_mainland_new.utils.ToastUtil;
import com.bojoy.bjsdk_mainland_new.widget.dialog.BJMGFDialog;

import java.util.List;

import static com.bojoy.bjsdk_mainland_new.utils.Resource.layout.bjmgf_sdk_account_login_list_page;

/**
 * Created by wutao on 2015/12/29.
 * 账户列表视图 ，如果当前已经存储用户 ，初始化 跳至此view ，反之跳至AccountLoginview
 */
public class AccountLoginListView extends BaseDialogPage implements IAccountLoginListView {

    private final String TAG = AccountLoginListView.class.getSimpleName();

    private RelativeLayout mOtherUser;
    private LinearLayout mFirstAccount;
    private Button mAccountLoginButton;
    private ListView mAccountLv;
    private TextView tvFirstAccount;
    private RelativeLayout mLoginTryTextView;
    private EventBus eventBus = EventBus.getDefault();

    private AccountLoginListAdapter adapter = null;
    IAccountPresenter iAccountPresenter;


    public AccountLoginListView(Context context, PageManager manager,
                                BJMGFDialog dialog) {
        super(ReflectResourceId.getLayoutId(context,
                  bjmgf_sdk_account_login_list_page), context,
                  manager, dialog);
    }

    @Override
    public void onCreateView(View view) {
        mFirstAccount = (LinearLayout) getView(Resource.id.bjmgf_sdk_first_account_ll);
        mOtherUser = (RelativeLayout) getView(Resource.id.bjmgf_sdk_account_login_other_userId);
        mLoginTryTextView = (RelativeLayout) getView(Resource.id.bjmgf_sdk_account_login_tryTextViewId);
        mAccountLoginButton = (Button) getView(Resource.id.bjmgf_sdk_account_login_buttonId);
        mAccountLv = (ListView) getView(Resource.id.bjmgf_sdk_login_list_lv);
        tvFirstAccount = (TextView) getView(Resource.id.bjmgf_sdk_login_list_first_account);


        mOtherUser.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                BJMGFSDKTools.getInstance().isNeedSetAccount = true;
                BaseDialogPage loginPage = new AccountLoginView(context,
                          manager, dialog);
                manager.clearTopPage(loginPage);

            }
        });

        mAccountLoginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                showProgressDialog();
                iAccountPresenter.autoLogin(context,1);

            }
        });


        if (BJMGFSDKTools.getInstance().mPlatform == SysConstant.PLATFORM_GAME_ID) {
            mLoginTryTextView.setVisibility(View.GONE);
        } else {
            mLoginTryTextView.setVisibility(View.VISIBLE);
        }

        mLoginTryTextView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // 试玩登录
                showProgressDialog();
                iAccountPresenter.tryPlay(context);
            }
        });

        mFirstAccount.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mAccountLv.getVisibility() == View.GONE) {
                    openListView();
                } else {
                    closeListView();
                }
            }
        });

        BJMGFSDKTools.getInstance().isByRegister = false;

        super.onCreateView(view);
    }

    @Override
    public void setView() {

        iAccountPresenter = new AccountPresenterImpl(context, this);
        //加载本地账户列表
        iAccountPresenter.loadAccountList(context);
    }

    @Override
    public void onResume() {
        super.onResume();
        dialog.setCancelable(true);
    }

    @Override
    public boolean canBack() {
        return true;
    }

    @Override
    public void showError(String message) {
        dismissProgressDialog();
        ToastUtil.showMessage(context, message);

    }

    @Override
    public void showSuccess() {
        BJMGFSDKTools.getInstance().globalViewFlag = 0;
        dismissProgressDialog();
        openWelcomePage();
    }


    /**
     * 关闭下拉列表
     * 动画
     */
    private void closeListView() {
        Animation alphaAnimation = new AlphaAnimation(1.0f, 0.1f);
        alphaAnimation.setDuration(200);
        mAccountLv.startAnimation(alphaAnimation);
        mAccountLv.setVisibility(View.GONE);
    }

    /**
     * 打开下拉列表
     * 动画
     */
    private void openListView() {
        mAccountLv.setVisibility(View.VISIBLE);
        Animation alphaAnimation = new AlphaAnimation(0.1f, 1.0f);
        alphaAnimation.setDuration(200);
        mAccountLv.startAnimation(alphaAnimation);
    }

    /**
     * 自动登陆成功后的处理
     */
    @Override
    public void autoLoginSuccess() {
        dismissProgressDialog();
        openWelcomePage();
    }


    /**
     * 加载账户列表
     *
     * @param list            账户list
     * @param currentPassport 当前用户passport
     */
    @Override
    public void loadingAccountList(final List<PassPort> list, PassPort currentPassport) {

        BJMGFSDKTools.getInstance().setCurrentPassPort(currentPassport);
        tvFirstAccount.setText(currentPassport.getPp());
        // 增加回调，用于隐藏列表
        adapter = new AccountLoginListAdapter(list, context, new BaseAdapterCalllback() {

            /**
             * itme 点击
             * @param position  item 的位置 回转 给 view
             */
            @Override
            public void onItemClick(int position) {

                closeListView();
                BJMGFSDKTools.getInstance().setCurrentPassPort(list.get(position));
                tvFirstAccount.setText(list.get(position).getPp());
            }

            /**
             * itme 上 删除图标
             * @param position item 的位置
             */
            @Override
            public void onClickIcon(int position) {
                AccountUtil.remove(context, list.get(position).getUid());
                list.remove(position);
                if (list.size() > 0) {
                    adapter.notifyDataSetChanged();
                    BJMGFSDKTools.getInstance().setCurrentPassPort(list.get(0));
                    tvFirstAccount.setText(list.get(0).getPp());
                } else {
                    BJMGFSDKTools.getInstance().isNeedSetAccount = true;
                    BaseDialogPage loginPage = new AccountLoginView(context,
                              manager, dialog);
                    manager.clearTopPage(loginPage);
                }
            }
        });
        mAccountLv.setAdapter(adapter);
    }

}
