package com.shijie;

import android.graphics.Color;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mylhyl.circledialog.CircleDialog;
import com.shijie.adapter.CheckedAdapter;
import com.shijie.base.BaseActivity;
import com.shijie.mvp.presenter.LoginPresenter;
import com.shijie.mvp.view.LoginView;

public class MainActivity extends BaseActivity<LoginPresenter> implements LoginView,View.OnClickListener {
    private EditText et_name;
    private EditText et_password;
    private Button btn_logon,bt_error;
    private DialogFragment dialogFragment;
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
        et_name = (EditText) findViewById(R .id.et_name);
        et_password = (EditText) findViewById(R.id.et_password);
        btn_logon = (Button) findViewById(R.id.btn_login);
        bt_error = (Button) findViewById(R.id.bt_error);
        btn_logon.setOnClickListener(this);
        bt_error.setOnClickListener(this);
        setIsBottomTabBar(true);
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
               // showContentView();
                break;
            case R.id.bt_error:
//                new CircleDialog.Builder()
//                        .setCanceledOnTouchOutside(false)
//                        .setCancelable(false)
//                        .configDialog(params -> {
//                            params.backgroundColor = Color.WHITE;
//                            params.backgroundColorPress = Color.GRAY;
//                        })
//                        .setTitle("标题")
//                        .setText("冷却风扇口无异物，风机扇叶无损伤，无过热痕迹")
//                        .configText(params -> {
////                                params.gravity = Gravity.LEFT | Gravity.TOP;
//                            params.padding = new int[]{100, 0, 100, 50};
//                        })
//                        .setNegative("取消", null)
//                        .setPositive("确定", v1 ->
//                                Toast.makeText(MainActivity.this, "确定", Toast.LENGTH_SHORT).show())
//                        .configPositive(params -> params.backgroundColorPress = Color.GRAY)
//                        .show(getSupportFragmentManager());
                final String[] objectsR = {"item0", "item1", "item2", "item3"};
                final CheckedAdapter checkedAdapterR = new CheckedAdapter(this, objectsR, true);

                new CircleDialog.Builder()
                        .configDialog(params -> params.backgroundColorPress = Color.CYAN)
                        .setTitle("带复选的ListView")
                        .setSubTitle("单选")
                        .setItems(checkedAdapterR, (parent, view15, position15, id) ->
                                checkedAdapterR.toggle(position15, objectsR[position15]))
                        .setItemsManualClose(true)
                        .setPositive("确定", v1 -> Toast.makeText(MainActivity.this
                                , "选择了：" + checkedAdapterR.getSaveChecked().toString()
                                , Toast.LENGTH_SHORT).show())
                        .show(getSupportFragmentManager());
                break;
        }
    }

    @Override
    public void showNetworkError(String msg) {

    }

    @Override
    public void showVerifyFailed() {

    }

    @Override
    public void loginSuccess() {
       // Toast.makeText(this,"登录成功",Toast.LENGTH_SHORT).show();
        showToast("登录成功");
    }

    @Override
    public void fristLogin() {

    }


    @Override
    public void onRefresh() {

    }



    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }
}
