package com.example.test;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.bojoy.bjsdk_mainland_new.R;
import com.bojoy.bjsdk_mainland_new.app.BJMGFSdk;
import com.bojoy.bjsdk_mainland_new.congfig.DockTypeConstants;
import com.bojoy.bjsdk_mainland_new.congfig.SysConstant;
import com.bojoy.bjsdk_mainland_new.eventhandler.event.BJMGFSdkEvent;
import com.bojoy.bjsdk_mainland_new.eventhandler.event.BJMGFSdkListener;
import com.bojoy.bjsdk_mainland_new.utils.LogProxy;

public class MyActivity extends Activity implements View.OnClickListener {
    /**
     * Called when the activity is first created.
     */

    private int orientation = SysConstant.BJMGF_Screen_Orientation_Portrait;
    private final String TAG = MyActivity.class.getSimpleName();

    private final int userInner = 1;
    private final int wapRecharge = 0;
    private final String Product_Id = "0";
    private final String Game_Version = "1.3.0";
    private final String Operator = "hwy_android";
    private final String Game_Domain = "";

    private final String App_Id = "50006";
    private final String App_Key = "D^Yt7Jb3uiF64#xjLhy37fY^62s";

    // private final String App_Id = "50009";
    // private final String App_Key = "LCe#OUdgMFU^1hA+sJzy";

    // private final String App_Id = "0";
    //private final String App_Key = "Sdu&@ud732jDJF128i";


    private BJMGFSdk bjmgfSdk = BJMGFSdk.getDefault();

    private Button btnExit, btnlogin, btnLogout, btnPay, btnCustomService;


    private BJMGFSdkListener listener = new BJMGFSdkListener() {

        @Override
        public void onBJMGFEvent(int eventId) {
            LogProxy.i(TAG, "listener event " + eventId);
            switch (eventId) {
                /** 打开Wifi配置界面 */
                case BJMGFSdkEvent.APP_NEED_WIFI:
                    break;
                /** 非联网应用或游戏的初始化完成通知 */
                case BJMGFSdkEvent.APP_INIT_OFFLINE:
                    break;
                /** 更新App的关闭通知 */
                case BJMGFSdkEvent.APP_CLOSED:
                    break;
                /** 登录成功 */
                case BJMGFSdkEvent.APP_LOGIN_SUCCESS:
                    break;
                /** 登录失败 */
                case BJMGFSdkEvent.APP_LOGIN_FAIL:
                    break;
                /** 注销成功 */
                case BJMGFSdkEvent.APP_LOGOUT:
                    break;
                /** 退出（开发者需要在这里实现退出应用或者游戏的退出） */
                case BJMGFSdkEvent.APP_EXIT:
                    android.os.Process.killProcess(android.os.Process.myPid());
                    System.exit(0);
                    break;
                /** 切换账号 */
                case BJMGFSdkEvent.APP_SWITCH_ACCOUNT:
                    break;
                case BJMGFSdkEvent.APP_BEFORE_SEND_QUESTION:
                    break;
                /** 注册成功 */
                case BJMGFSdkEvent.APP_REGISTER_SUCCESS:
                    break;
                case BJMGFSdkEvent.RECHARGE_SUCCESS:
                    Log.i("BJMEngine", "sdk recharge success");
                    break;
                case BJMGFSdkEvent.RECHARGE_FAIL:
                    Log.i("BJMEngine", "sdk recharge fail");
                    break;
                default:
                    break;
            }
        }


    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        initView();
        bjmgfSdk.setDebugMode(true);
        bjmgfSdk.initSdk(MyActivity.this, App_Id, App_Key, "", true,
                  orientation, listener, userInner, 0, DockTypeConstants.SDK_DOCK_SNS_WISH_POLLMSG_TYPE,
                  wapRecharge, "haowanyou", Product_Id, Game_Version, Operator, Game_Domain);
/*        EventBus eventBus=EventBus.getDefault();
        eventBus.register(this);

        Intent in=new Intent(MyActivity.this,Test.class);
        startActivity(in);
        finish();*/


    }

    @Override
    protected void onDestroy() {
        bjmgfSdk.onDestroy(this);
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        bjmgfSdk.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        bjmgfSdk.onPause(this);
    }


    private void initView() {
        btnExit = (Button) findViewById(R.id.btnexit);
        btnExit.setOnClickListener(this);
        btnlogin = (Button) findViewById(R.id.btnlogin);
        btnlogin.setOnClickListener(this);
        btnLogout = (Button) findViewById(R.id.btnlogout);
        btnLogout.setOnClickListener(this);
        btnPay = (Button) findViewById(R.id.btnPay);
        btnPay.setOnClickListener(this);
        btnCustomService = (Button) findViewById(R.id.btnCustomService);
        btnCustomService.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnexit:
                bjmgfSdk.sdkExit(MyActivity.this);
                break;
            case R.id.btnlogin:
                bjmgfSdk.login(MyActivity.this);
                break;
            case R.id.btnlogout:
                bjmgfSdk.logout(MyActivity.this);
                break;
            case R.id.btnPay:
                bjmgfSdk.rechargeProduct(MyActivity.this, String.valueOf(System.currentTimeMillis()), "1008", "20元宝", 1, 7, "088", "0001");
                break;
            case R.id.btnCustomService:
                bjmgfSdk.openCustomService(MyActivity.this);
                break;
        }
    }

}
