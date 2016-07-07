package com.lin.mobilesafe.engine;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.lin.mobilesafe.domain.ContactBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/7/7 0007.
 */
public class ReadContactsEngine {

    public static List<ContactBean> readContacts(Context context) {

        List<ContactBean> contactBeanList = new ArrayList<>();
        ContactBean contactBean;

        Uri uri = Uri.parse("content://com.android.contacts/contacts");
        Uri uriDatas = Uri.parse("content://com.android.contacts/data");

        Cursor cursor = context.getContentResolver().query(uri, new String[]{"_id"}, null, null, null);

        while (cursor.moveToNext()) {

            String id = cursor.getString(0);
            Cursor cursor1 = context.getContentResolver().query(uriDatas, new String[]{"data1", "mimetype"},
                    "raw_contact_id = ? ", new String[]{id}, null);

            contactBean = new ContactBean();

            while (cursor1.moveToNext()) {
                String data = cursor1.getString(0);
                String mimeType = cursor1.getString(1);

                if (mimeType.equals("vnd.android.cursor.item/name")) {
                    contactBean.setDisplayName(data);
                }else if(mimeType.equals("vnd.android.cursor.item/phone_v2")){
                    contactBean.setPhoneNum(data);
                }
            }

            Log.e("contactBeanList", contactBean.toString());
            contactBeanList.add(contactBean);

        }
        return contactBeanList;
    }
}
