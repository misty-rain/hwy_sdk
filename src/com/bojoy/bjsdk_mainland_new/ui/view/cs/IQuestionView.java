package com.bojoy.bjsdk_mainland_new.ui.view.cs;

import com.bojoy.bjsdk_mainland_new.ui.view.IBaseView;

import java.util.Map;

/**
 * Created by sunhaoyang on 2016/1/29.
 */
public interface IQuestionView extends IBaseView {

    /**
     * 显示用户会员信息
     * @param map
     */
    void showUserVipInfo(Map<String,String> map);
}
