package com.bojoy.bjsdk_mainland_new.ui.activity.base;

import android.app.Activity;
import android.app.Dialog;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import com.bojoy.bjsdk_mainland_new.api.BaseApi;
import com.bojoy.bjsdk_mainland_new.app.tools.BJMGFSDKTools;
import com.bojoy.bjsdk_mainland_new.app.tools.ParamsTools;
import com.bojoy.bjsdk_mainland_new.congfig.SysConstant;
import com.bojoy.bjsdk_mainland_new.utils.DialogUtil;
import com.bojoy.bjsdk_mainland_new.utils.DomainUtility;
import com.bojoy.bjsdk_mainland_new.utils.LogProxy;
import com.bojoy.bjsdk_mainland_new.utils.ReflectResourceId;
import com.bojoy.bjsdk_mainland_new.utils.Resource;
import com.bojoy.bjsdk_mainland_new.utils.StringUtility;
import java.lang.reflect.Field;
import java.util.Map;

/**
 * Created by liwei002 on 2016/2/4.
 */
public class WebViewActivity extends Activity {
    private static final String TAG = WebViewActivity.class.getCanonicalName();
    private RelativeLayout mCloseLinearLayout;
    private WebView mRechargeWebView;
    private Dialog mProgressDialog;
    private boolean loaded = false;
    private String webUrl = null;
    private ParamsTools paramsTools = ParamsTools.getInstance();

private Handler mHandler = new Handler(){
    @Override
    public void handleMessage(Message msg) {
        if (!Thread.currentThread().isInterrupted()) {
            switch (msg.what) {
                case 0:
                    if (mProgressDialog != null) {
                        mProgressDialog.show();
                    }
                    break;
                case 1:
                    // 隐藏进度对话框，不可使用dismiss()、cancel(),否则再次调用show()时，
                    // 显示的对话框小圆圈不会动。
                    // mProgressDialog.dismiss();
                    if (mProgressDialog != null) {
                        mProgressDialog.hide();
                    }
                    break;
                default:
                    break;
            }
        }
        super.handleMessage(msg);
    }
};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //控制充值的横竖屏
        if(BJMGFSDKTools.getInstance().getScreenOrientation()== SysConstant.BJMGF_Screen_Orientation_Landscape){
             setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }else{
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        setContentView(ReflectResourceId.getLayoutId(this, Resource.layout.bjmgf_sdk_recharge_webview));
        initView();
       String url= DomainUtility.getInstance().getServiceSDKDomain(WebViewActivity.this)
                + BaseApi.APP_WAP_RECHARGE;
        LogProxy.d(TAG,url);
        Map<String,String> params= paramsTools.getWapRecharge(WebViewActivity.this);
        String webRechargeUrl=StringUtility.getAppendUrlAndParams(url,params);
        webUrl=webRechargeUrl;
        LogProxy.d(TAG,webUrl);

    }

    /**
     * 初始化View
     */
    public void initView() {
        mCloseLinearLayout=(RelativeLayout) findViewById(ReflectResourceId.getViewId(this,Resource.id.bjmgf_sdk_closeLlId));
        mRechargeWebView=(WebView)findViewById(ReflectResourceId.getViewId(this,Resource.id.bjmgf_sdk_rechargeWebViewId));
        //能够执行Javascript脚本
        mRechargeWebView.getSettings().setJavaScriptEnabled(true);
        //支持缩放
        mRechargeWebView.getSettings().setSupportZoom(true);
        ////设置此属性，可任意比例缩放
        mRechargeWebView.getSettings().setUseWideViewPort(true);
        //支持缩放
        mRechargeWebView.getSettings().setBuiltInZoomControls(true);
        // 缩放至屏幕的大小
        mRechargeWebView.getSettings().setLoadWithOverviewMode(true);
        //将图片调整到适合webview的大小
        mRechargeWebView.getSettings().setUseWideViewPort(true);
        //mRechargeWebView.setScrollBarStyle(0);// 滚动条风格为0指滚动条不占用空间，直接覆盖在网页上
        mRechargeWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view,
                                                    String url) {
               loadUrl(view, url);
        //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                return true;
            }


        });
      mRechargeWebView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                mRechargeWebView.requestFocus();
                if (newProgress == 100) {
                    loaded = true;
                    mHandler.sendEmptyMessage(1);

                }
                super.onProgressChanged(view, newProgress);
            }

        });
     mRechargeWebView.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View view, boolean hasFocus) {

                if (hasFocus) {
                    try {
                      Field defaultScale = WebView.class
                                .getDeclaredField("mDefaultScale");
                        defaultScale.setAccessible(true);
                        // WebViewSettingUtil.getInitScaleValue(VideoNavigationActivity.this,
                        // false )/100.0f 是我的程序的一个方法，可以用float 的scale替代
                        defaultScale.setFloat(mRechargeWebView, 0.5f);
                    } catch (SecurityException e) {
                        e.printStackTrace();
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
     mCloseLinearLayout.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {

        WebViewActivity.this.finish();
    }
});

    }

    @Override
    protected void onResume() {
        super.onResume();
        mProgressDialog= DialogUtil.createTransparentProgressDialog(this,
                getResources().getString(
                        ReflectResourceId.getStringId(this,
                                Resource.string.bjmgf_sdk_http_laoding)));
        if(!loaded&& !StringUtility.isEmpty(webUrl)){

         loadUrl(mRechargeWebView, webUrl);

         }
    }

    @Override
    protected void onPause() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
        mProgressDialog = null;
        super.onPause();
    }

    /**
     * 加载Url
     * @param view WebView控件
     * @param url  传入网址
     */
    public void loadUrl(final WebView view, final String url){
        webUrl=url;
        loaded=false;
        view.loadUrl(url);
        mHandler.sendEmptyMessage(0);
    }
}
