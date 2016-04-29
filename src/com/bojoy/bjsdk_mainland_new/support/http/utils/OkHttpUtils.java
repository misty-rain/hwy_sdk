package com.bojoy.bjsdk_mainland_new.support.http.utils;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import com.bojoy.bjsdk_mainland_new.support.http.builder.OKHttpFileBuilder;
import com.bojoy.bjsdk_mainland_new.support.http.builder.OKHttpGetBuilder;
import com.bojoy.bjsdk_mainland_new.support.http.builder.OKHttpPostFormBuilder;
import com.bojoy.bjsdk_mainland_new.support.http.builder.OKHttpPostStringBuilder;
import com.bojoy.bjsdk_mainland_new.support.http.builder.OKHttpPostBuilder;
import com.bojoy.bjsdk_mainland_new.support.http.callback.Callback;
import com.bojoy.bjsdk_mainland_new.support.http.cookie.BaseCookieJar;
import com.bojoy.bjsdk_mainland_new.support.http.https.HttpsUtils;
import com.bojoy.bjsdk_mainland_new.support.http.request.RequestCall;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Response;

/**
 * Created by wutao on 2016/2/1.
 */
public class OkHttpUtils {
    public static final String TAG = OkHttpUtils.class.getSimpleName();
    public static final long DEFAULT_MILLISECONDS = 10000;
    private static OkHttpUtils mInstance;
    private OkHttpClient mOkHttpClient;
    private Handler mDelivery;

    private OkHttpUtils() {
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
        //cookie enabled
        okHttpClientBuilder.cookieJar(new BaseCookieJar());
        mDelivery = new Handler(Looper.getMainLooper());


        if (true) {
            okHttpClientBuilder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
        }

        mOkHttpClient = okHttpClientBuilder.build();
    }

    private boolean debug;
    private String tag;

    public OkHttpUtils debug(String tag) {
        debug = true;
        this.tag = tag;
        return this;
    }


    public static OkHttpUtils getInstance() {
        if (mInstance == null) {
            synchronized (OkHttpUtils.class) {
                if (mInstance == null) {
                    mInstance = new OkHttpUtils();
                }
            }
        }
        return mInstance;
    }

    public Handler getDelivery() {
        return mDelivery;
    }

    public OkHttpClient getOkHttpClient() {

        return mOkHttpClient;
    }
    /**
     * get请求
     * @return
     */
    public OKHttpGetBuilder get() {

        return new OKHttpGetBuilder();
    }
    /**
     * poststring 请求
     * @return
     */
    public OKHttpPostStringBuilder postString() {
        return new OKHttpPostStringBuilder();
    }
    /**
     * file 文件操作请求
     * @return
     */
    public OKHttpFileBuilder file() {

        return new OKHttpFileBuilder();
    }
    /**
     * post请求
     * @return
     */
    public OKHttpPostBuilder post() {
        return new OKHttpPostBuilder();
    }
    /**
     * postfrom表单提交方式，主要区别在于 enctype=multipart/form-data
     * @return
     */
    public OKHttpPostFormBuilder postFrom(){
        return new OKHttpPostFormBuilder();
    }


    public void execute(final RequestCall requestCall, Callback callback) {
        if (debug) {
            if (TextUtils.isEmpty(tag)) {
                tag = TAG;
            }
            Log.d(tag, "{method:" + requestCall.getRequest().method() + ", detail:" + requestCall.getOkHttpRequest().toString() + "}");
        }

        if (callback == null)
            callback = Callback.CALLBACK_DEFAULT;
        final Callback finalCallback = callback;

        requestCall.getCall().enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                sendFailResultCallback(call, e, finalCallback);
            }

            @Override
            public void onResponse(final Call call, final Response response) {
                if (response.code() >= 400 && response.code() <= 599) {
                    try {
                        sendFailResultCallback(call, new RuntimeException(response.body().string()), finalCallback);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return;
                }

                try {
                    Object o = finalCallback.parseNetworkResponse(response);
                    sendSuccessResultCallback(o, finalCallback);
                } catch (Exception e) {
                    sendFailResultCallback(call, e, finalCallback);
                }

            }
        });
    }


    public void sendFailResultCallback(final Call call, final Exception e, final Callback callback) {
        if (callback == null) return;

        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                callback.onError(call, e);
                callback.onAfter();
            }
        });
    }

    public void sendSuccessResultCallback(final Object object, final Callback callback) {
        if (callback == null) return;
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                callback.onResponse(object);
                callback.onAfter();
            }
        });
    }

    public void cancelTag(Object tag) {
        for (Call call : mOkHttpClient.dispatcher().queuedCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
        for (Call call : mOkHttpClient.dispatcher().runningCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
    }


    public void setCertificates(InputStream... certificates) {
        mOkHttpClient = getOkHttpClient().newBuilder()
                  .sslSocketFactory(HttpsUtils.getSslSocketFactory(certificates, null, null))
                  .build();
    }


    public void setConnectTimeout(int timeout, TimeUnit units) {
        mOkHttpClient = getOkHttpClient().newBuilder()
                  .connectTimeout(timeout, units)
                  .build();
    }
}
