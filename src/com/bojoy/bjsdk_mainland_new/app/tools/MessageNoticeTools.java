package com.bojoy.bjsdk_mainland_new.app.tools;

import android.content.Context;

import com.bojoy.bjsdk_mainland_new.model.entity.MessageNotifyData;
import com.bojoy.bjsdk_mainland_new.model.entity.UserData;
import com.bojoy.bjsdk_mainland_new.support.fastjson.JSON;
import com.bojoy.bjsdk_mainland_new.support.fastjson.JSONArray;
import com.bojoy.bjsdk_mainland_new.support.fastjson.JSONObject;
import com.bojoy.bjsdk_mainland_new.utils.LogProxy;
import com.bojoy.bjsdk_mainland_new.utils.StringUtility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wutao on 2016/1/27.
 * 消息功能模块 工具类， 所有关于消息模块的公共处理方法
 */
public class MessageNoticeTools {

    private final String TAG = MessageNoticeTools.class.getSimpleName();
    private static MessageNoticeTools messageNoticeTools;
    //当前用户消息列表中用户集合
    public Map<Long, UserData> currMsgUserMap;


    private MessageNoticeTools() {
    }

    public static MessageNoticeTools getInstance() {
        if (messageNoticeTools == null) {
            synchronized (MessageNoticeTools.class) {
                if (messageNoticeTools == null) {
                    messageNoticeTools = new MessageNoticeTools();
                }
            }
        }
        return messageNoticeTools;
    }


    /**
     * 从当前用户集合中获取单个user
     * @param uid
     * @return
     */
    public UserData getUserData(long uid) {
        return currMsgUserMap.get(uid);
    }

    public void setCurrMsgUserMap(Map<Long, UserData> currMsgUserMap) {
        this.currMsgUserMap = currMsgUserMap;
    }


    /**
     * 时间排序比较器
     */
    private Comparator<MessageNotifyData> timeComparator = new Comparator<MessageNotifyData>() {

        @Override
        public int compare(MessageNotifyData arg0, MessageNotifyData arg1) {
            if (arg0.time != arg1.time) {
                return arg1.time - arg0.time;
            } else {
                return arg1.msgType.compareTo(arg0.msgType);
            }
        }
    };

    /**
     * 获得未读消息列表
     *
     * @param jsonStr
     * @return
     */
    public ArrayList<MessageNotifyData> getUnReadMessageList(String jsonStr) {

        ArrayList<MessageNotifyData> messageDetailDataList = new ArrayList<MessageNotifyData>();
        if (StringUtility.isEmpty(jsonStr)) {
            return messageDetailDataList;
        }
        String json = JSON.toJSONString(JSONObject.parseObject(jsonStr).get("c"));
        messageDetailDataList = parseMessageList(json);
        ArrayList<MessageNotifyData> resultMessageList = new ArrayList<MessageNotifyData>();
        MessageNotifyData systemMsg = null;
        MessageNotifyData specialMsg = null;
        Collections.sort(messageDetailDataList, timeComparator);
        for (MessageNotifyData message : messageDetailDataList) {
            LogProxy.i(TAG, message.toString());
            if (message.msgType.equals(MessageNotifyData.Msg_Type_System_Message)) {
                systemMsg = message;
            } else if (message.msgType.equals(MessageNotifyData.Msg_Type_Hi_Message)) {
                specialMsg = message;
            }
        }

        int count = 0;
        if (specialMsg != null) {
            count++;
            resultMessageList.add(specialMsg);
        }
        if (systemMsg != null) {
            count++;
            resultMessageList.add(systemMsg);
        }

        int index = 0;
        for (int i = 0; i < messageDetailDataList.size(); i++) {
            while (true) {
                if (index >= messageDetailDataList.size()) {
                    break;
                }
                MessageNotifyData msg = messageDetailDataList.get(index);
                if (msg.msgType.equals(MessageNotifyData.Msg_Type_Normal_Message)) {
                    resultMessageList.add(msg);
                    index++;
                    break;
                }
                index++;
            }
        }

        for (MessageNotifyData element : resultMessageList) {
            LogProxy.i(TAG, "resultMessageList---uid:" + Long.toString(element.uid));
        }
        return resultMessageList;

    }


    /**
     * 将消息json解析组装成msglist
     *
     * @param jsonStr
     * @return
     */
    public ArrayList<MessageNotifyData> parseMessageList(String jsonStr) {
        LogProxy.i(TAG, "jsonStr=" + jsonStr);
        JSONArray jsonArray = JSON.parseArray(jsonStr);
        ArrayList<MessageNotifyData> list = new ArrayList<MessageNotifyData>();
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String msgType = null;
            JSONObject contentObject = null;
            JSONArray contentArrayObject = null;
            if (jsonObject.containsKey("msgType")) {
                msgType = jsonObject.getString("msgType");
            }
            if (MessageNotifyData.Msg_Type_Normal_Message.equals(msgType)) {
                if (jsonObject.containsKey("content")) {
                    contentArrayObject = jsonObject.getJSONArray("content");
                    if (contentArrayObject == null) {
                        continue;
                    }
                    ArrayList<MessageNotifyData> normalMsgList = (ArrayList<MessageNotifyData>) JSON.parseArray(
                              contentArrayObject.toJSONString(), MessageNotifyData.class);
                    for (MessageNotifyData messageNotifyData : normalMsgList) {
                        messageNotifyData.msgType = msgType;
                        list.add(messageNotifyData);
                    }
                }
            } else if (MessageNotifyData.Msg_Type_Hi_Message.equals(msgType)
                      || MessageNotifyData.Msg_Type_System_Message.equals(msgType)) {
                if (jsonObject.containsKey("content")) {
                    contentObject = jsonObject.getJSONObject("content");
                    if (contentObject == null) {
                        continue;
                    }
                    MessageNotifyData msgData = (MessageNotifyData) JSON.parseObject(
                              contentObject.toJSONString(), MessageNotifyData.class);
                    msgData.msgType = msgType;
                    if (msgData != null) {
                        list.add(msgData);
                    }
                }
            } else {
                continue;
            }

        }
        for (int i = 0; i < list.size(); i++) {
            LogProxy.d(TAG, list.get(i).toString());
        }
        return list;
    }

    /**
     * 从message list 得到id 并将其拼接 用来 用户头像
     *
     * @param messageList
     * @return
     */
    public String getUid(ArrayList<MessageNotifyData> messageList) {
        StringBuffer sBuffer = new StringBuffer();
        for (MessageNotifyData msg : messageList) {
            /** 过滤系统消息 */
            if (msg.msgType.equals(MessageNotifyData.Msg_Type_Normal_Message) ||
                      msg.msgType.equals(MessageNotifyData.Msg_Type_Hi_Message)) {
                sBuffer.append(msg.uid).append(",");
            }
        }
        if (sBuffer.length() > 0) {
            sBuffer.deleteCharAt(sBuffer.length() - 1);
        }
        String toUserIDs = sBuffer.toString();
        return toUserIDs;
    }


    /**
     * 处理组装 用户信息
     * @param context 上下文
     * @param json
     * @return  用户数据集合
     */
    public Map<Long, UserData> parseOtherUsersData(Context context, String json) {
        //用户集合，用来存放查询的用户
        Map<Long, UserData>  otherUserDataMap = new HashMap<Long, UserData>();
        JSONObject root = JSON.parseObject(json);
        if (root.containsKey("userInfos")) {
            JSONArray array = root.getJSONArray("userInfos");
            for (int i = 0; i < array.size(); i++) {
                JSONObject user = array.getJSONObject(i);
                UserData userData = null;
                if (user.containsKey("baseInfo")) {
                    JSONObject base = user.getJSONObject("baseInfo");
                    userData = (UserData) JSON.parseObject(
                              base.toJSONString(), UserData.class);
                }
                if (user.containsKey("uid")) {
                    if (userData != null) {
                        Long uid = user.getLong("uid");
                        userData.setUid(uid);
                        userData.setFaceUrl(BJMGFSDKTools.getInstance().getPFFaceUrl(context, userData.getUid(), userData.getFaceUrl()));
                        otherUserDataMap.put(uid, userData);
                    }
                }
            }
        }
        return otherUserDataMap;
    }


}
