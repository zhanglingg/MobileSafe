package com.lin.mobilesafe;


import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.ActivityManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class SplashActivity extends Activity {

    private RelativeLayout rl_Root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);


        ActivityManager memoryInfo = ActivityManager.getMyMemoryState();


        initView();
        initAnimation();

        Bitmap bitmap = BitmapFactory.decodeFile("");
        bitmap.recycle();

        checkVersion(); // 检查服务器版本
    }

    private void checkVersion() {
        // 访问服务器，获取数据Url
        // 子线程中执行，访问服务器

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("");

                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setReadTimeout(5000);
                    connection.setConnectTimeout(5000);
                    connection.setRequestMethod("GET");
                    int responseCode = connection.getResponseCode();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initView() {
        rl_Root = (RelativeLayout) findViewById(R.id.rl_splash_root);
    }

    private void initAnimation() {
        // 创建动画

        // 1. Alpaha 透明度
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        alphaAnimation.setDuration(3000);
        alphaAnimation.setFillAfter(true); // 动画停止在结束效果

        // 2. Rotate 旋转
        RotateAnimation rotateAnimation = new RotateAnimation(0, -360,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(3000);
        rotateAnimation.setFillAfter(true);

        // 3. Scale 缩放
        ScaleAnimation scaleAnimation = new ScaleAnimation(
                0.0f, 1.0f,
                0.0f, 1.0f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f);

        scaleAnimation.setDuration(3000);
        rotateAnimation.setFillAfter(true);

        AnimationSet animationSet = new AnimationSet(true);

        animationSet.addAnimation(alphaAnimation);
        animationSet.addAnimation(rotateAnimation);
        animationSet.addAnimation(scaleAnimation);

        ObjectAnimator objectAnimator = new ObjectAnimator();

        rl_Root.setAnimation(animationSet);


    }
}
