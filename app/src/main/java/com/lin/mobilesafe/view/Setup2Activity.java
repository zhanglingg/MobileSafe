package com.lin.mobilesafe.view;

import android.content.Context;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.lin.mobilesafe.R;
import com.lin.mobilesafe.utils.MyConstants;
import com.lin.mobilesafe.utils.SpTools;

public class Setup2Activity extends BaseSetupActivity {

    private Button bt_BindSim;
    private ImageView iv_isBind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        initEvent();
        initData();

    }

    @Override
    public void initData() {
        super.initData();
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_setup2);
        bt_BindSim = (Button) findViewById(R.id.bt_bind);
        super.initView();
    }

    @Override
    public void initEvent() {
        bt_BindSim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 绑定和解绑

                if (TextUtils.isEmpty(SpTools.getString(getApplicationContext(), MyConstants.SIM, ""))) {
                    // 没有绑定
                    // 获取sim卡信息
                    TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                    // sim卡信息
                    String simSeriaNumber = tm.getSimSerialNumber();
                    // 保存到sp中
                    SpTools.putString(getApplicationContext(), MyConstants.SIM, simSeriaNumber);
                    Log.e("zl", "绑定成功" + simSeriaNumber);
                } else {
                    // 已经绑定,  解绑（保存一个空值）
                    SpTools.putString(getApplicationContext(), MyConstants.SIM, "");
                    Log.e("zl", "解除绑定");
                }

            }
        });
        super.initEvent();
    }

    @Override
    public void next(View view) {
        if (TextUtils.isEmpty(SpTools.getString(getApplicationContext(), MyConstants.SIM, ""))) {
            Toast.makeText(getApplicationContext(),"请先绑定sim卡",Toast.LENGTH_SHORT).show();
            return;
        }
        super.next(view);
    }

    @Override
    protected void nextActivity() {
        startActivity(Setup3Activity.class);
    }

    @Override
    protected void prevActivity() {
        startActivity(Setup1Activity.class);
    }
}
