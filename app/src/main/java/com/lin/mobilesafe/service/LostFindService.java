package com.lin.mobilesafe.service;

import android.annotation.TargetApi;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.SmsMessage;
import android.util.Log;

public class LostFindService extends Service {

    private SmsReceiver receiver;

    public LostFindService() {
    }

    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public void onCreate() {
        // 注册短信监听广播
        receiver = new SmsReceiver();
        IntentFilter filter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        filter.setPriority(Integer.MAX_VALUE); // 级别一样，清单文件谁先注册谁执行，；如果级别一样，代码优先级要高
        // 注册短信监听
        registerReceiver(receiver, filter);
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        // 取消注册短信监听广播
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    // 短信的广播接收者
    private class SmsReceiver extends BroadcastReceiver {

        @TargetApi(Build.VERSION_CODES.M)
        @Override
        public void onReceive(Context context, Intent intent) {
            // 实现短信拦截的功能
            Bundle extras = intent.getExtras();
            Object datas[] = (Object[]) extras.get("pdus");
            String format = (String) extras.get("format");

            Log.e("format", format);

            for (Object data : datas) {
                SmsMessage sm = SmsMessage.createFromPdu((byte[]) data, format);
                String mess = sm.getMessageBody();

                Log.e("msg", "content:" + mess + "  from：" + sm.getDisplayOriginatingAddress());

                if (mess.equals("#*gps*#")) {
                    // 获取位置信息， 为了准确，把耗时的定位放在服务中
                    Intent service = new Intent(context,LocationService.class);
                    startService(service);
                    abortBroadcast();
                }
            }

        }
    }
}
