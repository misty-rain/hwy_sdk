package com.bojoy.bjsdk_mainland_new.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bojoy.bjsdk_mainland_new.eventhandler.event.BaseAdapterCalllback;
import com.bojoy.bjsdk_mainland_new.model.entity.PassPort;
import com.bojoy.bjsdk_mainland_new.utils.Resource;

import java.util.List;

import static com.bojoy.bjsdk_mainland_new.utils.Resource.layout.bjmgf_sdk_account_login_list_item;

/**
 * 选择列表用户adapter
 *
 * @author sunhaoyang
 */
public class AccountLoginListAdapter extends BasicAdapter<PassPort> {

    private BaseAdapterCalllback calllback;

    public AccountLoginListAdapter(List<PassPort> data, Context context, BaseAdapterCalllback calllback) {
        super(data, context, bjmgf_sdk_account_login_list_item);
        this.calllback = calllback;
    }

    @Override
    protected void setView(final int position, View convertView, ViewGroup parent) {
        TextView tvTitle = getView(Resource.id.bjmgf_sdk_login_list_item_account);
        LinearLayout llContent = getView(Resource.id.bjmgf_sdk_login_list_item_ll);
        RelativeLayout rlClose = getView(Resource.id.bjmgf_sdk_close_rl);
        ImageButton ibClose = getView(Resource.id.bjmgf_sdk_login_list_item_close);
        tvTitle.setText(mData.get(position).getPp());

        //item 行 点击事件
        llContent.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                calllback.onItemClick(position);
            }
        });

        //item 行上删除 用户
        rlClose.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                calllback.onClickIcon(position);
            }
        });

        ibClose.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                calllback.onClickIcon(position);
            }
        });
    }

}
