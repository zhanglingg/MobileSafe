package com.lin.mobilesafe.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.lin.mobilesafe.R;
import com.lin.mobilesafe.utils.MyConstants;
import com.lin.mobilesafe.utils.SpTools;

public class LostFindActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(SpTools.getBoolean(getApplicationContext(), MyConstants.ISSETTING, false)) {
            // 设置过
            initView();
            initData();
        } else {
            // 没有设置过，进入设置引导页面
            Intent intent = new Intent(LostFindActivity.this, Setup1Activity.class);
            startActivity(intent);
            finish();
        }

        

    }

    private void initData() {
    }

    private void initView() {
        setContentView(R.layout.activity_lost_find);
    }
}
