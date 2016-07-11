package com.lin.mobilesafe.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lin.mobilesafe.R;

/**
 * Created by Administrator on 2016/7/11 0011.
 */
public class SettingItemView extends LinearLayout {

    private TextView tv_title;
    private TextView tv_content;

    /**
     * 配置文件中，反射实例化设置参数
     *
     * @param context
     * @param attrs
     */
    public SettingItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
        String title = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto", "setting_title");
        String content = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto", "setting_content");

        tv_title.setText(title);
        tv_content.setText(content);
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
        View item = View.inflate(getContext(), R.layout.item_settingcenter_layout, null);

        tv_title = (TextView) item.findViewById(R.id.tv_settingcenter_title);
        tv_content = (TextView) item.findViewById(R.id.tv_settingcenter_autoupdate_content);

        addView(item);
    }

}
