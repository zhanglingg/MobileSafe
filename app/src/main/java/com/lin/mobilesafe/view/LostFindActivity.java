package com.lin.mobilesafe.view;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lin.mobilesafe.R;
import com.lin.mobilesafe.receiver.DeviceAdminSample;
import com.lin.mobilesafe.service.LostFindService;
import com.lin.mobilesafe.utils.MyConstants;
import com.lin.mobilesafe.utils.ServiceUtils;
import com.lin.mobilesafe.utils.SpTools;

public class LostFindActivity extends AppCompatActivity {

    private TextView tv_safenum;
    private ImageView iv_isopen;
    private TextView tv_reset;
    DevicePolicyManager mDPM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (SpTools.getBoolean(getApplicationContext(), MyConstants.ISSETTING, false)) {
            // 设置过
            initView();
            initData();
            initEvent();

        } else {
            // 没有设置过，进入设置引导页面
            Intent intent = new Intent(LostFindActivity.this, Setup1Activity.class);
            startActivity(intent);
            finish();
        }

        mDPM = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);

    }


    private void initView() {
        setContentView(R.layout.activity_lost_find);

        tv_safenum = (TextView) findViewById(R.id.tv_lostfind_safenumber);
        iv_isopen = (ImageView) findViewById(R.id.iv_lostfind_isopen);
        tv_reset = (TextView) findViewById(R.id.tv_lostfind_reset);
    }


    private void initData() {

        tv_safenum.setText(SpTools.getString(getApplicationContext(), MyConstants.SAFE_NUMBER, "--"));

        if (ServiceUtils.isServiceRunning(getApplicationContext(), "com.lin.mobilesafe.service.LostFindService")) {
            // 服务在运行
            iv_isopen.setImageResource(R.mipmap.on_button);
        } else {
            iv_isopen.setImageResource(R.mipmap.off_button);
        }
    }

    private void initEvent() {

        iv_isopen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent service = new Intent(LostFindActivity.this, LostFindService.class);
                if (ServiceUtils.isServiceRunning(getApplicationContext(), "com.lin.mobilesafe.service.LostFindService")) {
                    // 服务在运行,就关闭服务
                    stopService(service);
                    iv_isopen.setImageResource(R.mipmap.off_button);
                } else {
                    startService(service);
                    iv_isopen.setImageResource(R.mipmap.on_button);
                }
            }
        });

        tv_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LostFindActivity.this, Setup1Activity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void lockScreen(View view) {
        ComponentName who = new ComponentName(this, DeviceAdminSample.class);
        if (mDPM.isAdminActive(who)) {

            mDPM.lockNow();
        } else {
            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, who);
            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "设备管理器");
            startActivityForResult(intent, 1);
        }

        // 清除数据
        // 清除sd卡数据
//        mDPM.wipeData(DevicePolicyManager.WIPE_EXTERNAL_STORAGE);
//        // 恢复出厂设置
//        mDPM.wipeData(DevicePolicyManager.WIPE_RESET_PROTECTION_DATA);


        // 播放音乐
//        MediaPlayer mp = MediaPlayer.create(getApplicationContext(), null);
//        AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
//        public void adjustStreamVolume (int streamType, int direction, int flags)
//        am.adjustStreamVolume (AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
//        mp.setVolume(1, 1);
//        mp.start();
    }

}
