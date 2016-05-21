package com.bojoy.bjsdk_mainland_new.ui.view.cs.impl;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bojoy.bjsdk_mainland_new.ui.activity.base.BJMGFActivity;
import com.bojoy.bjsdk_mainland_new.ui.page.PageManager;
import com.bojoy.bjsdk_mainland_new.ui.page.base.BaseActivityPage;
import com.bojoy.bjsdk_mainland_new.ui.view.cs.ICustomerView;
import com.bojoy.bjsdk_mainland_new.utils.ReflectResourceId;
import com.bojoy.bjsdk_mainland_new.utils.Resource;

import java.io.File;

/**
 * Created by shenliuyong on 2016/2/3.
 * 单纯的图片展示，只做图片是否存在的检查，不做图片不存在的后续处理逻辑
 * 展示图片页,一般情况下都是直接调用系统的图片展示器展示图片，但有的图片要下载在APP的私有目录，不能调用系统的图片展示器展示图片
 */
public class ImageViewerView extends BaseActivityPage implements ICustomerView {

    private final String TAG = ImageViewerView.class.getSimpleName();
    private Context context;
    private PageManager manager;
    private BJMGFActivity activity;
    private View view;
    private ImageView imageView;
    private TextView textView;
    private File file;
    private Bitmap bitmap;

    /**
     * @param context 上下文
     * @param manager 页面管理器
     * @param file    要显示的Image图片
     *******************/
    public ImageViewerView(Context context, PageManager manager, BJMGFActivity activity, File file) {
        super(ReflectResourceId.getLayoutId(context, Resource.layout.bjmgf_sdk_image_viewer_page),
                context, manager, activity);
        this.context = context;
        this.manager = manager;
        this.activity = activity;
        this.file = file;
    }

    @Override
    public void onCreateView(View view) {
        super.onCreateView(view);
        this.view = view;
        imageView = (ImageView) view.findViewById(ReflectResourceId.getViewId(context,
                Resource.id.bjmgf_sdk_image_viewer_id));
        textView = (TextView) view.findViewById(ReflectResourceId.getViewId(context,
                Resource.id.bjmgf_sdk_image_viewer_failed_hint_id));
    }

    @Override
    public void onResume() {
        super.onResume();
        updateView();
    }

    private void updateView() {
        if (file != null && file.exists()) {
            bitmap = BitmapFactory.decodeFile(file.getPath());
            imageView.setImageBitmap(bitmap);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //回收bitmap资源
        if (bitmap != null) {
            bitmap.recycle();
            bitmap = null;
        }
    }

    @Override
    public void setView() {

    }

    @Override
    public void showError(String message) {

    }

    @Override
    public void showSuccess() {

    }
}
