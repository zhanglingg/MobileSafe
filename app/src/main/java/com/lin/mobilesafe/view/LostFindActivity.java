package com.lin.mobilesafe.view;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.lin.mobilesafe.R;
import com.lin.mobilesafe.receiver.DeviceAdminSample;
import com.lin.mobilesafe.service.LostFindService;
import com.lin.mobilesafe.utils.MyConstants;
import com.lin.mobilesafe.utils.ServiceUtils;
import com.lin.mobilesafe.utils.SimpleCiphertext;
import com.lin.mobilesafe.utils.SpTools;

public class LostFindActivity extends AppCompatActivity {


    private TextView tv_safenum;
    private ImageView iv_isopen;
    private TextView tv_reset;
    DevicePolicyManager mDPM;
    private PopupWindow pw;
    private View viewContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (SpTools.getBoolean(getApplicationContext(), MyConstants.ISSETTING, false)) {
            // 设置过
            initView();
            initPopWindowView();
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

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tv_safenum = (TextView) findViewById(R.id.tv_lostfind_safenumber);
        iv_isopen = (ImageView) findViewById(R.id.iv_lostfind_isopen);
        tv_reset = (TextView) findViewById(R.id.tv_lostfind_reset);
    }


    private void initData() {

        String safeNumber = SpTools.getString(getApplicationContext(), MyConstants.SAFE_NUMBER, "--");
        safeNumber = SimpleCiphertext.encryptOrdecrypt(MyConstants.SEED, safeNumber);

        byte[] bytes = safeNumber.getBytes();
        for (int i = 3; i < 7; i++) {
            bytes[i] = '*';
        }
        safeNumber = new String(bytes);

        tv_safenum.setText(safeNumber);

        if (ServiceUtils.isServiceRunning(getApplicationContext(), "com.lin.mobilesafe.service.LostFindService")) {
            // 服务在运行
            iv_isopen.setImageResource(R.mipmap.on_button);
        } else {
            iv_isopen.setImageResource(R.mipmap.off_button);
        }
        pw.setFocusable(true);
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

        viewContent.findViewById(R.id.pop_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pw.dismiss();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings1) {
            Snackbar.make(tv_safenum, "action_settings1", Snackbar.LENGTH_LONG)
                    .setAction("Undo", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.e("zl", "action_settings1确定");
                        }
                    })
                    .setDuration(3000)
                    .show();

            int[] location = new int[2];

            pw.showAtLocation(tv_safenum, Gravity.CENTER,
                    location[0], location[1]);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initPopWindowView() {
        viewContent = View.inflate(getApplicationContext(), R.layout.layout_popwindow, null);
        pw = new PopupWindow(viewContent, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected void onDestroy() {
        pw.dismiss();
        pw = null;
        super.onDestroy();
    }
}
