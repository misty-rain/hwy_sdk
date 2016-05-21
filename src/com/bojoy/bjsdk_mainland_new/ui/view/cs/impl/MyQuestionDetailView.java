package com.bojoy.bjsdk_mainland_new.ui.view.cs.impl;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bojoy.bjsdk_mainland_new.eventhandler.event.FileRevEvent;
import com.bojoy.bjsdk_mainland_new.model.entity.MyQuestionBean;
import com.bojoy.bjsdk_mainland_new.model.entity.MyQuestionDetailBean;
import com.bojoy.bjsdk_mainland_new.presenter.cs.ICustomerServicePresenter;
import com.bojoy.bjsdk_mainland_new.presenter.cs.impl.CustomerServicePresenterImpl;
import com.bojoy.bjsdk_mainland_new.support.eventbus.EventBus;
import com.bojoy.bjsdk_mainland_new.ui.activity.base.BJMGFActivity;
import com.bojoy.bjsdk_mainland_new.ui.adapter.MyQuestionDetailListViewAdapter;
import com.bojoy.bjsdk_mainland_new.ui.page.PageManager;
import com.bojoy.bjsdk_mainland_new.ui.page.base.BaseActivityPage;
import com.bojoy.bjsdk_mainland_new.ui.view.cs.IMyQuestionDetailView;
import com.bojoy.bjsdk_mainland_new.ui.view.cs.IShowPictureView;
import com.bojoy.bjsdk_mainland_new.utils.LogProxy;
import com.bojoy.bjsdk_mainland_new.utils.ReflectResourceId;
import com.bojoy.bjsdk_mainland_new.utils.Resource;
import com.bojoy.bjsdk_mainland_new.utils.ToastUtil;
import com.bojoy.bjsdk_mainland_new.utils.Utility;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * 我的问题详情，聊天主页面
 */
public class MyQuestionDetailView extends BaseActivityPage implements IMyQuestionDetailView, IShowPictureView {

    private final String TAG = getClass().getSimpleName();
    private View mView;
    private BJMGFActivity mActivity;
    private PageManager mRootPageManager;// 根布局页面管理器，返回上一页时要用
    private PageManager mQuestionPageManager;// 子页面页面管理器

    private ImageView mBackImageView;

    private Button mQuestionSendButton;
    private ImageView mAddFileImageView;
    // 提示信息
    private View mHintLayout;
    private ImageView mHintImageView;
    private ImageView mClearHintImageView;
    private TextView mHintTextView;

    private View mListViewContainerLayout;
    private ListView mListView;// 聊天list
    private MyQuestionDetailListViewAdapter mAdapter;
    private EditText mInputEditText;// 输入框

    // 问题描述:head view，要添加具体内容，及控件
    private View mHeaderView;
    //问题创建时间
    private TextView mQuestionCreateTimeTextView;
    //问题编号
    private TextView mQuestionNumberTextView;
    //问题类型，全称
    private TextView mQuestionTypeTotalTextView;
    //服务器，全称
    private TextView mServerTotalTextView;
    //角色名，全称
    private TextView mRoleNameTotalTextView;
    //问题标题
    private TextView mQuestionTitleTextView;
    //问题内容
    private TextView mQuestionContentTextView;
    //文件链接
    private TextView mExtraFileTextView;

    private LinkedList<MyQuestionDetailBean> mDatas;

    private MyQuestionBean mMyQuestionBean;

    private ICustomerServicePresenter iCustomerServicePresenter;

    //图片地址
    private String picPath = "";

    public MyQuestionDetailView(Context context, PageManager rootPageManager, BJMGFActivity activity,
                                MyQuestionBean myQuestionBean) {
        super(ReflectResourceId.getLayoutId(activity, Resource.layout.bjmgf_sdk_question_detail_content),
                context, rootPageManager, activity);
        this.mActivity = activity;
        this.mRootPageManager = rootPageManager;
        this.mMyQuestionBean = myQuestionBean;
        // 控制层
        iCustomerServicePresenter = new CustomerServicePresenterImpl(context, this);
    }

    @Override
    public void onCreateView(View parentView) {
        this.mView = parentView;
        initView();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateView();
    }

    @Override
    public void onAttach(ViewGroup parent) {
        super.onAttach(parent);
        //注册事件监听，将来或许会在ViewPager中统一注册监听器，那么就要把这行代码删除
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    protected void initView() {
        mBackImageView = (ImageView) mView.findViewById(ReflectResourceId.getViewId(mActivity,
                Resource.id.bjmgf_sdk_closeCustomerCenterBtnId));
        // 问题发送栏
        mQuestionSendButton = (Button) mView.findViewById(ReflectResourceId.getViewId(mActivity,
                Resource.id.bjmgf_sdk_dialog_send));
        mInputEditText = (EditText) mView.findViewById(ReflectResourceId.getViewId(mActivity,
                Resource.id.bjmgf_sdk_dialog_edit));
        mAddFileImageView = (ImageView) mView.findViewById(ReflectResourceId.getViewId(mActivity,
                Resource.id.bjmgf_sdk_dialog_addfile));

        // 提示布局
        mHintLayout = mView.findViewById(ReflectResourceId.getViewId(mActivity,
                Resource.id.bjmgf_sdk_attachment_hint_layout));
        mHintImageView = (ImageView) mView.findViewById(ReflectResourceId.getViewId(mActivity,
                Resource.id.bjmgf_sdk_attachment_image));
        mHintTextView = (TextView) mView.findViewById(ReflectResourceId.getViewId(mActivity,
                Resource.id.bjmgf_sdk_attachment_hint_text));
        mClearHintImageView = (ImageView) mView.findViewById(ReflectResourceId.getViewId(mActivity,
                Resource.id.bjmgf_sdk_attachment_clear));

        //ListView
        mListViewContainerLayout = mView.findViewById(ReflectResourceId.getViewId(mActivity,
                Resource.id.bjmgf_sdk_question_dialog_linner));
        mListView = (ListView) mView.findViewById(ReflectResourceId.getViewId(mActivity,
                Resource.id.bjmgf_sdk_question_dialog_list));

        // 问题详情
        mHeaderView = LayoutInflater.from(mActivity).inflate(ReflectResourceId.getLayoutId(
                mActivity, Resource.layout.bjmgf_sdk_question_detail_header), null);
        mQuestionCreateTimeTextView = (TextView) mHeaderView.findViewById(ReflectResourceId.getViewId(mActivity,
                Resource.id.bjmgf_sdk_question_create_time));
        mQuestionNumberTextView = (TextView) mHeaderView.findViewById(ReflectResourceId.getViewId(mActivity,
                Resource.id.bjmgf_sdk_question_number_total));
        mQuestionTypeTotalTextView = (TextView) mHeaderView.findViewById(ReflectResourceId.getViewId(mActivity,
                Resource.id.bjmgf_sdk_question_type_total));
        mServerTotalTextView = (TextView) mHeaderView.findViewById(ReflectResourceId.getViewId(mActivity,
                Resource.id.bjmgf_sdk_question_service_total));
        mRoleNameTotalTextView = (TextView) mHeaderView.findViewById(ReflectResourceId.getViewId(mActivity,
                Resource.id.bjmgf_sdk_question_role_name_total));
        mQuestionTitleTextView = (TextView) mHeaderView.findViewById(ReflectResourceId.getViewId(mActivity,
                Resource.id.bjmgf_sdk_question_title));
        mQuestionContentTextView = (TextView) mHeaderView.findViewById(ReflectResourceId.getViewId(mActivity,
                Resource.id.bjmgf_sdk_question_content));
        mExtraFileTextView = (TextView) mHeaderView.findViewById(ReflectResourceId.getViewId(mActivity,
                Resource.id.bjmgf_sdk_getfile));

        mAddFileImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.pickPicture(activity);
            }
        });
        //发送
        mQuestionSendButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                //先加判断，长度不能大于100,附件地址交由presenter判断，真的要显示Dialog吗？
                if (isInputTextAvailable(mInputEditText.getText().toString())) {
                    showProgressDialog();
                    LogProxy.d(TAG, "pic url ::" + picPath);
                    iCustomerServicePresenter.sendMyQuestionDetailAppend(context, mMyQuestionBean.getId(),
                            mMyQuestionBean.getType(), mMyQuestionBean.getSubtype(), picPath,
                            mInputEditText.getText().toString());

                } else {
                    ToastUtil.showMessage(context, context.getString(ReflectResourceId.getStringId(context,
                            Resource.string.bjmgf_sdk_question_append_content_null)));
                }

            }
        });
        //清除缩略图，隐藏提示栏
        mClearHintImageView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                picPath = "";
                mHintLayout.setVisibility(View.GONE);
            }
        });
        //点击缩略图打开图片
        mHintImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = new File(picPath);
                Intent intent = new Intent();
                intent.setAction(android.content.Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(file), "image/*");
                context.startActivity(intent);
            }
        });
        mBackImageView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mRootPageManager.previousPage();
            }
        });
        //ListView  HeaderView 的附件
        mExtraFileTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                LogProxy.d(TAG, mMyQuestionBean.getAttachment());
                showProgressDialog();
                iCustomerServicePresenter.getPicWithURL(context, mMyQuestionBean.getAttachment());
            }
        });
        //只能添加一次，否则会使ListView显示很不稳定
        mListView.addHeaderView(mHeaderView);
    }

    /**
     * 更新界面，为控件设置数据，只要有网络请求，就调showProcessDialog()
     */
    public void updateView() {
        mListViewContainerLayout.setVisibility(View.VISIBLE);
        mListView.setVisibility(View.VISIBLE);
        mQuestionCreateTimeTextView.setText(mMyQuestionBean.getCreate());
        //问题编号
        mQuestionNumberTextView.setText(mMyQuestionBean.getId());
        //服务器，全称
        mServerTotalTextView.setText(mMyQuestionBean.getServerid());
        //角色名，全称
        mRoleNameTotalTextView.setText(mMyQuestionBean.getRolename());
        //问题标题
        mQuestionTitleTextView.setText(mMyQuestionBean.getTitle());
        //问题内容
        mQuestionContentTextView.setText(mMyQuestionBean.getContent());
        //问题附件图片
        LogProxy.d(TAG, "mExtraFileTextView getAttachment==" + mMyQuestionBean.getAttachment());
        if (mMyQuestionBean.getAttachment() == null) {
            mExtraFileTextView.setVisibility(View.GONE);
        } else {
            mExtraFileTextView.setVisibility(View.VISIBLE);
        }

        //刷新聊天记录列表
        iCustomerServicePresenter.getMyQuestionDetailRecord(mActivity, mMyQuestionBean.getId());
        //获取问题类型，未联网
        iCustomerServicePresenter.setMyQuestionDetailQuestionType(mActivity,
                mMyQuestionBean.getType(), mMyQuestionBean.getSubtype());
        showProgressDialog();
    }

    /**
     * 显示聊天记录列表，默认显示至列表结尾，如果有需求第一次显示头，发送问题后显示至结尾，可以getMyQuestionDetailRecord()函数中传递一个Boolean值，
     * 在每次请求时在presenter层保存，把Boolean值传递到showMyQuestionDetailRecord()函数，从源头控制ListView的显示
     *
     * @param datas 聊天数据
     */
    @Override
    public void showMyQuestionDetailRecord(ArrayList<MyQuestionDetailBean> datas) {
        dismissProgressDialog();
        mAdapter = new MyQuestionDetailListViewAdapter(mActivity, datas, mRootPageManager, mActivity);
        mListView.setAdapter(mAdapter);
        mListView.setSelection(ListView.FOCUS_DOWN);
        for (MyQuestionDetailBean b : datas) {
            LogProxy.d(TAG, "showMyQuestionDetailRecord=" + b.getContent());
        }
    }

    /**
     * 显示问题根类型和子类型
     *
     * @param text 问题类型
     */
    @Override
    public void showMyQuestionType(String text) {
        mQuestionTypeTotalTextView.setText(text);
    }


    /**
     * 聊天界面发送问题回调，根据返回码判断，如果返回码为0，不回调，返回码为1，回调
     */
    @Override
    public void showQuestionAppendSendResult() {
        dismissProgressDialog();
        //隐藏界面，数据处理
        picPath = "";
        mHintImageView.setVisibility(View.GONE);
        mHintLayout.setVisibility(View.GONE);
        mInputEditText.setText("");

        //问题追加 提交成功，刷新ListView
        iCustomerServicePresenter.getMyQuestionDetailRecord(mActivity, mMyQuestionBean.getId());
    }

    /**
     * 对发送的文本信息做非空性判断
     *
     * @param inputString 输入的字符
     * @return
     */
    private boolean isInputTextAvailable(String inputString) {
        if (inputString == null || inputString.trim().equals("")) {
            return false;
        }
        String str;
        if (("").equals(str = inputString.trim().replaceAll("\t", "").replaceAll("\n", "")
                .replaceAll("\r", "").replaceAll("\f", ""))) {
            return false;
        }
        return true;
    }

    /**
     * @param file 下载的图片文件
     */
    @Override
    public void showPicture(File file) {
        LogProxy.d(TAG, TAG + "\tshowPicture::" + (file == null ? true : file.getPath()));
        dismissProgressDialog();
        if (file == null || !Utility.checkFilePath(file.getPath())) {
            //图片正在审核，请稍后查看
            ToastUtil.showMessage(context, context.getString(ReflectResourceId.getStringId(context,
                    Resource.string.bjmgf_sdk_question_image_load_failed)));
            return;
        }
        ImageViewerView imageViewerView = new ImageViewerView(context, manager, activity, file);
        manager.addPage(imageViewerView);
    }

    /**
     * 图片未获取到产生的错误：图片正在审核
     * @param message 错误信息
     * @param errorCode 返回码：请求码
     */
    @Override
    public void showPictureNotAvailableError(String message, int errorCode) {
        dismissProgressDialog();
        //图片正在审核，请稍后查看
        ToastUtil.showMessage(context, context.getString(ReflectResourceId.getStringId(context,
                Resource.string.bjmgf_sdk_question_image_load_failed)));
    }

    /**
     * 使用EventBus，异步展示图片
     *
     * @param revEvent
     */
    public void onEventMainThread(FileRevEvent revEvent) {
        picPath = revEvent.getFilePath();
        LogProxy.d(TAG, "onEventMainThread FileRevEvent file path::" + picPath);
        if (!Utility.checkImagePath(picPath)) {
            ToastUtil.showMessage(context, getString(Resource.string.bjmgf_sdk_question_file_invalid_format));
            return;
        }
        int outSize = (int) context.getResources().getDimension(ReflectResourceId.getDimenId(context,
                Resource.dimen.bjmgf_sdk_attach_iamge_size));
        Bitmap bmp = Utility.getScaleBitmapFromFile(picPath, outSize, outSize);
        mHintLayout.setVisibility(View.VISIBLE);
        mHintImageView.setVisibility(View.VISIBLE);
        mHintImageView.setImageBitmap(bmp);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //EventBus取消注册
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    public void showError(String message) {
        LogProxy.d(TAG, TAG + "\tshowPicture::error");
        dismissProgressDialog();
        ToastUtil.showMessage(context, message);
        mHintLayout.setVisibility(View.GONE);
        mInputEditText.setText("");
    }

    @Override
    public void showSuccess() {
        dismissProgressDialog();
    }

    @Override
    public void setView() {

    }
}
