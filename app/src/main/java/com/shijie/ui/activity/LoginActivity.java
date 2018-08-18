package com.shijie.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mylhyl.circledialog.CircleDialog;
import com.mylhyl.circledialog.params.ProgressParams;
import com.shijie.MainActivity;
import com.shijie.R;
import com.shijie.base.BaseActivity;
import com.shijie.mvp.presenter.LoginPresenter;
import com.shijie.mvp.view.LoginView;


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
        dialogFragment = new CircleDialog.Builder()
                .setCanceledOnTouchOutside(false)
                .setCancelable(true)
                .setInputManualClose(true)
                .setTitle("请完善个人资料")
                .setSubTitle("昵称")
                .autoInputShowKeyboard()
                .setInputCounter(20)
//                        .setInputCounter(20, (maxLen, currentLen) -> maxLen - currentLen + "/" + maxLen)
                .configInput(params -> {
//                            params.padding = new int[]{30, 30, 30, 30};
//                                params.inputBackgroundResourceId = R.drawable.bg_input;
//                                params.gravity = Gravity.CENTER;
                    //密码
//                                params.inputType = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD
//                                        | InputType.TYPE_TEXT_FLAG_MULTI_LINE;
                    //文字加粗
                    params.styleText = Typeface.BOLD;
                })
                .setSubTitle("性别")
                .autoInputShowKeyboard()
                .setInputCounter(2)
                .configInput(params -> {
                    params.styleText = Typeface.BOLD;
                })
                .setNegative("取消", null)
                .setPositiveInput("确定", (text, v) -> {
                    if (TextUtils.isEmpty(text)) {
                        Toast.makeText(LoginActivity.this, "请输入内容", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(LoginActivity.this, text, Toast.LENGTH_SHORT).show();
                        dialogFragment.dismiss();
                    }
                })
                .show(getSupportFragmentManager());

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

                modify(0);
                break;
        }
    }

    private void modify(int index){
        String leftBtn= "上一步";
        String rightBtn= "下一步";
         String subTitle=null;
         String inputHint=null;
         switch (index){
             case 0:
                 leftBtn = "取消";
                 subTitle = "昵称";
                 inputHint="请输入您的"+subTitle;
             break;
             case 1:
                 subTitle = "性别";
                 inputHint="请输入您的性别,男或女";
             break;
             case 2:
                 subTitle = "生日";
                 inputHint="请输入您的生日,时间用-分开";
                 break;
             case 3:
                 subTitle="个性签名";
                 inputHint="请输入您的个性签名";
                 break;
             case 4:
                 final String[] items = {"拍照", "从相册选择", "小视频"};
                 new CircleDialog.Builder()
                         .configDialog(params -> {
                             params.backgroundColorPress = Color.CYAN;
                             //增加弹出动画
                             params.animStyle = R.style.dialogWindowAnim;
                         })
                         .setTitle("选择头像照片")
//                        .setTitleColor(Color.BLUE)
                         .configTitle(params -> {
//                                params.backgroundColor = Color.RED;
                         })
                         .setSubTitle("请从以下中选择照片的方式进行提交")
                         .configSubTitle(params -> {
//                                params.backgroundColor = Color.YELLOW;
                         })
                         .setItems(items, (parent, view1, position1, id) ->
                                 Toast.makeText(LoginActivity.this, "点击了：" + items[position1]
                                         , Toast.LENGTH_SHORT).show())
                         .setNegative("取消", null)
//                        .setNeutral("中间", null)
//                        .setPositive("确定", null)
//                        .configNegative(new ConfigButton() {
//                            @Override
//                            public void onConfig(ButtonParams params) {
//                                //取消按钮字体颜色
//                                params.textColor = Color.RED;
//                                params.backgroundColorPress = Color.BLUE;
//                            }
//                        })
                         .show(getSupportFragmentManager());
                 subTitle="选择照片";
                 rightBtn="完成";
                 dialogFragment.dismiss();
         }
        dialogFragment = new CircleDialog.Builder()
                .setCanceledOnTouchOutside(false)
                .setCancelable(true)
                .setInputManualClose(true)
                .setTitle("请完善个人资料")
                .setSubTitle(subTitle)
                .setInputHint(inputHint)
              //  .setInputText("默认文本") //默认值
                .autoInputShowKeyboard()
                .setInputCounter(20)
                .configInput(params -> {
                           params.padding = new int[]{30, 10, 10, 30};
                    params.inputBackgroundResourceId = R.drawable.bg_input;
                })

                .setNegative(leftBtn, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Log.e("调试", "modify: " );
                                if (index==0){
                                    //TODO
                                    Log.e("调试", "0" );
                                    //不用进行操作
                                }else if (0<=index&&index<4){
                                    Log.e("调试", "1" );
                                    dialogFragment.dismiss();
                                    modify(index-1);
                                    Log.e("调试", "2" );
                                }
                            }
                        }
  //                      v->{
//                    Log.e("调试", "modify: " );
//                    if (index==0){
//                        //TODO
//                        Log.e("调试", "0" );
//                        //不用进行操作
//                    }else if (0<=index&&index<4){
//                        Log.e("调试", "1" );
//                        dialogFragment.dismiss();
//                        modify(index-1);
//                        Log.e("调试", "2" );
//                    }
//                }
                )
                .setPositiveInput(rightBtn, (text, v) -> {
                    if (TextUtils.isEmpty(text)) {
                        Toast.makeText(LoginActivity.this, "请输入内容", Toast.LENGTH_SHORT).show();
                    } else {
                        if (index==4){
                            //TODO
                            // 选择照片
                           // dialogFragment.dismiss();
                        }else if(0<=index&&index<4){
                            //写入text数据
                            dialogFragment.dismiss();
                            modify(index+1);
                        }
                    }
                })
                .show(getSupportFragmentManager());
    }
}
