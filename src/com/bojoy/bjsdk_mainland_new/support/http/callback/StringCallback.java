package com.bojoy.bjsdk_mainland_new.support.http.callback;

import java.io.IOException;

import okhttp3.Response;

/**
 * Created by wutao on 2016/2/1.
 * 默认回调泛型为string，
 */
public abstract class StringCallback extends Callback<String> {
    @Override
    public String parseNetworkResponse(Response response) throws IOException
    {
        return response.body().string();
    }
}
