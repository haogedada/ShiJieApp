package com.shijie.ui.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.shijie.R;
import com.shijie.utils.ActivityUtils;
import com.shijie.utils.PermissionHelper;
import com.shijie.utils.SharedPreferencesHelper;

public class StartUpActivity extends AppCompatActivity {

    private TextView txViewTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_up);
        View view = getLayoutInflater().inflate(R.layout.activity_start_up, null);
        //设置填充activity_base布局
        super.setContentView(view);
        //去除标题栏
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        // 沉浸效果
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) { // 透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            view.setFitsSystemWindows(true);
        }
        txViewTime=findViewById(R.id.text_view_time);
        init();
        ActivityUtils.addActivity(this);
    }


    /**
     * 第一次启动
     */
    private void fristStartUp(){
        boolean isFirst=(boolean)new SharedPreferencesHelper(this,"first_start_up").getSharedPreference("isFirst",true);
        //如果是第一次启动可以在此进行一些操作，例如初始化数据库等；
        if (isFirst){
            Intent intent=new Intent(StartUpActivity.this,IntroActivity.class);
            startActivity(intent);
        }
    }

    /**
     * 做一些初始化
     */
    private void init(){
        fristStartUp();
        permissionApplication();
    }

    /**
     * 加载页面倒计时
     */
    private void ountDown(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                int i=3;
                while (i>=0) {
                    try {
                        int finalI = i;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                txViewTime.setText(finalI+" ");
                            }
                        });
                        Thread.sleep(1000);
                        i--;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent=new Intent(StartUpActivity.this,LoginActivity.class);
                        startActivity(intent);
                    }
                });
                }
                }).start();
    }

    /**
     * 一些权限申请，用户拒绝将退出软件
     */
    public void permissionApplication() {
       new PermissionHelper(this, getSupportFragmentManager()) {
           @Override
           public void success() {
               ountDown();
           }

           @Override
           public void fail() {
               ountDown();
           }
       }.allUsePermission();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityUtils.removeAllActivity();
    }
}
