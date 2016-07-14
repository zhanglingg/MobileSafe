package com.lin.mobilesafe.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lin.mobilesafe.R;
import com.lin.mobilesafe.domain.BlackNameBean;

/**
 * Created by Administrator on 2016/7/13 0013.
 */
public class TelSmsSafeItemView extends LinearLayout {

    private TextView mPhoneNum;
    private TextView mMode;
    private View mViewContent;
    private ImageView mDelete;
    private String[] contentText;

    public TelSmsSafeItemView(Context context) {
        super(context);
        initView();
    }

    public TelSmsSafeItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TelSmsSafeItemView);

        String safe_phone = typedArray.getString(R.styleable.TelSmsSafeItemView_safe_phone);
        String safe_mode = typedArray.getString(R.styleable.TelSmsSafeItemView_safe_mode);

        contentText = safe_mode.split("-");

        mPhoneNum.setText(safe_phone);
        mMode.setText(contentText[0]);
    }


    private void initView() {
        mViewContent = View.inflate(getContext(), R.layout.item_telsmssafe_layout, null);
        mPhoneNum = (TextView) mViewContent.findViewById(R.id.tv_safecenter_phone);
        mMode = (TextView) mViewContent.findViewById(R.id.tv_safecenter_mode);

        mDelete = (ImageView) mViewContent.findViewById(R.id.iv_safecenter_delete);

        addView(mViewContent);
    }

    public void setDeleteOnClickListener(OnClickListener listener){
        mDelete.setOnClickListener(listener);
    }

    public void setText(BlackNameBean blackNameBean){

        mPhoneNum.setText(blackNameBean.getPhoneNum());

        if(blackNameBean.getMode().equals("1")){
            mMode.setText(contentText[0]);
        } else if(blackNameBean.getMode().equals("2")){
            mMode.setText(contentText[1]);
        } else {
            mMode.setText(contentText[2]);
        }
    }

}
