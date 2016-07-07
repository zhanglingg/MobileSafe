package com.lin.mobilesafe;

import android.test.AndroidTestCase;

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
        ServiceUtils.isServiceRunning(getContext(),"");
    }
}
