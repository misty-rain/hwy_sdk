package com.bojoy.bjsdk_mainland_new.support.http.builder;

import com.bojoy.bjsdk_mainland_new.support.http.request.OKHttpFileRequest;
import com.bojoy.bjsdk_mainland_new.support.http.request.RequestCall;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

import okhttp3.MediaType;

/**
 * Created by wutao on 2016/2/1.
 * 文件请求组装器 ,采用post 方式
 */
public class OKHttpFileBuilder extends OKHttpRequestBuilder {

    private File file;
    private MediaType mediaType;


    public OKHttpRequestBuilder file(File file) {
        this.file = file;
        return this;
    }

    public OKHttpRequestBuilder mediaType(MediaType mediaType) {
        this.mediaType = mediaType;
        return this;
    }


    @Override
    public RequestCall build() {
        return new OKHttpFileRequest(url, tag, params, headers, file, mediaType).build();
    }

    @Override
    public OKHttpFileBuilder url(String url) {
        this.url = url;
        return this;
    }

    @Override
    public OKHttpFileBuilder tag(Object tag) {
        this.tag = tag;
        return this;
    }

    @Override
    public OKHttpFileBuilder params(Map<String, String> params) {
        this.params = params;
        return this;
    }

    @Override
    public OKHttpFileBuilder addParams(String key, String val) {
        if (this.params == null) {
            params = new LinkedHashMap<>();
        }
        params.put(key, val);
        return this;
    }

    @Override
    public OKHttpFileBuilder headers(Map<String, String> headers) {
        this.headers = headers;
        return this;
    }

    @Override
    public OKHttpFileBuilder addHeader(String key, String val) {
        if (this.headers == null) {
            headers = new LinkedHashMap<>();
        }
        headers.put(key, val);
        return this;
    }

}
