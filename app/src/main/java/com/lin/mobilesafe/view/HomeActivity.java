package com.lin.mobilesafe.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lin.mobilesafe.R;
import com.lin.mobilesafe.utils.MD5Utils;
import com.lin.mobilesafe.utils.MyConstants;
import com.lin.mobilesafe.utils.SpTools;

public class HomeActivity extends AppCompatActivity {

    private GridView gv_menus;
    private MyAdapter adapter;
    private AlertDialog dialog;

    private int icons[] = {R.mipmap.safe, R.mipmap.callmsgsafe, R.mipmap.app,
            R.mipmap.taskmanager, R.mipmap.netmanager, R.mipmap.trojan,
            R.mipmap.sysoptimize, R.mipmap.atools, R.mipmap.settings};
    private String iconsName[] = {"手机防盗", "通讯卫士", "软件管理",
            "进程管理", "流量统计", "病毒查杀",
            "缓存清理", "高级工具", "设置中心"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);
        initView();
        initdata();

        initEvent();
    }

    private void initEvent() {
        gv_menus.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 判断点击位置
                switch (position) {
                    case 0: // 手机防盗
                        // 自定义对话框
                        showSettingDialog();
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void showSettingDialog() {

        final String password = SpTools.getString(getApplicationContext(), MyConstants.PASSWORD, "");

        Log.e("SpPassword",password);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = View.inflate(getApplicationContext(), R.layout.dialog_setting_password, null);

        final EditText et_passone = (EditText) view.findViewById(R.id.et_dialog_setting_password_passone);
        final EditText et_passtwo = (EditText) view.findViewById(R.id.et_dialog_setting_password_passtwo);

        Button btn_setPsw = (Button) view.findViewById(R.id.btn_setting_password);
        Button btn_cancelPsw = (Button) view.findViewById(R.id.btn_setting_password_cancel);

        if (!"".equals(password)) {
            et_passtwo.setVisibility(View.GONE);
            btn_setPsw.setText("输入密码");
            btn_cancelPsw.setText("取消输入");
        }

        builder.setView(view);

        btn_cancelPsw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btn_setPsw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String passone = et_passone.getText().toString().trim();

                if (!"".equals(password)) {
                    if (!MD5Utils.md5(passone).equals(password)) {
                        Toast.makeText(getApplicationContext(),"密码错误",Toast.LENGTH_SHORT).show();
                        return;
                    }else{
                        Toast.makeText(getApplicationContext(),"密码输入成功",Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        Intent intent = new Intent(HomeActivity.this, LostFindActivity.class);
                        startActivity(intent);
                        return;
                    }
                }

                String passtwo = et_passtwo.getText().toString().trim();

                if (TextUtils.isEmpty(passone) || TextUtils.isEmpty(passtwo)) {
                    Toast.makeText(getApplicationContext(), "密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!passone.equals(passtwo)) {
                    Toast.makeText(getApplicationContext(), "两次密码不一致", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    // 保存密码到sp中
                    Log.e("zl", "保存密码");
                    // 对密码进行加密MD5 (银行加密十次以上)
                    String pass = MD5Utils.md5(passone);
                    Log.e("MD5", pass);

                    SpTools.putString(getApplicationContext(), MyConstants.PASSWORD, pass);

                    dialog.dismiss();
                }
            }
        });

        dialog = builder.create();
        dialog.show();
    }

    private void initView() {
        gv_menus = (GridView) findViewById(R.id.gv_home_menus);

    }

    /**
     * 初始化组件数据
     */
    private void initdata() {
        adapter = new MyAdapter(getApplicationContext());
        gv_menus.setAdapter(adapter);

    }


    private class MyAdapter extends BaseAdapter {

        private LayoutInflater inflater;

        public MyAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            HolderView holderView = null;

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_home_grildview, null);
                holderView = new HolderView();
                holderView.icon = (ImageView) convertView.findViewById(R.id.iv_item);
                holderView.name = (TextView) convertView.findViewById(R.id.tv_item);
                convertView.setTag(holderView);
            }

            holderView = (HolderView) convertView.getTag();

            holderView.icon.setImageResource(icons[position]);
            holderView.name.setText(iconsName[position]);

            return convertView;
        }

        @Override
        public int getCount() {
            return icons.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }


        class HolderView {
            public ImageView icon;
            public TextView name;

        }

    }
}
