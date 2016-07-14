package com.lin.mobilesafe.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.lin.mobilesafe.db.DBManger;
import com.lin.mobilesafe.domain.BlackNameBean;
import com.lin.mobilesafe.utils.MyConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/7/13 0013.
 */
public class BlackNameDao {

    private Context context;
    private SQLiteDatabase dateBase;
    private DBManger dbManger;

    public BlackNameDao(Context context) {
        this.context = context;
        dbManger = new DBManger(context);
        dateBase = dbManger.getConnDB();
    }


    private void getConnDB() {
        dateBase = dbManger.getConnDB();
    }


    public void insertDB(BlackNameBean blackNameBean) {
        if (dateBase == null) {
            getConnDB();
        }
        ContentValues cv = new ContentValues();
        cv.put(MyConstants.BLACKPHONENUM, blackNameBean.getPhoneNum());
        cv.put(MyConstants.BLACKMODE, blackNameBean.getMode());

        long result = dateBase.insert(MyConstants.BLACKNAME, null, cv);
        if (result != -1) {
            Log.e("insertDB", "success:" + result);
        } else {
            Log.e("insertDB", "fail:" + result);
        }

    }

    public List<BlackNameBean> queryDBAll() {

        if (dateBase == null) {
            getConnDB();
        }

        List<BlackNameBean> blackNameBeanList = new ArrayList<>();
        BlackNameBean blackNameBean;
        String sql = "select " + MyConstants.BLACKPHONENUM + ", " + MyConstants.BLACKMODE
                + " from " + MyConstants.BLACKNAME;

        Cursor cursor = dateBase.rawQuery(sql, null);

        while (cursor.moveToNext()) {
            blackNameBean = new BlackNameBean();
            blackNameBean.setPhoneNum(cursor.getString(0));
            blackNameBean.setMode(cursor.getString(1));

            blackNameBeanList.add(blackNameBean);
        }
        cursor.close();
        return blackNameBeanList;
    }

    public List<BlackNameBean> queryDBByLimit(int start) {

        if (dateBase == null) {
            getConnDB();
        }

        List<BlackNameBean> blackNameBeanList = new ArrayList<>();
        BlackNameBean blackNameBean;

        String sql = "select " + MyConstants.BLACKPHONENUM + ", " + MyConstants.BLACKMODE
                + " from " + MyConstants.BLACKNAME + " limit "
                + MyConstants.LIMIT + " offset " + start + ";";

        Cursor cursor = dateBase.rawQuery(sql, null);

        while (cursor.moveToNext()) {
            blackNameBean = new BlackNameBean();
            blackNameBean.setPhoneNum(cursor.getString(0));
            blackNameBean.setMode(cursor.getString(1));

            blackNameBeanList.add(blackNameBean);
        }
        cursor.close();
        return blackNameBeanList;
    }


    public void close() {
        dbManger.dataBaseClose();
    }
}
