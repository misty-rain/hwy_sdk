package com.bojoy.bjsdk_mainland_new.support.http.builder;


import com.bojoy.bjsdk_mainland_new.support.http.config.FileInput;
import com.bojoy.bjsdk_mainland_new.support.http.request.OKHttpPostRequest;
import com.bojoy.bjsdk_mainland_new.support.http.request.RequestCall;
import com.bojoy.bjsdk_mainland_new.support.http.utils.Utils;
import com.bojoy.bjsdk_mainland_new.utils.LogProxy;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wutao on 2016/2/1.
 * post 表单式请求组装器,支持表单形式发送文件file
 */
public class OKHttpPostBuilder extends OKHttpRequestBuilder {

    private final String TAG = OKHttpPostBuilder.class.getSimpleName();
    private List<FileInput> files = new ArrayList<>();

    @Override
    public RequestCall build() {
        LogProxy.d(TAG, Utils.parseParamsStr(url,params));
        return new OKHttpPostRequest(url, tag, params, headers, files).build();
    }

    public OKHttpPostBuilder addFile(List<FileInput> list) {
       this.files = list;
        return this;
    }


    //
    @Override
    public OKHttpPostBuilder url(String url) {
        this.url = url;
        return this;
    }

    @Override
    public OKHttpPostBuilder tag(Object tag) {
        this.tag = tag;
        return this;
    }

    @Override
    public OKHttpPostBuilder params(Map<String, String> params) {
        this.params = params;
        return this;
    }

    @Override
    public OKHttpPostBuilder addParams(String key, String val) {
        if (this.params == null) {
            params = new LinkedHashMap<>();
        }
        params.put(key, val);
        return this;
    }

    @Override
    public OKHttpPostBuilder headers(Map<String, String> headers) {
        this.headers = headers;
        return this;
    }


    @Override
    public OKHttpPostBuilder addHeader(String key, String val) {
        if (this.headers == null) {
            headers = new LinkedHashMap<>();
        }
        headers.put(key, val);
        return this;
    }

}
