package com.bojoy.bjsdk_mainland_new.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bojoy.bjsdk_mainland_new.model.entity.MyQuestionDetailBean;
import com.bojoy.bjsdk_mainland_new.presenter.cs.ICustomerServicePresenter;
import com.bojoy.bjsdk_mainland_new.presenter.cs.impl.CustomerServicePresenterImpl;
import com.bojoy.bjsdk_mainland_new.ui.activity.base.BJMGFActivity;
import com.bojoy.bjsdk_mainland_new.ui.page.PageManager;
import com.bojoy.bjsdk_mainland_new.ui.view.cs.IShowPictureView;
import com.bojoy.bjsdk_mainland_new.ui.view.cs.impl.ImageViewerView;
import com.bojoy.bjsdk_mainland_new.utils.DialogUtil;
import com.bojoy.bjsdk_mainland_new.utils.LogProxy;
import com.bojoy.bjsdk_mainland_new.utils.ReflectResourceId;
import com.bojoy.bjsdk_mainland_new.utils.Resource;
import com.bojoy.bjsdk_mainland_new.utils.ToastUtil;
import com.bojoy.bjsdk_mainland_new.utils.Utility;

import java.io.File;
import java.util.ArrayList;

/**
 * <p>
 * 我的问题详情页，聊天数据列表
 * </P>
 * Created by shenliuyong on 2015/12/30.
 */
public class MyQuestionDetailListViewAdapter extends BaseAdapter implements IShowPictureView {

    private final String TAG = MyQuestionDetailListViewAdapter.class.getSimpleName();
    private ArrayList<MyQuestionDetailBean> mDatas;
    private Context mContext;
    private LayoutInflater mInflater;
    private int meLayoutId;
    private int uLayoutId;
    private ICustomerServicePresenter iCustomerServicePresenter;
    private PageManager mManager;
    private BJMGFActivity activity;

    public MyQuestionDetailListViewAdapter(Context context, ArrayList<MyQuestionDetailBean> datas,
                                           PageManager manager, BJMGFActivity activity) {
        this.mDatas = datas;
        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
        this.mManager = manager;
        this.activity = activity;
        meLayoutId = ReflectResourceId.getLayoutId(context, "bjmgf_sdk_question_detail_dialog_me");
        uLayoutId = ReflectResourceId.getLayoutId(context, "bjmgf_sdk_question_detail_dialog_you");
        iCustomerServicePresenter = new CustomerServicePresenterImpl(context, this);
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int i) {
        return mDatas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        //初始化view中的组件
        //  if (view == null) {
        //根据disposer判断，"disposer":"回复人(0-玩家，1-客服)
        if ("1".equals(mDatas.get(i).getDisposer())) {
            viewHolder = new ViewHolder();
            view = mInflater.inflate(uLayoutId, null);
        } else {
            viewHolder = new ViewHolder();
            view = mInflater.inflate(meLayoutId, null);
        }
        //两个layout中的对应组件名一致
        viewHolder.timeTextView = (TextView) view.findViewById(ReflectResourceId.getViewId(
                mContext, "bjmgf_sdk_respond_time"));
        viewHolder.contentTextView = (TextView) view.findViewById(ReflectResourceId.getViewId(
                mContext, "bjmgf_sdk_respond"));
        viewHolder.attachFileTextView = (TextView) view.findViewById(ReflectResourceId.getViewId(
                mContext, "bjmgf_sdk_watch_file"));
        view.setTag(viewHolder);
        // } else {
        //   viewHolder = (ViewHolder) view.getTag();
        //  }
        if (mDatas.get(i) != null) {
            //为view中的各个组件设置数据
            viewHolder.timeTextView.setText(mDatas.get(i).getDisposetime());
            viewHolder.contentTextView.setText(mDatas.get(i).getContent());
            //查看附件
            if ("".equals(mDatas.get(i).getAttachment())) {
                viewHolder.attachFileTextView.setVisibility(View.GONE);
            } else {
                viewHolder.attachFileTextView.setVisibility(View.VISIBLE);
                viewHolder.attachFileTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //点击查看图片
                        LogProxy.d(TAG, "attachment file url=" + mDatas.get(i).getAttachment());
                        DialogUtil.createTransparentProgressDialog(mContext,
                                mContext.getString(ReflectResourceId.getStringId(mContext,
                                        Resource.string.bjmgf_sdk_dataSubmitingStr)));
                        iCustomerServicePresenter.getPicWithURL(mContext, mDatas.get(i).getAttachment());
                    }
                });
            }
        }

        return view;
    }

    /**
     * 下载图片后的回调
     *
     * @param file 下载的图片文件
     */
    @Override
    public void showPicture(File file) {
        Log.d("pepelu", TAG + "\tshowPicture::" + (file == null ? true : file.getPath()));
        DialogUtil.dismissProgressDialog();
        if (file == null || !Utility.checkFilePath(file.getPath())) {
            //图片正在审核，请稍后查看
            ToastUtil.showMessage(mContext, mContext.getString(ReflectResourceId.getStringId(mContext,
                    Resource.string.bjmgf_sdk_question_image_load_failed)));
            return;
        }
        ImageViewerView imageViewerView = new ImageViewerView(mContext, mManager, activity, file);
        mManager.addPage(imageViewerView);
    }

    /**
     * 图片未获取到产生的错误：图片正在审核
     *
     * @param message   错误信息
     * @param errorCode 返回码：请求码
     */
    @Override
    public void showPictureNotAvailableError(String message, int errorCode) {
        DialogUtil.dismissProgressDialog();
        //图片正在审核，请稍后查看
        ToastUtil.showMessage(mContext, mContext.getString(ReflectResourceId.getStringId(mContext,
                Resource.string.bjmgf_sdk_question_image_load_failed)));
    }

    @Override
    public void showError(String message) {
        Log.d("pepelu", TAG + "\tshowPicture::error");
        DialogUtil.dismissProgressDialog();
        ToastUtil.showMessage(mContext, message);
    }

    @Override
    public void showSuccess() {
        DialogUtil.dismissProgressDialog();
    }

    class ViewHolder {
        TextView timeTextView;
        TextView contentTextView;
        TextView attachFileTextView;
    }
}
