package com.lin.mobilesafe.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.lin.mobilesafe.R;

public class Setup1Activity extends BaseSetupActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup1);

    }

    @Override
    protected void nextActivity() {
        startActivity(Setup2Activity.class);
    }

    @Override
    protected void prevActivity() {

    }


}
