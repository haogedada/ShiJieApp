package com.shijie.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.util.Log;
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
import com.shijie.utils.Permission;
import com.shijie.utils.SharedPreferencesHelper;
import com.shijie.utils.StrJudgeUtil;
import com.shijie.wedget.FormWindow;


/**
 * Created by haoge on 2018/8/18.
 */

public class LoginActivity extends BaseActivity<LoginPresenter> implements LoginView,View.OnClickListener{

    private Button btnLogin,btnRegdit;
    private EditText editName,editPassword;
    private DialogFragment dialogFragment;
    private String name,password;
    private final int IMAGE_REQUEST_CODE=100;
    private String imgPath;
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
        new FormWindow(name,this,context, getSupportFragmentManager(), getFragmentManager()) {
            @Override
            public void success() {
                presenter.modifyUserMsg(name);
              //  loginSuccess();
            }
            @Override
            public void fial() {
                showModifyMsgFail();
            }
        }.modifyMsgForm(0);
    }

    private void login() {
         name = editName.getText().toString();
         password = editPassword.getText().toString();
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
                name = editName.getText().toString();
                new Permission(this) {
                    @Override
                    public void success() {
                        presenter.modifyUserMsg(name);
                    }

                    @Override
                    public void fail() {

                    }
                }.permissionCheck(100, new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                },new String[]{
                        "权限提示","需要读取文件权限","取消","确定"
                });
                break;
        }
    }
    /**
     * 选择完照片的回调时间
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //在相册里面选择好相片之后调回到现在的这个activity中
        switch (requestCode) {
            case IMAGE_REQUEST_CODE://这里的requestCode是我自己设置的，就是确定返回到那个Activity的标志
                if (resultCode == RESULT_OK) {//resultcode是setResult里面设置的code值
                    try {
                        Uri selectedImage = data.getData(); //获取系统返回的照片的Uri
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        Cursor cursor = getContentResolver().query(selectedImage,
                                filePathColumn, null, null, null);//从系统表中查询指定Uri对应的照片
                        cursor.moveToFirst();
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        imgPath = cursor.getString(columnIndex);  //获取照片路径
                        cursor.close();
                        String finalKey="imgpath";
                        if (StrJudgeUtil.isCorrectStr(name)){
                            new SharedPreferencesHelper(context,"user_msg_"+name)
                                    .put(finalKey,imgPath);
                            Log.e("调试", "储存: "+finalKey+"|"+ imgPath);
                        }
                    } catch (Exception e) {
                        // TODO Auto-generatedcatch block
                        showModifyMsgFail();
                        e.printStackTrace();
                    }
                }else {
                    showModifyMsgFail();
                }
                break;
            default:
                break;
        }
    }

    /**
     * 显示修改信息失败弹窗
     */
    private void showModifyMsgFail(){
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
}
