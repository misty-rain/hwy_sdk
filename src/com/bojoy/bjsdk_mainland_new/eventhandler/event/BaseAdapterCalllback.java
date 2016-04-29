package com.bojoy.bjsdk_mainland_new.eventhandler.event;

/**
 * Created by wutao on 2015/12/29.
 *  adapter 中的回调，主要用于 adapter、view 之间的回调
 */
public interface BaseAdapterCalllback {
    /**
     *  Item 点击 事件
     *
     * @param position  item 的位置 回转 给 view
     */
    void onItemClick(int position);

    /**
     * Item 上ICON 图标的点击事件 ，目前只要用在 登陆用户list item 中删除用户icon 点击
     * @param position item 的位置
     */
    void onClickIcon(int position);
}
