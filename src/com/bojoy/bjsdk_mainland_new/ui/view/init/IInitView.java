package com.bojoy.bjsdk_mainland_new.ui.view.init;

import com.bojoy.bjsdk_mainland_new.model.entity.UpdateBean;
import com.bojoy.bjsdk_mainland_new.ui.view.IBaseView;

/**
 * Created by wutao on 2015/12/21.
 * 初始化 SDK 视图 接口
 * 视图接口，此处定义视图（activity）中的方法
 */
public interface IInitView extends IBaseView {

    //初始化SDK 登录
    void setAccountLoginView();

    //关闭应用
    void closeApp();

    //downloadAPK
    void downloadApk();

    /**
     * 打开更新视图
     * @param updateBean 更新实体
     */
    void openUpdateView(UpdateBean updateBean);



}
