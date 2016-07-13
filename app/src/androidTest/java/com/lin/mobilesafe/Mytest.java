package com.lin.mobilesafe;

import android.test.AndroidTestCase;
import android.util.Log;

import com.lin.mobilesafe.dao.BlackNameDao;
import com.lin.mobilesafe.db.DBManger;
import com.lin.mobilesafe.domain.BlackNameBean;
import com.lin.mobilesafe.engine.ReadContactsEngine;
import com.lin.mobilesafe.utils.ServiceUtils;

/**
 * Created by Administrator on 2016/7/7 0007.
 */
public class Mytest extends AndroidTestCase {

    public void testReadContants() {
        ReadContactsEngine.readContacts(getContext()); // 获取虚拟的上下文
    }


    public void testRunningService() {
        ServiceUtils.isServiceRunning(getContext(), "");
    }

    public void testdbManger() {

        BlackNameDao blackNameDao = new BlackNameDao(getContext());
        BlackNameBean blackNameBean2;

//        for (int i = 101; i <= 200; i++) {
//            blackNameBean2 = new BlackNameBean();
//            blackNameBean2.setPhoneNum("15308197" + i);
//            blackNameBean2.setMode("1");
//
//            blackNameDao.insertDB(blackNameBean2);
//        }

        for (BlackNameBean blackNameBean1 : blackNameDao.queryDBByLimit(0)) {
            Log.e("tostring:", blackNameBean1.toString());
        }

        //blackNameDao.close();
    }

}
