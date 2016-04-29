package com.bojoy.bjsdk_mainland_new.ui.view;

/**
 * Created by wutao on 2015/12/23.
 * 视图基接口，此种主要定义view 公共的方法
 */
public interface IBaseView {
    /**
     * 显示错误信息
     * @param message 信息
     */
    void showError(String message);

    /**
     * 操作成功需要的操作
     */
    void showSuccess();
}
