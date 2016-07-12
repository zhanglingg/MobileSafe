package com.lin.mobilesafe.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.lin.mobilesafe.R;
import com.lin.mobilesafe.utils.MyConstants;
import com.lin.mobilesafe.utils.SpTools;

public class SettingCenterActivity extends AppCompatActivity {

    private SettingItemView sivAutoUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_center);

        initView();

        initData();

        initEvent();

    }

    private void initData() {
        boolean isAutoUpdate = SpTools.getBoolean(getApplicationContext(), MyConstants.AUTOUPDATE, false);
        sivAutoUpdate.setChecked(isAutoUpdate);
    }

    private void initView() {
        sivAutoUpdate = (SettingItemView) findViewById(R.id.siv_auto_update);
    }

    private void initEvent() {
        sivAutoUpdate.setRootOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                sivAutoUpdate.toggle();
                SpTools.putBoolean(getApplicationContext(), MyConstants.AUTOUPDATE, sivAutoUpdate.isChecked());
            }
        });
    }


}
