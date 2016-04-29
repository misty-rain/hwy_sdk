package com.bojoy.bjsdk_mainland_new.support.http.builder;

import com.bojoy.bjsdk_mainland_new.support.http.request.OKHttpPostStringRequest;
import com.bojoy.bjsdk_mainland_new.support.http.request.RequestCall;
import com.bojoy.bjsdk_mainland_new.support.http.utils.Utils;
import com.bojoy.bjsdk_mainland_new.utils.LogProxy;

import java.util.LinkedHashMap;
import java.util.Map;

import okhttp3.MediaType;

/**
 * Created by wutao on 2016/2/1.
 * post 请求组装器,通过content 发送字符串
 */
public class OKHttpPostStringBuilder extends OKHttpRequestBuilder {
    private final String TAG = OKHttpPostStringBuilder.class.getSimpleName();
    private String content;
    private MediaType mediaType;


    public OKHttpPostStringBuilder content(String content) {
        this.content = content;
        return this;
    }

    public OKHttpPostStringBuilder mediaType(MediaType mediaType) {
        this.mediaType = mediaType;
        return this;
    }


    @Override
    public RequestCall build() {
        LogProxy.d(TAG, Utils.parseParamsStr(url,params));
        return new OKHttpPostStringRequest(url, tag, params, headers, content, mediaType).build();
    }

    @Override
    public OKHttpPostStringBuilder url(String url) {
        this.url = url;
        return this;
    }

    @Override
    public OKHttpPostStringBuilder tag(Object tag) {
        this.tag = tag;
        return this;
    }

    @Override
    public OKHttpPostStringBuilder params(Map<String, String> params) {
        this.params = params;
        return this;
    }

    @Override
    public OKHttpPostStringBuilder addParams(String key, String val) {
        if (this.params == null) {
            params = new LinkedHashMap<>();
        }
        params.put(key, val);
        return this;
    }

    @Override
    public OKHttpPostStringBuilder headers(Map<String, String> headers) {
        this.headers = headers;
        return this;
    }

    @Override
    public OKHttpPostStringBuilder addHeader(String key, String val) {
        if (this.headers == null) {
            headers = new LinkedHashMap<>();
        }
        headers.put(key, val);
        return this;
    }
}
