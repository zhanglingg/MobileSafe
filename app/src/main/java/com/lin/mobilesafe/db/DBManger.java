package com.lin.mobilesafe.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.lin.mobilesafe.utils.MyConstants;

/**
 * Created by Administrator on 2016/7/13 0013.
 */
public class DBManger {

    private DataBaseHelper helper;
    private SQLiteDatabase dataBase;
    private Context context;

    public DBManger(Context context) {
        this.context = context;
    }


    public SQLiteDatabase getConnDB() {
        helper = new DataBaseHelper(context, MyConstants.DATABAEVERSION);
        dataBase = helper.getWritableDatabase();

        return dataBase;
    }

    public void dataBaseClose() {
        Log.e("manger", "close");

        if (helper != null) {
            helper.close();
            helper = null;
        }
    }

}
