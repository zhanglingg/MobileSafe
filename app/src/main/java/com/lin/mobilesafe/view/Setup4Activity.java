package com.lin.mobilesafe.view;

import android.os.Bundle;

import com.lin.mobilesafe.R;

public class Setup4Activity extends BaseSetupActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup4);
    }

    @Override
    protected void nextActivity() {
        startActivity(LostFindActivity.class);
    }

    @Override
    protected void prevActivity() {
        startActivity(Setup3Activity.class);
    }
}
