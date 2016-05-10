package com.example.test;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.bojoy.bjsdk_mainland_new.R;
import com.bojoy.bjsdk_mainland_new.app.BJMGFSdk;
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
                /** 閹垫挸绱慦ifi闁板秶鐤嗛悾宀勬桨 */
                case BJMGFSdkEvent.App_Need_Wifi:
                    break;
                /** 闂堢偠浠堢純鎴濈安閻€劍鍨ㄥ〒鍛婂灆閻ㄥ嫬鍨垫慨瀣鐎瑰本鍨氶柅姘辩叀 */
                case BJMGFSdkEvent.App_Init_Offline:
                    break;
                /** 閺囧瓨鏌夾pp閻ㄥ嫬鍙ч梻顓拷姘辩叀 */
                case BJMGFSdkEvent.App_Closed:
                    break;
                /** 閻ц缍嶉幋鎰 */
                case BJMGFSdkEvent.App_Login_Success:
                    break;
                /** 閻ц缍嶆径杈Е */
                case BJMGFSdkEvent.App_Login_Fail:
                    break;
                /** 濞夈劑鏀㈤幋鎰 */
                case BJMGFSdkEvent.App_Logout:
                    break;
                /** 闁拷閸戠尨绱欏锟介崣鎴ｏ拷鍛存付鐟曚礁婀潻娆撳櫡鐎圭偟骞囬柅锟介崙鍝勭安閻€劍鍨ㄩ懓鍛埗閹村繒娈戦柅锟介崙鐚寸礆 */
                case BJMGFSdkEvent.App_Exit:
                    android.os.Process.killProcess(android.os.Process.myPid());
                    System.exit(0);
                    break;
                /** 閸掑洦宕茬拹锕?褰? */
                case BJMGFSdkEvent.App_Switch_Account:
                    break;
                case BJMGFSdkEvent.App_Before_Send_Question:
                    break;
                /** 濞夈劌鍞介幋鎰 */
                case BJMGFSdkEvent.App_Register_Success:
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
                  orientation, listener, userInner, 0, SysConstant.SDK_DOCK_SNS_WISH_POLLMSG_TYPE,
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
