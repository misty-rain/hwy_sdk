package com.bojoy.bjsdk_mainland_new.ui.view.message;

import com.bojoy.bjsdk_mainland_new.model.entity.MessageNotifyData;
import com.bojoy.bjsdk_mainland_new.ui.view.IBaseView;

import java.util.ArrayList;

/**
 * Created by wutao on 2016/1/22.
 */
public interface IMessageView extends IBaseView {
    /**
     * 显示没有结果视图
     */
    void showNoResultView();

    /**
     * 显示消息列表
     * @param messageNotifyDataArrayList 回传的消息list
     */
    void showMsgListView(ArrayList<MessageNotifyData> messageNotifyDataArrayList);
}
