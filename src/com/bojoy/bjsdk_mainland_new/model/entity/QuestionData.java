package com.bojoy.bjsdk_mainland_new.model.entity;

import android.content.Context;

import com.bojoy.bjsdk_mainland_new.utils.LogProxy;
import com.bojoy.bjsdk_mainland_new.utils.ReflectResourceId;
import com.bojoy.bjsdk_mainland_new.utils.Resource;
import com.bojoy.bjsdk_mainland_new.utils.Utility;

public class QuestionData {
	
	private final String TAG = QuestionData.class.getSimpleName();
	
	public final static String File_Name = "feedback_file";
	private final static String subType_Null_TextString = "无";
	private final static int Base_Value = 100;
	
	public int typeIndex = -1;	//问题类型的索引号
	public int subTypeIndex = -1;//问题次级类型索引号
	public int type = -1;		//问题类型
	public int subType = -1;		//问题次级类型
	public String title = "";	//问题标题
	public String content = "";	//问题详细
	public String roleName = "";	//角色名
	public String roleID = "";	//角色ID
	public String serverName = "";//服务器名
	public String serverID = "0"; //服务器ID
	public String filePath = "";	//附件路径
	public String fileName = "";	//附件名称
	public String gameId = "0";//游戏Id
	public String sort = "1"; //工单分类 ，默认为1 游戏内的提单
	public boolean haveRole = false;	//已有角色标识
	public boolean haveServer = false;  //已有服务器标识

	public static final String[] Question_SubType_Names = {
			Resource.array.bjmgf_sdk_question_1_subType,
			Resource.array.bjmgf_sdk_question_2_subType,
			Resource.array.bjmgf_sdk_question_3_subType,
			Resource.array.bjmgf_sdk_question_4_subType,
			Resource.array.bjmgf_sdk_question_5_subType,
			Resource.array.bjmgf_sdk_question_6_subType,
			Resource.array.bjmgf_sdk_question_7_subType,
			Resource.array.bjmgf_sdk_question_8_subType,
			Resource.array.bjmgf_sdk_question_9_subType,
	};
	
	public QuestionData() {
		typeIndex = subTypeIndex = -1;
	}
	
	/**
	 * 设置类型，转换成服务器的计算规则
	 * @param index
	 */
	public void setType(int index) {
		typeIndex = index;
		type = (index + 1) * Base_Value;
	}
	
	/**
	 * 设置次级类型，转换成服务器的计算规则
	 * @param index
	 * @param name
	 */
	public void setSubType(int index, String name) {
		subTypeIndex = index;
		if (name.equals(subType_Null_TextString)) {
			subType = 0;
		} else {
			subType = type + index + 1;
		}
	}
	
	public void setFilaPath(String filePath) {
		if (Utility.stringIsEmpty(filePath)) {
			return;
		}
		if (!Utility.checkFilePath(filePath)) {
			LogProxy.i(TAG, "file path is not valid format");
			return;
		}
		this.filePath = filePath;
		fileName = File_Name;
	}
	
	@Override
	public String toString() {
		return String.format("type = %s, subType = %s, title = %s, content = %s, " +
				"roleName = %s, roleID = %s, serviceName = %s, serverID = %s, " +
				"filePath = %s, fileName = %s, super = %s",  
				type, subType, title, content, roleName, roleID, serverName, serverID, 
				filePath, fileName, super.toString());
	}
	
	public static String getTypeText(Context context, int type) {
		int index = type / Base_Value - 1;
		String[] typeContents = context.getResources().getStringArray(ReflectResourceId.getArrayId(context,
				Resource.array.bjmgf_sdk_question_type));
		if (index < 0 || index >= typeContents.length) {
			return null;
		}
		return typeContents[index];
	}
	
	public String getSubTypeText(Context context, int type, int subType) {
		if (subType == 0) {
			return subType_Null_TextString;
		}
		int index = type / Base_Value - 1;
		int subIndex = subType - type - 1;
		String[] subTypeContent = getQuestionSubTypes(context, index);
		if (subIndex < 0 || subIndex >= subTypeContent.length) {
			return null;
		}
		return subTypeContent[subIndex];
	}

	public String[] getQuestionSubTypes(Context context, int typeIndex) {
		if (typeIndex < 0 || typeIndex >= Question_SubType_Names.length) {
			return null;
		}
		return context.getResources().getStringArray(ReflectResourceId.getArrayId(context,
				Question_SubType_Names[typeIndex]));
	}
	
}
