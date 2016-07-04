package com.lin.mobilesafe.view;


import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

import com.lin.mobilesafe.R;
import com.lin.mobilesafe.domain.UrlBean;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class SplashActivity extends Activity {

    private static final int LOADMAIN = 1;      // 加载主界面
    private static final int SHOWUPDATEALOG = 2;//
    private RelativeLayout rl_Root;

    private UrlBean urlBean;
    private int versionCode;
    private String versionName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        initView();

        initData();

        initAnimation();

//        Bitmap bitmap = BitmapFactory.decodeFile("");
//        bitmap.recycle();

        checkVersion(); // 检查服务器版本
    }

    private void initData() {

        // 获取版本信息
        PackageManager manager = getPackageManager();
        try {
            PackageInfo packageInfo = manager.getPackageInfo(getPackageName(), 0);
            // 版本号
            versionCode = packageInfo.versionCode;

            // 版本名字
            versionName = packageInfo.versionName;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void checkVersion() {
        // 访问服务器，获取数据Url
        // 子线程中执行，访问服务器

        urlBean = new UrlBean();

        Log.e("zl", ":checkVersion");

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("http://192.168.32.118:8080/safeversion.json");

                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setReadTimeout(5000);
                    connection.setConnectTimeout(5000);
                    connection.setRequestMethod("GET");
                    int responseCode = connection.getResponseCode();

                    Log.e("zl", "" + responseCode);

                    if (responseCode == 200) {
                        InputStream inputStream = connection.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

                        StringBuilder builder = new StringBuilder();
                        String jsonString = reader.readLine();

                        if (jsonString != null) {
                            builder.append(jsonString);
                        }

                        Log.e("zl", builder + "");

                        urlBean.parseJson(builder);

                        // 比较当前的版本号
                        isNewVersion(urlBean);

                        reader.close();
                        connection.disconnect();
                    }

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            // 处理消息
            switch (msg.what) {
                case LOADMAIN:
                    Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                    startActivity(intent);
                    break;
                case SHOWUPDATEALOG:
                    // 显示更新版本对话框
                    showUpdateDialog();
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 显示是否更新对话框
     */
    private void showUpdateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("提醒")
                .setMessage("请更新到新版本" + urlBean.getDesc())
                .setPositiveButton("更新", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 跟新APK
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 进入主界面
                        Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                        startActivity(intent);
                    }
                });
        builder.show();
    }

    private void isNewVersion(UrlBean urlBean) {

        String serverCode = urlBean.getVersionCode();
        if (serverCode.equals(versionCode)) {
            // 进入主界面
            Message msg = Message.obtain();
            msg.what = LOADMAIN;
            handler.sendMessage(msg);
        } else {
            /**
             * 有新版本更新，显示新版本的描述信息，让用户点击是否更新
             */
            Message msg = Message.obtain();
            msg.what = SHOWUPDATEALOG;
            handler.sendMessage(msg);

        }
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
