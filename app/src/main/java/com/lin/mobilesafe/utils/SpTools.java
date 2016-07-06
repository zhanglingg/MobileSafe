package com.lin.mobilesafe.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2016/7/6 0006.
 */
public class SpTools {
    public static void putString(Context context, String key, String value) {
        SharedPreferences sp = context.getSharedPreferences(MyConstants.SPFILE, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getString(Context context, String key, String defaultValue) {
        SharedPreferences sp = context.getSharedPreferences(MyConstants.SPFILE, Context.MODE_PRIVATE);
        return sp.getString(key, defaultValue);
    }

    public static void putBoolean(Context context, String key, boolean value) {
        SharedPreferences sp = context.getSharedPreferences(MyConstants.SPFILE, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static boolean getBoolean(Context context, String key, boolean defultValue) {
        SharedPreferences sp = context.getSharedPreferences(MyConstants.SPFILE, Context.MODE_PRIVATE);
        return sp.getBoolean(key, defultValue);
    }


}
