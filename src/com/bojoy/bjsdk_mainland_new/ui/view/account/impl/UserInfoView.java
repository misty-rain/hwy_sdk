package com.bojoy.bjsdk_mainland_new.ui.view.account.impl;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bojoy.bjsdk_mainland_new.app.tools.BJMGFSDKTools;
import com.bojoy.bjsdk_mainland_new.congfig.SysConstant;
import com.bojoy.bjsdk_mainland_new.eventhandler.event.FileRevEvent;
import com.bojoy.bjsdk_mainland_new.model.entity.UserData;
import com.bojoy.bjsdk_mainland_new.presenter.account.IAccountCenterPresenter;
import com.bojoy.bjsdk_mainland_new.presenter.account.impl.AccountCenterPresenterImpl;
import com.bojoy.bjsdk_mainland_new.support.eventbus.EventBus;
import com.bojoy.bjsdk_mainland_new.ui.activity.base.BJMGFActivity;
import com.bojoy.bjsdk_mainland_new.ui.page.PageManager;
import com.bojoy.bjsdk_mainland_new.ui.page.base.BaseActivityPage;
import com.bojoy.bjsdk_mainland_new.ui.view.account.IUserInfoView;
import com.bojoy.bjsdk_mainland_new.utils.ImageCropUtil;
import com.bojoy.bjsdk_mainland_new.utils.ImageLoaderUtil;
import com.bojoy.bjsdk_mainland_new.utils.ImageUtil;
import com.bojoy.bjsdk_mainland_new.utils.LogProxy;
import com.bojoy.bjsdk_mainland_new.utils.ObjectUtil;
import com.bojoy.bjsdk_mainland_new.utils.ReflectResourceId;
import com.bojoy.bjsdk_mainland_new.utils.Resource;
import com.bojoy.bjsdk_mainland_new.utils.StringUtility;
import com.bojoy.bjsdk_mainland_new.utils.ToastUtil;
import com.bojoy.bjsdk_mainland_new.utils.UIUtil;
import com.bojoy.bjsdk_mainland_new.utils.Utility;
import com.bojoy.bjsdk_mainland_new.widget.dialog.BJMSdkDialog;
import com.bojoy.bjsdk_mainland_new.widget.dialog.SexDialogPage;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wutao on 2016/1/11.
 * 用户中心视图
 */
public class UserInfoView extends BaseActivityPage implements IUserInfoView {

    private final String TAG = UserInfoView.class.getSimpleName();

    private TextView nickText, sexText, birthdayText;
    private ImageView faceImage, sexArrowImage;
    private RelativeLayout mSexLayout, mBirthdayLayout, mFaceImageLayout,
              mNickNameLayout;
    private ImageLoaderUtil imageLoaderUtil = ImageLoaderUtil.getInstance();

    private int roundRectSize, themeResId;
    IAccountCenterPresenter iAccountCenterPresenter;
    EventBus eventBus = EventBus.getDefault();
    //用来标识 修改资料时 修改个人信息还是注册好玩友平台 ，0 为 未注册 1已注册;
    private int flag = 0;
    private UserData baseUserData, afterUserData;

    private ImageLoaderUtil imageLoaderHelper = ImageLoaderUtil.getInstance();
    //用来判断是否修改了头像
    private boolean modifyFaceFlag;
    private String filePath,backFilePath;

    public UserInfoView(Context context, PageManager manager,
                        BJMGFActivity activity) {
        super(ReflectResourceId.getLayoutId(context,
                  Resource.layout.bjmgf_sdk_dock_account_page_info), context,
                  manager, activity);
        themeResId = activity.getThemeId();
        imageLoaderHelper.init(context);
        eventBus.register(this);
    }

    @Override
    public void onCreateView(View view) {
        nickText = getView(Resource.id.bjmgf_sdk_float_account_manager_nickname_contentId);
        sexText = getView(Resource.id.bjmgf_sdk_float_account_manager_sex_contentId);
        birthdayText = getView(Resource.id.bjmgf_sdk_float_account_manager_birthday_contentId);
        faceImage = getView(Resource.id.bjmgf_sdk_float_account_manager_headiconImageId);
        mNickNameLayout = getView(Resource.id.bjmgf_sdk_float_account_manager_nicknameItemId);
        mSexLayout = getView(Resource.id.bjmgf_sdk_float_account_manager_sexId);
        mBirthdayLayout = getView(Resource.id.bjmgf_sdk_float_account_manager_birthdayId);
        mFaceImageLayout = getView(Resource.id.bjmgf_sdk_float_account_manager_headiconId);
        sexArrowImage = getView(Resource.id.bjmgf_sdk_float_account_manager_sex_arrowImageId);

        //昵称
        mNickNameLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ModifyNickNameView page = new ModifyNickNameView(context, manager, activity);
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("nickName", afterUserData.getNick() != null ? nickText.getText() : "");
                page.putParams(params);
                manager.addPage(page);
            }
        });

        //生日
        mBirthdayLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // LogProxy.i(TAG, " birthday = --------------------");
                if (Utility.isFastDoubleClick()) {
                    return;
                }
                UIUtil.openDatePick(context, afterUserData, themeResId, birthdayText);
            }
        });

        //性别
        mSexLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (baseUserData.getSex() == null) {
                    SexDialogPage.getInstance().openSexChooiceDialog(context, sexText, faceImage, afterUserData, modifyFaceFlag);
                }

            }
        });


        //更换头像
        mFaceImageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 图片裁剪
                ImageCropUtil imageCropUtil = ImageCropUtil.getInstance(activity);
                imageCropUtil.InvokeOpenImageSelectorMessage();
            }
        });

        super.onCreateView(view);
    }


    @Override
    public void onResume() {
        super.onResume();
        if (getParams().get("nickName") != null) {
            afterUserData.setNick(String.valueOf(getParams().get("nickName")));
            nickText.setText(afterUserData.getNick());
            nickText.setTextColor(context.getResources().getColor(
                      ReflectResourceId.getColorId(context,
                                Resource.color.bjmgf_sdk_black)));
        }

    }

    @Override
    public void setView() {
        iAccountCenterPresenter = new AccountCenterPresenterImpl(context, this);
        showUserInfo();
        if (baseUserData != null)
            afterUserData = BJMGFSDKTools.getInstance().cloneUserData(baseUserData);
        //setFaceImage();


        /**
         * 返回操作
         */
        setBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (!ObjectUtil.domainEquals(baseUserData, afterUserData) || modifyFaceFlag) {
                    showSaveDialog();
                } else {
                    manager.previousPage();
                }
            }
        });

        /**
         * 保存个人信息
         */
        setRightButtonClick(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                checkTextIsEmpty();
                showProgressDialog();
                if (flag == 1)
                    editUserInfo();
                   // uploadFaceIcon();
                else
                    registerHWYPlatform();


            }
        });

    }

    /**
     * 修改个人信息
     */
    private void editUserInfo() {
        iAccountCenterPresenter.editUserInfo(context, nickText.getText().toString().trim(), birthdayText.getText().toString().trim());
    }

    /**
     * 注册好玩友平台
     */
    private void registerHWYPlatform() {
        iAccountCenterPresenter.hwyPlatformRegister(context, nickText.getText().toString().trim(), sexText.getText().toString().trim(), birthdayText.getText().toString().trim());
    }

    /**
     * 上传头像
     */
    private void uploadFaceIcon() {
        iAccountCenterPresenter.uploadUserFace(context, faceImage, filePath);
    }


    /**
     * 检查内容是否为空
     *
     * @return
     */
    private void checkTextIsEmpty() {
        if (StringUtility.isEmpty(nickText.getText().toString())) {
            ToastUtil.showMessage(context, getString(Resource.string.bjmgf_sdk_input_nick_title));
            return;
        }
        if (StringUtility.isEmpty(sexText.getText().toString())) {
            ToastUtil.showMessage(context, getString(Resource.string.bjmgf_sdk_input_sex_title));
            return;
        }

        if (StringUtility.isEmpty(birthdayText.getText().toString())) {
            ToastUtil.showMessage(context, getString(Resource.string.bjmgf_sdk_input_birthday));
            return;
        }

    }


    @Override
    public void showError(String message) {
        dismissProgressDialog();
        ToastUtil.showMessage(context, message);
    }

    @Override
    public void showSuccess() {
        dismissProgressDialog();
        mBundle.putString("facePath", backFilePath);
        goBack();
    }

    /**
     * 显示头像
     */
    private void setFaceImage() {
        if (StringUtility.isEmpty(filePath)) {

            faceImage.setImageResource(ReflectResourceId.getDrawableId(
                      context,
                      Resource.drawable.bjmgf_sdk_user_default_icon_female));
        } else {
            roundRectSize = (int) context.getResources().getDimension(
                      ReflectResourceId.getDimenId(context,
                                Resource.dimen.bjmgf_sdk_user_head_icon_size));
            imageLoaderHelper.loadImageUrl(context, faceImage,
                      baseUserData.getFaceUrl(), null, roundRectSize);
        }
    }


    /**
     * 显示用户信息
     */
    @Override
    public void showUserInfo() {
        baseUserData = BJMGFSDKTools.getInstance().getCurrUserData();
        if (baseUserData != null) {
            flag = 1;
            if (baseUserData.getNick() != null) {
                nickText.setText(baseUserData.getNick());
                nickText.setTextColor(context.getResources().getColor(
                          ReflectResourceId.getColorId(context,
                                    Resource.color.bjmgf_sdk_black)));
            } else {
                nickText.setText(getString(Resource.string.bjmgf_sdk_floatWindow_accountManager_inputNickName));
                nickText.setTextColor(context.getResources().getColor(
                          ReflectResourceId.getColorId(context,
                                    Resource.color.bjmgf_sdk_text_gray)));
            }

            if (baseUserData.getSex() != null) {
                sexText.setText(baseUserData.equals(SysConstant.SEX_MALE) ? getString(Resource.string.bjmgf_sdk_sex_male) : getString(Resource.string.bjmgf_sdk_sex_female));
                sexText.setTextColor(context.getResources().getColor(
                          ReflectResourceId.getColorId(context,
                                    Resource.color.bjmgf_sdk_black)));
            } else {
                sexText.setText(getString(Resource.string.bjmgf_sdk_floatWindow_accountManager_chooseSex));
                sexText.setTextColor(context.getResources().getColor(
                          ReflectResourceId.getColorId(context,
                                    Resource.color.bjmgf_sdk_text_gray)));
            }
            if (baseUserData.getBirth() != null)
                if (baseUserData.getBirth().length() > 11)
                    birthdayText.setText(baseUserData.getBirth().substring(0, 11));
                else
                    birthdayText.setText(baseUserData.getBirth());
            else {

                birthdayText
                          .setText(getString(Resource.string.bjmgf_sdk_floatWindow_accountManager_chooseBirthDay));
                birthdayText.setTextColor(context.getResources().getColor(
                          ReflectResourceId.getColorId(context,
                                    Resource.color.bjmgf_sdk_text_gray)));
            }

            if (baseUserData.getFaceUrl() != null)
                imageLoaderUtil.loadImageUrl(context, faceImage, baseUserData.faceUrl, null, (int) context.getResources().getDimension(
                          ReflectResourceId.getDimenId(context,
                                    Resource.dimen.bjmgf_sdk_user_head_icon_size)));
        }
    }

    @Override
    public void showEditUserInfoSuccess() {
        if (modifyFaceFlag && filePath != null)
            uploadFaceIcon();
        else
            showSuccess();
    }

    @Override
    public void showRegisterHwySuccess() {
        if (modifyFaceFlag && filePath != null)
            uploadFaceIcon();
        else
            showSuccess();
    }

    @Override
    public void showUploadHeadIconSuccess() {
        showSuccess();
    }

    public void onEventMainThread(FileRevEvent event) {
        filePath = event.getFilePath();

        if (StringUtility.isEmpty(filePath)) {
            return;
        }
        LogProxy.i(TAG, filePath);
        if (!Utility.checkImagePath(filePath)) {
            showError(getString(Resource.string.bjmgf_sdk_question_file_invalid_format));
            return;
        }
        baseUserData.faceUrl = ImageUtil.saveBitmap(activity, filePath);
        if (!StringUtility.isEmpty(baseUserData.faceUrl)) {
            imageLoaderHelper.clearCache();
            baseUserData.faceUrl = "file://" + baseUserData.faceUrl;
            backFilePath = baseUserData.faceUrl;
            modifyFaceFlag = true;
        }
        LogProxy.i(TAG, baseUserData.faceUrl);
        setFaceImage();


    }


    /**
     * 弹出 确认离开修改个人信息 对话框
     */

    public void showSaveDialog() {
        final BJMSdkDialog dialog = new BJMSdkDialog(context);
        dialog.setTitle(Utility
                  .getString(
                            Resource.string.bjmgf_sdk_floatWindow_accountManager_dialog_editedAccountInfo_titleStr,
                            context));
        dialog.setMessage(Utility
                  .getString(
                            Resource.string.bjmgf_sdk_floatWindow_accountManager_dialog_editedAccountInfo_contentStr,
                            context));
        dialog.setPositiveButton(
                  Utility.getString(
                            Resource.string.bjmgf_sdk_floatWindow_accountManager_dialog_no_saveBtnStr,
                            context), new View.OnClickListener() {

                      @Override
                      public void onClick(View v) {
                          dialog.dismiss();
                          manager.previousPage();
                      }
                  });

        dialog.setNegativeButton(
                  Utility.getString(
                            Resource.string.bjmgf_sdk_floatWindow_accountManager_dialog_saveBtnStr,
                            context), new View.OnClickListener() {

                      @Override
                      public void onClick(View v) {
                          dialog.dismiss();
                      }

                  });
        dialog.show();
    }
}
