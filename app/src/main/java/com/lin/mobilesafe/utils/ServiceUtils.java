package com.lin.mobilesafe.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * Created by Administrator on 2016/7/7 0007.
 */
public class ServiceUtils {

    public static boolean isServiceRunning(Context context, String serviceName) {

        boolean isRunning = false;

        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> runningServices = am.getRunningServices(50);

        Log.e("service size", runningServices.size() + "");

        for (ActivityManager.RunningServiceInfo running : runningServices) {

            Log.e("service", running.service.getClassName());
            if (running.service.getClassName().equals(serviceName)) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }
}
