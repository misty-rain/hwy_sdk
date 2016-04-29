package com.bojoy.bjsdk_mainland_new.ui.view.login;

import com.bojoy.bjsdk_mainland_new.model.entity.PassPort;
import com.bojoy.bjsdk_mainland_new.ui.view.IBaseView;

import java.util.List;

/**
 * Created by wutao on 2015/12/29.
 * 账户列表登录 视图 接口
 * 视图接口，此处定义视图（activity）中的方法
 */
public interface IAccountLoginListView extends IBaseView {
    /**
     * 自动登陆成功后的处理
     */
    void autoLoginSuccess();

    /**
     * 加载本地账户列表
     * @param list 账户list
     * @param currentPassport  当前用户passport
     */
    void loadingAccountList(List<PassPort> list, PassPort currentPassport);


}
