package com.lin.mobilesafe.view;


import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lin.mobilesafe.R;
import com.lin.mobilesafe.domain.UrlBean;

import java.io.BufferedReader;
import java.io.File;
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
    private TextView tv_splash_version;
    private ProgressBar pb_download;
    private TextView tv_download;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        initView();

        initData();

        initAnimation();

//        Bitmap bitmap = BitmapFactory.decodeFile("");
//        bitmap.recycle();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
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

            tv_splash_version.setText(versionName);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 常见错误情况
     * 1.没有网络
     * 2.找不到资源
     * 3.json数据格式错误
     */

    private void checkVersion() {
        // 访问服务器，获取数据Url
        // 子线程中执行，访问服务器

        urlBean = new UrlBean();

        Log.e("zl", ":checkVersion");

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    URL url = new URL("http://192.168.32.171:8080/safeversion.json");

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

                        while (jsonString != null) {
                            builder.append(jsonString);
                            jsonString = reader.readLine();
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
                    loadHome();
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

    private void loadHome() {
        Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * 显示是否更新对话框
     */
    private void showUpdateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // 让用禁止取消操作 或者 定义取消事件
        // builder.setCancelable(false);
        builder.setTitle("提醒")
                .setMessage("请更新到新版本" + urlBean.getDesc())
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        loadHome();
                    }
                })
                .setPositiveButton("更新", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 访问网络，下载更新APK
                        downLoadNewApk();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 进入主界面
                        loadHome();
                    }
                });
        builder.show();
    }

    private void downLoadNewApk() {

        HttpUtils utils = new HttpUtils();
        // urlBean.getUrl()下载的url
        // target 本地路径
        utils.download(urlBean.getUrl(), "mnt/sdcard/xx1.apk", new RequestCallBack<File>() {

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                pb_download.setVisibility(View.VISIBLE);
                tv_download.setVisibility(View.VISIBLE);
                pb_download.setMax((int) total);
                pb_download.setProgress((int) current);
                int progress = (int) (current * 100 / total);
                tv_download.setText(progress + "%");

                super.onLoading(total, current, isUploading);
            }

            @Override
            public void onSuccess(ResponseInfo<File> responseInfo) {
                // 下载成功
                // 在主线程中执行
                Toast.makeText(getApplicationContext(), "下载成功", Toast.LENGTH_LONG).show();
                Log.e("zl", "下载成功");
                // 安装apk
                installApk();
                pb_download.setVisibility(View.GONE);
                tv_download.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                // 下载失败
                Toast.makeText(getApplicationContext(), "下载失败", Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * 安装APK
     */
    private void installApk() {
        // 拷贝上层源码packageinstall的清单文件
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        String type = "application/vnd.android.package-archive";
        Uri data = Uri.fromFile(new File("mnt/sdcard/xx1.apk"));
        intent.setDataAndType(data, type);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 如果用户取消更新Apk，那么直接进入主页面
        loadHome();
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void isNewVersion(UrlBean urlBean) {

        String serverCode = urlBean.getVersionCode().trim();

        if (serverCode.equals(versionCode + "")) {
            // 进入主界面
            Message msg = Message.obtain();
            msg.what = LOADMAIN;
            handler.sendMessage(msg);
        } else {
            /**
             * 有新版本更新，显示新版本的描述信息，让用户点击是否更新
             */
            Log.e("version", "service:" + serverCode + "   :verionCode:" + versionCode);
            Message msg = Message.obtain();
            msg.what = SHOWUPDATEALOG;
            handler.sendMessage(msg);

        }
    }

    private void initView() {
        rl_Root = (RelativeLayout) findViewById(R.id.rl_splash_root);
        tv_splash_version = (TextView) findViewById(R.id.tv_splash_version);
        pb_download = (ProgressBar) findViewById(R.id.pb_download_progress);
        tv_download = (TextView) findViewById(R.id.tv_download_progress);

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

        //sObjectAnimator objectAnimator = new ObjectAnimator();

        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                checkVersion(); // 检查服务器版本
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        ObjectAnimator objectAnimator = new ObjectAnimator();
        objectAnimator.addListener(new AnimatorListenerAdapter() {
        });

        rl_Root.setAnimation(animationSet);

    }
}
