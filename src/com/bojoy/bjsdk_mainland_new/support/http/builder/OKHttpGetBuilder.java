package com.bojoy.bjsdk_mainland_new.support.http.builder;

import com.bojoy.bjsdk_mainland_new.support.http.request.OKHttpGetRequest;
import com.bojoy.bjsdk_mainland_new.support.http.request.RequestCall;
import com.bojoy.bjsdk_mainland_new.support.http.utils.Utils;
import com.bojoy.bjsdk_mainland_new.utils.LogProxy;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by wutao on 2016/2/1.
 * Get 请求组装器
 */
public class OKHttpGetBuilder extends OKHttpRequestBuilder {

    private final String TAG = OKHttpGetBuilder.class.getSimpleName();

    @Override
    public RequestCall build() {
        if (params != null) {
            url = appendParams(url, params);
        }
        LogProxy.d(TAG, Utils.parseParamsStr(url,params));
        return new OKHttpGetRequest(url, tag, params, headers).build();
    }

    private String appendParams(String url, Map<String, String> params) {
        StringBuilder sb = new StringBuilder();
        sb.append(url + "?");
        if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
                sb.append(key).append("=").append(params.get(key)).append("&");
            }
        }
        sb = sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    @Override
    public OKHttpGetBuilder url(String url) {
        this.url = url;
        return this;
    }

    @Override
    public OKHttpGetBuilder tag(Object tag) {
        this.tag = tag;
        return this;
    }

    @Override
    public OKHttpGetBuilder params(Map<String, String> params) {
        this.params = params;
        return this;
    }

    @Override
    public OKHttpGetBuilder addParams(String key, String val) {
        if (this.params == null) {
            params = new LinkedHashMap<>();
        }
        params.put(key, val);
        return this;
    }

    @Override
    public OKHttpGetBuilder headers(Map<String, String> headers) {
        this.headers = headers;
        return this;
    }

    @Override
    public OKHttpGetBuilder addHeader(String key, String val) {
        if (this.headers == null) {
            headers = new LinkedHashMap<>();
        }
        headers.put(key, val);
        return this;
    }

}
