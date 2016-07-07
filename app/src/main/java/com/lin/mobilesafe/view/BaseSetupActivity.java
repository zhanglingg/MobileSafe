package com.lin.mobilesafe.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.lin.mobilesafe.R;

/**
 * Created by Administrator on 2016/7/6 0006.
 */
public abstract class BaseSetupActivity extends AppCompatActivity {

    private GestureDetector gd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();

        initView();
        initData();
        initEvent();
        initGesture(); // 初始化手势识别器,,,要绑定Ontouch事件

    }


    public void initView() {

    }

    public void initData() {

    }

    public void initEvent() {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        gd.onTouchEvent(event); // 绑定onTouch事件
        return super.onTouchEvent(event);
    }

    private void initGesture() {
        gd = new GestureDetector(this, new GestureDetector.OnGestureListener() {

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

                // X轴方向的速度是否满足横向滑动的条件pix/s

                float dx = e2.getX() - e1.getX();

                if (Math.abs(distanceX) > 100 && Math.abs(dx) > 200) {
                    if (dx < 0) { // 从右往左滑
                        next(null);
                    } else {
                        prev(null);
                    }
                }
                Log.e("GestureDetector", "速度：" + distanceX + "  距离：" + dx);
                return true;
            }

            @Override
            public boolean onDown(MotionEvent e) {
                return false;
            }

            @Override
            public void onShowPress(MotionEvent e) {

            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return false;
            }


            @Override
            public void onLongPress(MotionEvent e) {

            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                return false;
            }
        });
    }

    public void next(View view) {
        // 页面跳转
        nextActivity();
        // 动画播放
        nextAnimation();
    }


    public void startActivity(Class classType) {
        Intent intent = new Intent(this, classType);
        startActivity(intent);
        finish();
    }

    protected abstract void nextActivity();

    protected abstract void prevActivity();

    public void prev(View view) {
        // 页面跳转
        prevActivity();
        // 动画播放
        prevAnimation();
    }

    private void prevAnimation() {
        overridePendingTransition(R.anim.prev_in_left, R.anim.prev_out_right);
    }

    private void nextAnimation() {
        overridePendingTransition(R.anim.next_in_right, R.anim.next_out_left);
    }


}
