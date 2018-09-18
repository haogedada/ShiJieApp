package com.shijie.mvp.view;

import com.shijie.base.BaseView;

/**
 * mvp中的View的定义
 * 用户登录界面用户的操作行为的定义
 *
 * Created by haoge on 2018/8/15.
 */

public interface LoginView extends BaseView {

    void showVerifyFailed();//信息验证失败,账号或密码有误

    void loginSuccess();//登录成功

    void fristLogin();//第一次登陆视图

}
