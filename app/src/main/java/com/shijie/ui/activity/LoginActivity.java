package com.shijie.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.mylhyl.circledialog.CircleDialog;
import com.mylhyl.circledialog.params.ProgressParams;
import com.shijie.MainActivity;
import com.shijie.R;
import com.shijie.base.BaseActivity;
import com.shijie.mvp.presenter.LoginPresenter;
import com.shijie.mvp.view.LoginView;
import com.shijie.wedget.FormWindow;


/**
 * Created by haoge on 2018/8/18.
 */

public class LoginActivity extends BaseActivity<LoginPresenter> implements LoginView,View.OnClickListener{

    private Button btnLogin,btnRegdit;
    private EditText editName,editPassword;
    private DialogFragment dialogFragment;
    /**
     * 显示登录等待框
     */
    @Override
    public void showLoading() {
        dialogFragment = new CircleDialog.Builder()
                .setProgressText("登录中...")
                .setProgressStyle(ProgressParams.STYLE_SPINNER)
                        .setProgressDrawable(R.drawable.bg_progress_s)
                .show(getSupportFragmentManager());
    }

    /**
     * 隐藏等待框
     */
    @Override
    public void hideLoading() {
        if (dialogFragment.isCancelable())
            dialogFragment.dismiss();
    }

    @Override
    protected LoginPresenter createPresenter() {
        return new LoginPresenter(this);
    }

    /**
     * 设置布局文件
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_login;
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {

    }

    /**
     * 初始化界面
     */
    @Override
    protected void initView() {
        setIsBottomTabBar(false);
        btnLogin=findViewById(R.id.btn_login);
        btnRegdit=findViewById(R.id.btn_regdit);
        editName=findViewById(R.id.edit_name);
        editPassword=findViewById(R.id.edid_password);
        btnLogin.setOnClickListener(this);
        btnRegdit.setOnClickListener(this);
    }


    /**
     * 网络错误
     */
    @Override
    public void showNetworkError(String msg) {
        new CircleDialog.Builder()
                .setCanceledOnTouchOutside(false)
                .setCancelable(false)
                .configDialog(params -> {
                    params.backgroundColor = Color.WHITE;
                    params.backgroundColorPress = Color.GRAY;
                })
                .setTitle("网络异常!!")
                .setTitleColor(Color.RED)
                .setText(msg+"!\n"+"您需要尝试重新连接吗？")
                .configText(params -> {
                    params.padding = new int[]{100, 0, 100, 50};
                })
                .setNegative("取消", null)
                .setPositive("确定", v ->
                       login())
                .configPositive(params -> params.backgroundColorPress = Color.GRAY)
                .show(getSupportFragmentManager());
    }

    /**
     * 账号密码错误登录失败
     */
    @Override
    public void showVerifyFailed() {
        showToast("账号或密码错误");
    }

    /**
     * 登录成功
     * 跳转页面暂时用显示跳转，这种方式后面要换
     */
    @Override
    public void loginSuccess() {
        Intent intent=new Intent(LoginActivity.this, MainActivity.class);   //Intent intent=new Intent(MainActivity.this,JumpToActivity.class);
        startActivity(intent);
    }

    /**
     * 第一次登陆
     * 跳出弹窗要求用户完善个人资料
     */
    @Override
    public void fristLogin() {


        new FormWindow(context, getSupportFragmentManager(), getFragmentManager()) {
            @Override
            public void success() {
                loginSuccess();
            }
            @Override
            public void fial() {
                //弹窗
                new CircleDialog.Builder()
                        .setCanceledOnTouchOutside(false)
                        .setCancelable(false)
                        .configDialog(params -> {
                            params.backgroundColor = Color.WHITE;
                            params.backgroundColorPress = Color.GRAY;
                        })
                        .setTitle("完善个人资料失败")
                        .setTitleColor(Color.RED)
                        .setText("如不完成个人资料每次登陆都会有完善资料提示！\n您确定不完善个人资料？")
                        .configText(params -> {
                            params.padding = new int[]{100, 0, 100, 50};
                        })
                        .setNegative("取消", null)
                        .setPositive("确定", v ->{
                                    loginSuccess();
                                }
                        )
                        .configPositive(params -> params.backgroundColorPress = Color.GRAY)
                        .show(getSupportFragmentManager());
            }
        }.modifyMsgForm(0);


    }

    private void login() {
        String name = editName.getText().toString();
        String password = editPassword.getText().toString();
        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(password)) {
            presenter.login(name, password);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login:
                login();
                break;
            case R.id.btn_regdit:
                new FormWindow(context, getSupportFragmentManager(), getFragmentManager()) {
                    @Override
                    public void success() {
                        loginSuccess();
                    }
                    @Override
                    public void fial() {
                        new CircleDialog.Builder()
                                .setCanceledOnTouchOutside(false)
                                .setCancelable(false)
                                .configDialog(params -> {
                                    params.backgroundColor = Color.WHITE;
                                    params.backgroundColorPress = Color.GRAY;
                                })
                                .setTitle("完善个人资料失败")
                                .setTitleColor(Color.RED)
                                .setText("如不完成个人资料每次登陆都会有完善资料提示！\n您确定不完善个人资料？")
                                .configText(params -> {
                                    params.padding = new int[]{100, 0, 100, 50};
                                })
                                .setNegative("取消", null)
                                .setPositive("确定", v ->{
                                            loginSuccess();
                                        }
                                       )
                                .configPositive(params -> params.backgroundColorPress = Color.GRAY)
                                .show(getSupportFragmentManager());
                    }
                }.modifyMsgForm(0);
                break;
        }
    }

}
