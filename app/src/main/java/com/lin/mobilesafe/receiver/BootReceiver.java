package com.lin.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;

import com.lin.mobilesafe.utils.MyConstants;
import com.lin.mobilesafe.utils.SpTools;

/**
 * Created by Administrator on 2016/7/8 0008.
 */
public class BootReceiver extends BroadcastReceiver {

    private String oldSIMInfo;

    @Override
    public void onReceive(Context context, Intent intent) {
        // 手机启动完成，检测SIM是否发生变化

        // 取到保存原SIM卡的信息
        String oldSIMInfo =  SpTools.getString(context, MyConstants.SIM, "");

        // 获取现在的SIM卡信息
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String serialNumber = tm.getSimSerialNumber();


        if (!serialNumber.equals(oldSIMInfo)) {
            // 发送报警短息
            String safeNumber = SpTools.getString(context, MyConstants.SAFE_NUMBER, "");
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(safeNumber, "", "手机被偷", null, null);
        }
    }
}
