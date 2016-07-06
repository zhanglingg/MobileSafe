package com.lin.mobilesafe.utils;

import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Administrator on 2016/7/6 0006.
 */
public class MD5Utils {
    public static String md5(String str) {

        try {
            // MD5的加密器
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = str.getBytes();
            byte[] digest = md.digest(bytes);

            StringBuilder stringBuilder = new StringBuilder();

            for (byte b : digest) {

                int temp = 0xff & b;
                String hexString = Integer.toHexString(temp);

                if (hexString.length() == 1) {
                    hexString = "0" + hexString;
                }
                stringBuilder.append(hexString);
            }
            Log.e("MD5", stringBuilder.toString());
            return stringBuilder.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}
