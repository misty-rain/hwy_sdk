package com.example.test;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.bojoy.bjsdk_mainland_new.R;
import com.example.test.widget.CircleMenuLayout;

/**
 * Created by wutao on 2016/5/6.
 */
public class NewActivity extends Activity {
    private CircleMenuLayout mCircleMenuLayout;

    private String[] mItemTexts = new String[] { "退出 ", "登陆", "切换账户",
              "充值", "客服" };
    private int[] mItemImgs = new int[] { R.drawable.home_mbank_1_normal,
              R.drawable.home_mbank_1_normal, R.drawable.home_mbank_1_normal,
              R.drawable.home_mbank_1_normal, R.drawable.home_mbank_1_normal};

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_layout);

        mCircleMenuLayout = (CircleMenuLayout) findViewById(R.id.id_menulayout);
        mCircleMenuLayout.setMenuItemIconsAndTexts(mItemImgs, mItemTexts);



        mCircleMenuLayout.setOnMenuItemClickListener(new CircleMenuLayout.OnMenuItemClickListener()
        {

            @Override
            public void itemClick(View view, int pos)
            {

            }

            @Override
            public void itemCenterClick(View view)
            {


            }
        });

    }
}
