package com.lin.mobilesafe.view;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.lin.mobilesafe.R;
import com.lin.mobilesafe.domain.ContactBean;
import com.lin.mobilesafe.engine.ReadContactsEngine;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/7/7 0007.
 */
public class MyFriendActivity extends ListActivity {

    private static final int LOADING = 0;
    private static final int FINISH = 1;
    private ListView lv_contact;
    private MyAdapter myAdapter;
    private AlertDialog alertDialog;

    private List<ContactBean> contactBeanList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        lv_contact = getListView();

        initData();

        myAdapter = new MyAdapter();
        lv_contact.setAdapter(myAdapter);

        initEvent();
    }

    private void initEvent() {
        lv_contact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 是否确认选择的号码
                showSureDialog(position);
            }
        });
    }

    private void showSureDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // 让用禁止取消操作 或者 定义取消事件
        // builder.setCancelable(false);
        builder.setTitle("注意")
                .setMessage("确定选择该安全号码!\r\n"+contactBeanList.get(position).getDisplayName()
                +"\r\n"+contactBeanList.get(position).getPhoneNum())
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        alertDialog.dismiss();
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 返回选择的数据
                        Intent intent = new Intent();
                        Bundle bundle = new Bundle();

                        bundle.putString("name",contactBeanList.get(position).getDisplayName());
                        bundle.putString("phone",contactBeanList.get(position).getPhoneNum());

                        intent.putExtras(bundle);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 进入主界面
                        alertDialog.dismiss();
                    }
                });
        alertDialog = builder.create();
        alertDialog.show();
    }

    private class MyAdapter extends BaseAdapter {

        ViewHodler viewHodler;

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                viewHodler = new ViewHodler();
                convertView = View.inflate(getApplicationContext(), R.layout.item_friend_listview, null);
                viewHodler.tv_name = (TextView) convertView.findViewById(R.id.tv_friend_name);
                viewHodler.tv_phone = (TextView) convertView.findViewById(R.id.tv_friend_phone);

                convertView.setTag(viewHodler);

            }
            viewHodler = (ViewHodler) convertView.getTag();

            if (contactBeanList == null) {
                contactBeanList = new ArrayList<>();
            }
            viewHodler.tv_name.setText(contactBeanList.get(position).getDisplayName());
            viewHodler.tv_phone.setText(contactBeanList.get(position).getPhoneNum());

            return convertView;
        }

        @Override
        public int getCount() {
            if (contactBeanList == null) {
                contactBeanList = new ArrayList<>();
            }
            return contactBeanList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        class ViewHodler {
            TextView tv_name;
            TextView tv_phone;

        }

    }

    Handler handler = new Handler() {

        ProgressDialog pd;

        @Override
        public void handleMessage(Message msg) {
            // 更新界面
            switch (msg.what) {
                case LOADING:
                    // show progressDialog
                    pd = new ProgressDialog(MyFriendActivity.this);
                    pd.setTitle("注意");
                    pd.setMessage("正在加载数据……");
                    pd.show();

                    break;
                case FINISH:
                    myAdapter.notifyDataSetChanged();
                    if (pd != null) {
                        pd.dismiss();
                        pd = null;
                    }
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    private void initData() {


        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = Message.obtain();
                msg.what = LOADING;
                handler.sendMessage(msg);

                contactBeanList = ReadContactsEngine.readContacts(getApplicationContext());
                if (contactBeanList == null) {
                    contactBeanList = new ArrayList<>();
                }

                msg = Message.obtain();
                msg.what = FINISH;
                handler.sendMessage(msg);
            }
        }).start();
    }
}
