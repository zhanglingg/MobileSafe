package com.lin.mobilesafe.service;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsManager;
import android.util.Log;

import com.lin.mobilesafe.utils.MyConstants;
import com.lin.mobilesafe.utils.SimpleCiphertext;
import com.lin.mobilesafe.utils.SpTools;

import java.util.List;

/**
 * Created by Administrator on 2016/7/8 0008.
 */
public class LocationService extends Service {

    private LocationManager lm;
    private LocationListener listener;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        //获取定位管理器

        lm = (LocationManager) getSystemService(LOCATION_SERVICE);

        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                float accuracy = location.getAccuracy(); // 获取精确度
                double altitude = location.getAltitude(); // 海拔
                double longitude = location.getLongitude(); // 经度
                double latitude = location.getLatitude(); // 纬度
                float speed = location.getSpeed(); // 速度

                StringBuilder locationInfo = new StringBuilder();

                locationInfo.append("精确度:" + accuracy + "\r\n");
                locationInfo.append("海拔:" + altitude + "\r\n");
                locationInfo.append("经度:" + longitude + "\r\n");
                locationInfo.append("纬度:" + latitude + "\r\n");
                locationInfo.append("速度:" + speed + "\r\n");

                // 发送短信
                String safeNumber = SpTools.getString(getApplicationContext(), MyConstants.SAFE_NUMBER, "");
                safeNumber = SimpleCiphertext.encryptOrdecrypt(MyConstants.SEED,safeNumber);

                Log.e("safeNumber", safeNumber);

                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(safeNumber, "", locationInfo.toString(), null, null);

                // 关闭GPS
                stopSelf();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        List<String> allProviders = lm.getAllProviders();
        for (String provider : allProviders) {
            Log.e("provider", provider);
        }


        // 动态获取最佳定位方式
        Criteria criteria = new Criteria();
        criteria.setCostAllowed(true);
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        String bestProvider = lm.getBestProvider(criteria, true);

        lm.requestLocationUpdates(bestProvider, 0, 0, listener);
        super.onCreate();
    }

    @Override
    public void onDestroy() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        lm.removeUpdates(listener);

        lm = null;

        super.onDestroy();
    }
}
