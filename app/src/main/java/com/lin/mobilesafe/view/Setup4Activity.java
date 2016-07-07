package com.lin.mobilesafe.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.lin.mobilesafe.R;
import com.lin.mobilesafe.service.LostFindService;
import com.lin.mobilesafe.utils.MyConstants;
import com.lin.mobilesafe.utils.ServiceUtils;
import com.lin.mobilesafe.utils.SpTools;

public class Setup4Activity extends BaseSetupActivity {

    private CheckBox cb_isProtected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_setup4);
        cb_isProtected = (CheckBox) findViewById(R.id.cb_setup4_isprotected);
        super.initView();
    }

    @Override
    public void initData() {

        if (ServiceUtils.isServiceRunning(getApplicationContext(), "com.lin.mobilesafe.service.LostFindService")) {
            // 服务在运行
            cb_isProtected.setChecked(true);
        } else {
            cb_isProtected.setChecked(false);
        }
        super.initData();
    }

    @Override
    public void initEvent() {
        cb_isProtected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Intent service = new Intent(Setup4Activity.this, LostFindService.class);
                if(isChecked) {
                    startService(service);
                } else {
                    stopService(service);
                }
            }
        });

        super.initEvent();
    }

    @Override
    protected void nextActivity() {
        SpTools.putBoolean(getApplicationContext(), MyConstants.ISSETTING,true);
        startActivity(LostFindActivity.class);
    }

    @Override
    protected void prevActivity() {
        startActivity(Setup3Activity.class);
    }
}
