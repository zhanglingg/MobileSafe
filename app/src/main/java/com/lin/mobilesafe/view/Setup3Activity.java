package com.lin.mobilesafe.view;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.lin.mobilesafe.R;
import com.lin.mobilesafe.utils.MyConstants;
import com.lin.mobilesafe.utils.SpTools;

public class Setup3Activity extends BaseSetupActivity {

    private EditText et_safeNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    @Override
    protected void nextActivity() {
        startActivity(Setup4Activity.class);
    }

    @Override
    protected void prevActivity() {
        startActivity(Setup2Activity.class);
    }


    @Override
    public void initView() {
        setContentView(R.layout.activity_setup3);

        et_safeNumber = (EditText) findViewById(R.id.et_setup3_safenumber);
        super.initView();
    }

    @Override
    public void initData() {
        et_safeNumber.setText(SpTools.getString(getApplicationContext(), MyConstants.SAFE_NUMBER, ""));
        super.initData();
    }

    @Override
    public void next(View view) {
        // 保存安全号码
        // 不存在安全号码，不进行下一步

        String safe_Number = et_safeNumber.getText().toString().trim();
        if(TextUtils.isEmpty(safe_Number)){
            return;
        }

        SpTools.putString(getApplicationContext(), MyConstants.SAFE_NUMBER, safe_Number);

        super.next(view);
    }

    public void selectSafeNumber(View view) {


    }
}
