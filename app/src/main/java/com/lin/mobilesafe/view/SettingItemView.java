package com.lin.mobilesafe.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lin.mobilesafe.R;

/**
 * Created by Administrator on 2016/7/11 0011.
 */
public class SettingItemView extends LinearLayout implements Checkable {

    private TextView tv_title;
    private TextView tv_content;
    private CheckBox cb_check;
    private String[] contents;
    private View item;

    /**
     * 配置文件中，反射实例化设置参数
     *
     * @param context
     * @param attrs
     */
    public SettingItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
        initEvent();


        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SettingItemView);

        //String title = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto", "setting_title");
        //String content = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto", "setting_content");

        String title = typedArray.getString(R.styleable.SettingItemView_setting_title);
        String content = typedArray.getString(R.styleable.SettingItemView_setting_content);
        boolean checked = typedArray.getBoolean(R.styleable.SettingItemView_setting_checked, false);

        contents = content.split("-");

        tv_title.setText(title);
        tv_content.setText(contents[0]);
        tv_content.setTextColor(Color.RED);
        cb_check.setChecked(checked);
    }

    public void setRootOnClickListener(OnClickListener listener) {
        item.setOnClickListener(listener);
    }

    private void initEvent() {

        cb_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    tv_content.setTextColor(Color.GREEN);
                    tv_content.setText(contents[1]);
                }else{
                    tv_content.setTextColor(Color.RED);
                    tv_content.setText(contents[0]);
                }
            }
        });
    }

    // 代码实例化调用该构造函数
    public SettingItemView(Context context) {
        super(context);
        initView();
    }

    /**
     * 初始化LinerLayout的子组件
     */
    private void initView() {
        item = View.inflate(getContext(), R.layout.item_settingcenter_layout, null);

        tv_title = (TextView) item.findViewById(R.id.tv_settingcenter_title);
        tv_content = (TextView) item.findViewById(R.id.tv_settingcenter_autoupdate_content);
        cb_check = (CheckBox) item.findViewById(R.id.cb_settingcenter_autoupdate);


        addView(item);
    }

    @Override
    public void setChecked(boolean checked) {
        cb_check.setChecked(checked);
    }

    @Override
    public boolean isChecked() {
        return cb_check.isChecked();
    }

    @Override
    public void toggle() {
        cb_check.setChecked(!cb_check.isChecked());
    }
}
