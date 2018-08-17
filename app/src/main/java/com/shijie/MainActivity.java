package com.shijie;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.shijie.base.BaseActivity;
import com.shijie.mvp.presenter.LoginPresenter;
import com.shijie.mvp.view.LoginView;

public class MainActivity extends BaseActivity<LoginPresenter> implements LoginView,View.OnClickListener {
    private EditText et_name;
    private EditText et_password;
    private Button btn_logon,bt_error;
    @Override
    protected LoginPresenter createPresenter() {
        return new LoginPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData() {
    }

    /**
     * 初始化界面
     */
    public void initView() {
        et_name = (EditText) findViewById(R.id.et_name);
        et_password = (EditText) findViewById(R.id.et_password);
        btn_logon = (Button) findViewById(R.id.btn_login);
         bt_error = (Button) findViewById(R.id.bt_error);
        btn_logon.setOnClickListener(this);
        bt_error.setOnClickListener(this);
    }
    /**
     * 登录按钮的监听方法
     * 这里要做后台数据的处理，需要用到Presenter
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                String name = et_name.getText().toString();
                String password = et_password.getText().toString();
                if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(password)) {
                    presenter.login(name, password);
                }
                break;
            case R.id.bt_error:


                break;

        }

    }

    @Override
    public void showNetworkError() {

    }

    @Override
    public void showVerifyFailed() {

    }

    @Override
    public void loginSuccess() {
       // Toast.makeText(this,"登录成功",Toast.LENGTH_SHORT).show();
        showtoast("登录成功");
    }

    @Override
    public void fristLogin() {

    }

    @Override
    public void onRefresh() {

    }
}
