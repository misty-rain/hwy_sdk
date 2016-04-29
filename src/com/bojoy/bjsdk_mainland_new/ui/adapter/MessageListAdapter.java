package com.bojoy.bjsdk_mainland_new.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bojoy.bjsdk_mainland_new.app.tools.MessageNoticeTools;
import com.bojoy.bjsdk_mainland_new.congfig.SysConstant;
import com.bojoy.bjsdk_mainland_new.model.entity.MessageNotifyData;
import com.bojoy.bjsdk_mainland_new.utils.DateUtil;
import com.bojoy.bjsdk_mainland_new.utils.ImageLoaderUtil;
import com.bojoy.bjsdk_mainland_new.utils.LogProxy;
import com.bojoy.bjsdk_mainland_new.utils.ReflectResourceId;
import com.bojoy.bjsdk_mainland_new.utils.Resource;
import com.bojoy.bjsdk_mainland_new.utils.StringUtility;
import com.bojoy.bjsdk_mainland_new.utils.Utility;

import java.util.ArrayList;

/**
 * Created by wutao on 2016/1/27.
 * 消息列表适配器
 */
public class MessageListAdapter extends BaseAdapter {
    private final String TAG = MessageListAdapter.class.getSimpleName();
    private Context context;
    private MessageItemListener listener;
    private ArrayList<MessageNotifyData> messageList;
    private ImageLoaderUtil imageLoaderUtil = ImageLoaderUtil.getInstance();
    private String[] HI_Strings = null;
    private String[] Normal_Strings = null;

    public MessageListAdapter(Context context,
                              MessageItemListener listener,
                              ArrayList<MessageNotifyData> messageList) {
        this.context = context;
        this.listener = listener;
        this.messageList = messageList;

        HI_Strings = context.getResources().getStringArray(
                ReflectResourceId.getArrayId(context,
                        Resource.array.bjmgf_sdk_dock_message_notify_hi_array));
        Normal_Strings = context
                .getResources()
                .getStringArray(
                        ReflectResourceId
                                .getArrayId(
                                        context,
                                        Resource.array.bjmgf_sdk_dock_message_notify_Normal_array));
        imageLoaderUtil.init(context);
    }

    @Override
    public int getCount() {
        return messageList.size();
    }

    @Override
    public Object getItem(int arg0) {
        LogProxy.i(TAG, "getItem " + messageList.get(arg0));
        return messageList.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View_Holder holder;
        final MessageNotifyData messageData = messageList.get(position);
        // 如果缓存convertView为空，则需要创建View
        if (convertView == null) {
            // 自定义的一个类用来缓存convertview
            holder = new View_Holder();
            // 根据自定义的Item布局加载布局
            convertView = (LinearLayout) View.inflate(context,
                    ReflectResourceId.getLayoutId(context,
                            Resource.layout.bjmgf_sdk_dock_message_item), null);
            holder.bjmgf_sdk_message_headImg = (ImageView) convertView
                    .findViewById(ReflectResourceId.getViewId(context,
                            Resource.id.bjmgf_sdk_message_headImg));
            holder.bjmgf_sdk_message_titleTv = (TextView) convertView
                    .findViewById(ReflectResourceId.getViewId(context,
                            Resource.id.bjmgf_sdk_message_titleTv));
            holder.bjmgf_sdk_message_contentTv = (TextView) convertView
                    .findViewById(ReflectResourceId.getViewId(context,
                            Resource.id.bjmgf_sdk_message_contentTv));
            holder.bjmgf_sdk_message_timeTv = (TextView) convertView
                    .findViewById(ReflectResourceId.getViewId(context,
                            Resource.id.bjmgf_sdk_message_timeTv));
            // 将设置好的布局保存到缓存中，并将其设置在Tag里，以便后面方便取出Tag
            convertView.setTag(holder);
        } else {
            holder = (View_Holder) convertView.getTag();
        }

        holder.position = position;
        /** 设置头像 */
        setHeadImg(holder, messageData);
        /** 设置信息内容显示 */
        setContentText(holder, messageData);
        /** 设置发送时间 */
        holder.bjmgf_sdk_message_timeTv.setText(DateUtil.formatTime(context,messageData.time));
        convertView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (listener != null) {
                    listener.onClickMessageItem(holder.position);
                }
            }
        });
        return convertView;
    }

    private void setHeadImg(View_Holder holder, MessageNotifyData messageData) {

        if (messageData.msgType.equals(MessageNotifyData.Msg_Type_Hi_Message)) {
            holder.bjmgf_sdk_message_headImg
                    .setImageResource(ReflectResourceId.getDrawableId(context,
                            Resource.drawable.bjmgf_sdk_head_hi));// 打招呼
        } else if (messageData.msgType
                .equals(MessageNotifyData.Msg_Type_System_Message)) {
            holder.bjmgf_sdk_message_headImg.setImageResource(ReflectResourceId
                    .getDrawableId(context,
                            Resource.drawable.bjmgf_sdk_head_system));// 系统消息
        } else if (messageData.msgType
                .equals(MessageNotifyData.Msg_Type_Normal_Message)) {
            if (StringUtility.isExitsNullStr(MessageNoticeTools.getInstance().getUserData(messageData.uid).faceUrl)) {
                if (MessageNoticeTools.getInstance().getUserData(messageData.uid).sex
                        .equals(SysConstant.SEX_MALE)) {
                    holder.bjmgf_sdk_message_headImg
                            .setImageResource(ReflectResourceId
                                    .getDrawableId(context,
                                            "bjmgf_sdk_user_default_icon_male"));// 男默认头像
                } else {
                    holder.bjmgf_sdk_message_headImg
                            .setImageResource(ReflectResourceId.getDrawableId(
                                    context,
                                    "bjmgf_sdk_user_default_icon_female"));// 女默认头像
                }
            } else {
                String faceUrl = MessageNoticeTools.getInstance().getUserData(messageData.uid).faceUrl;
                int rectSize = (int) context.getResources().getDimension(
                        ReflectResourceId.getDimenId(context,
                                Resource.dimen.bjmgf_sdk_user_head_icon_size));
                imageLoaderUtil.loadImageUrl(context,
                        holder.bjmgf_sdk_message_headImg, faceUrl, null,
                        rectSize);
            }
        }

    }

    private void setContentText(View_Holder holder,
                                MessageNotifyData messageData) {
        if (messageData.msgType.equals(MessageNotifyData.Msg_Type_Hi_Message)) {
            String nick = MessageNoticeTools.getInstance().getUserData(messageData.uid).nick;
            holder.bjmgf_sdk_message_titleTv.setText(Utility.getString(
                      Resource.string.bjmgf_sdk_dock_message_to_hi, context));
            if (messageData.type == 0) {
                holder.bjmgf_sdk_message_contentTv
                          .setText(HI_Strings[MessageNotifyData.Hi_Type_Flower_Message]
                                    + messageData.content);
            } else if (messageData.type == 1) {
                // holder.bjmgf_sdk_message_contentTv
                // .setText(HI_Strings[MessageNotifyData.Hi_Type_Normal_Message]
                // + messageData.content);
                holder.bjmgf_sdk_message_contentTv.setText(messageData.content);
            } else if (messageData.type == 2) {
                holder.bjmgf_sdk_message_contentTv
                          .setText(HI_Strings[MessageNotifyData.Hi_Type_Wish_Message]
                                    + messageData.content);
            } else if (messageData.type == 3) {
                holder.bjmgf_sdk_message_contentTv
                          .setText(MessageNoticeTools.getInstance().getUserData(messageData.uid).nick
                                    + Utility.getString(
                                    Resource.string.bjmgf_sdk_dock_message_game_notify_type_three,
                                    context));
            } else if (messageData.type == 4) {
                holder.bjmgf_sdk_message_contentTv
                          .setText(MessageNoticeTools.getInstance().getUserData(messageData.uid).nick
                                    + Utility.getString(
                                    Resource.string.bjmgf_sdk_dock_message_game_notify_type_four,
                                    context));
            } else if (messageData.type == 5) {
                holder.bjmgf_sdk_message_contentTv
                          .setText(HI_Strings[MessageNotifyData.Hi_Type_Keep_Mistress_Message]
                                    + messageData.content);
            } else {
                holder.bjmgf_sdk_message_contentTv
                          .setText(ReflectResourceId
                                    .getStringId(
                                              context,
                                              Resource.string.bjmgf_sdk_dock_no_message_is_hi));
            }
        } else

        if (messageData.msgType
                  .equals(MessageNotifyData.Msg_Type_System_Message)) {
            holder.bjmgf_sdk_message_titleTv.setText(Utility.getString(
                      Resource.string.bjmgf_sdk_dock_message_game_system_user,
                      context));
            holder.bjmgf_sdk_message_contentTv.setText(messageData.title);
        } else

        if (messageData.msgType
                  .equals(MessageNotifyData.Msg_Type_Normal_Message)) {
            toSet(holder, messageData, messageData.type);
        }

    }

    private void toSet(View_Holder holder, MessageNotifyData messageData,
                       int type) {
        if (StringUtility.isEmpty(MessageNoticeTools.getInstance().getUserData(messageData.uid).nick)) {
            holder.bjmgf_sdk_message_titleTv.setText(ReflectResourceId
                      .getStringId(context,
                                Resource.string.bjmgf_sdk_dock_message_null));
        }

        if (StringUtility.isEmpty(messageData.mes)) {
            holder.bjmgf_sdk_message_contentTv.setText(ReflectResourceId
                      .getStringId(context,
                                Resource.string.bjmgf_sdk_dock_message_null));
        }

        if (type == MessageNotifyData.Normal_Type_Text_Message) {
            holder.bjmgf_sdk_message_titleTv.setText(MessageNoticeTools.getInstance().getUserData(messageData.uid).nick);
            holder.bjmgf_sdk_message_contentTv.setText(messageData.mes);
        } else if (type == MessageNotifyData.Normal_Type_Img_Message
                  || type == MessageNotifyData.Normal_Type_Voice_Message
                  || type == MessageNotifyData.Normal_Type_Flower_Message) {
            holder.bjmgf_sdk_message_titleTv.setText(MessageNoticeTools.getInstance().getUserData(messageData.uid).nick);
            holder.bjmgf_sdk_message_contentTv.setText(Normal_Strings[type]);
        } else if (type == MessageNotifyData.Normal_Type_Game_Message) {
            String gameName = messageData.mes
                      .split(Resource.string.bjmgf_sdk_dock_split_sign)[1];
            holder.bjmgf_sdk_message_titleTv.setText(MessageNoticeTools.getInstance().getUserData(messageData.uid).nick);
            holder.bjmgf_sdk_message_contentTv.setText(ReflectResourceId
                      .getStringId(context,
                                Resource.string.bjmgf_sdk_dock_message_game_part1)
                      + gameName
                      + ReflectResourceId.getStringId(context,
                      Resource.string.bjmgf_sdk_dock_message_game_part2));
        } else {
            holder.bjmgf_sdk_message_titleTv.setText(MessageNoticeTools.getInstance().getUserData(messageData.uid).nick);
            holder.bjmgf_sdk_message_contentTv.setText(Normal_Strings[type]);
        }
    }

    final class View_Holder {

        public ImageView bjmgf_sdk_message_headImg;
        public TextView bjmgf_sdk_message_titleTv;
        public TextView bjmgf_sdk_message_contentTv;
        public TextView bjmgf_sdk_message_timeTv;
        public int position;
    }

    public interface MessageItemListener {
        public void onClickMessageItem(int position);
    }
}
