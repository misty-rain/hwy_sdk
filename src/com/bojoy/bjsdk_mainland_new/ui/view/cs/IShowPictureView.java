package com.bojoy.bjsdk_mainland_new.ui.view.cs;

import com.bojoy.bjsdk_mainland_new.ui.view.IBaseView;

import java.io.File;

/**
 * Created by shenliuyong on 2016/2/3.
 */
public interface IShowPictureView extends IBaseView {

    /**
     * 下载图片后的回调
     *
     * @param file 下载的图片文件
     */
    void showPicture(File file);

    /**
     * 图片未获取到产生的错误：图片正在审核
     * @param message 错误信息
     * @param errorCode 返回码：请求码
     */
    void showPictureNotAvailableError(String message,int errorCode);
}
