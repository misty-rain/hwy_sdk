package com.bojoy.bjsdk_mainland_new.support.http;

import com.bojoy.bjsdk_mainland_new.support.fastjson.JSON;
import com.bojoy.bjsdk_mainland_new.support.http.callback.BitmapCallback;
import com.bojoy.bjsdk_mainland_new.support.http.callback.Callback;
import com.bojoy.bjsdk_mainland_new.support.http.callback.FileCallback;
import com.bojoy.bjsdk_mainland_new.support.http.config.FileInput;
import com.bojoy.bjsdk_mainland_new.support.http.config.HttpMethod;
import com.bojoy.bjsdk_mainland_new.support.http.utils.OkHttpUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by wutao on 2015/11/26.
 * 网络请求封住类
 * 支持Http  And Https  请求
 * 主要封装了post、get、post表单、文件、图像
 */
public class HttpUtility {


    private static HttpUtility httpUtility;

    private HttpUtility() {
    }

    public static HttpUtility getInstance() {

        if (httpUtility == null) {
            synchronized (HttpUtility.class) {
                if (httpUtility == null) {
                    httpUtility = new HttpUtility();
                }
            }
        }
        return httpUtility;
    }

    /**
     * Get or Post 网络请求 回调方法
     *
     * @param httpMethod
     * @param url
     * @param param
     * @param callback
     * @throws Exception
     */
    public void execute(HttpMethod httpMethod, String url,
                        Map<String, String> param, Callback callback) throws Exception {
        switch (httpMethod) {
            case POST:
                OkHttpUtils.getInstance().post().url(url).params(param).build().execute(callback);
                break;
            case GET:
                OkHttpUtils.getInstance().get().url(url).params(param).build().execute(callback);
                break;
            case POSTSTRING:
                OkHttpUtils.getInstance().postString().url(url).content(JSON.toJSONString(param)).build().execute(callback);
                break;
        }
    }

    /**
     * Post 表单请求 支持文件上传,主要用于 设置了 enctype=multipart/form-data 属性的请求
     *
     * @param url
     * @param param
     * @param callback
     * @param files    文件流
     * @throws Exception
     */
    public void execute(String url,
                        Map<String, String> param, List<FileInput> files, Callback callback) throws Exception {
        Map<String, String> headers = new HashMap<>();
        OkHttpUtils.getInstance().postFrom().addFile(files).url(url).params(param).headers(headers).build().execute(callback);
        OkHttpUtils.getInstance().post().url(url).params(param).headers(headers).build().execute();
    }


    /**
     * 显示图片 ，带回调
     *
     * @param urlAddress
     * @param tag
     * @param callback
     * @throws Exception]]
     */
    public void executeDisplayImage(String urlAddress, Object tag, BitmapCallback callback) throws Exception {
        OkHttpUtils.getInstance().get().url(urlAddress).tag(tag).build().connTimeOut(20000).readTimeOut(20000).writeTimeOut(20000).execute(callback);
    }

    /**
     * 上传 文件 , 带回调
     *
     * @param urlAddress 文件地址
     * @param params
     * @param file
     * @param callback
     * @throws Exception
     */
    public void executeUpLoadFile(String urlAddress, Map<String, String> params, List<FileInput> file, Callback callback) throws Exception {
        OkHttpUtils.getInstance().post().addFile(file).url(urlAddress).params(params).build().execute(callback);
    }

    /**
     * 下载文件
     *
     * @param urlAddress 文件地址
     * @param callback   回调
     */
    public void executeDownloadFile(String urlAddress, FileCallback callback) {
        OkHttpUtils.getInstance().get().url(urlAddress).build().execute(callback);
    }

    /**
     * 同步网络请求
     * @param httpMethod
     * @param url
     * @param param,
     * @param callback
     * @throws Exception
     */
    public void syncRequest(HttpMethod httpMethod, String url,
                            Map<String, String> param, Callback callback) throws Exception {
        switch (httpMethod) {
            case POST:

                OkHttpUtils.getInstance().post().url(url).params(param).build().execute(callback);
                break;
            case GET:
                 OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                          .url("http://publicobject.com/helloworld.txt")
                          .build();

                Response response = client.newCall(request).execute();
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

               // OkHttpUtils.getInstance().get().url(url).params(param).build().execute(callback);
                break;
            case POSTSTRING:
                OkHttpUtils.getInstance().postString().url(url).content(JSON.toJSONString(param)).build().execute(callback);
                break;
        }
    }
}
