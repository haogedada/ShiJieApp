package com.shijie.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.WindowManager;

import com.github.paolorotolo.appintro.AppIntro;
import com.shijie.R;
import com.shijie.utils.ActivityUtils;
import com.shijie.utils.PermissionHelper;
import com.shijie.utils.SharedPreferencesHelper;
import com.shijie.wedget.SampleSlide;

/**
 * Created by haoge on 2018/8/21.
 */

public class IntroActivity extends AppIntro {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 沉浸效果
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        ActivityUtils.addActivity(this);
        setBarColor(Color.parseColor("#B3F5FF"));
        setFadeAnimation();//过度动画
        permissionApplication();
        addSlide(SampleSlide.newInstance(R.layout.intro_fragment_1));
        addSlide(SampleSlide.newInstance(R.layout.intro_fragment_2));
        addSlide(SampleSlide.newInstance(R.layout.intro_fragment_3));
        setSeparatorColor(Color.GRAY);
        //设置按钮
        showSkipButton(true);
        setSkipText("跳过");
        setColorSkipButton(Color.parseColor("#8181A2"));
        setDoneText("开始体验");
        setColorDoneText(Color.parseColor("#8181A2"));
        setProgressButtonEnabled(true);
        setVibrate(true);
        setVibrateIntensity(30);
    }

    /**
     * 点击跳过的点击事件
     *
     * @param currentFragment
     */
    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        new SharedPreferencesHelper(this, "first_start_up").put("isFirst", false);
        Intent intent = new Intent(IntroActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    /**
     * 点击完成的点击事件
     *
     * @param currentFragment
     */
    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        new SharedPreferencesHelper(this, "first_start_up").put("isFirst", false);
        Intent intent = new Intent(IntroActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        //幻灯片变化时做一些事情
    }

    /**
     * 一些权限申请处理
     */
    public void permissionApplication() {
        new PermissionHelper(this, getSupportFragmentManager()) {
            @Override
            public void success() {

            }

            @Override
            public void fail() {
            }

        }.allUsePermission();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityUtils.removeAllActivity();
    }
}
