package com.lin.mobilesafe.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.lin.mobilesafe.utils.MyConstants;

/**
 * Created by Administrator on 2016/7/13 0013.
 */
public class DataBaseHelper extends SQLiteOpenHelper {

    // 数据库名称
    private static final String MDBNAME = "black_name.db";

    // 数据库版本
    private int mDBVersion;

    public DataBaseHelper(Context context, int version) {
        super(context, MDBNAME, null, version);
        mDBVersion = version;
    }


    /**
     * 创建数据库时调用
     *
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        createDataBaseForm(db);
    }

    private void createDataBaseForm(SQLiteDatabase db) {
        String sql = "create table " + MyConstants.BLACKNAME + "(" + MyConstants.AUTOID + " integer primary key autoincrement, "
                + MyConstants.BLACKPHONENUM + " varchar(20) not null, "
                + MyConstants.BLACKMODE + " varchar(10) not null, "
                + MyConstants.REMARK + " varchar(20));";

        db.execSQL(sql);

        Log.e("创建数据库", "创建数据库成功");
    }

    /**
     * mDBVersion：版本号更新时调用
     *
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


    }
}
