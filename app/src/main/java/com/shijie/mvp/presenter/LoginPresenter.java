package com.shijie.mvp.presenter;

import android.util.Log;

import com.shijie.base.BaseModel;
import com.shijie.base.BaseObserver;
import com.shijie.base.BasePresenter;
import com.shijie.mvp.view.LoginView;
import com.shijie.utils.StrJudgeUtil;

/**
 * 登录业务逻辑
 * Created by haoge on 2018/8/15.
 */

public class LoginPresenter extends BasePresenter<LoginView> {
    /**
     * 在构造方法中实例化View对象
     */
    public LoginPresenter(LoginView loginView) {
        super(loginView);
    }

    /**
     * 登录业务
     */
    public void login(String name, String pwd) {
        if (!StrJudgeUtil.isCorrectStr(name)) {
            if (!StrJudgeUtil.isCorrectStr(pwd)) {
                baseView.showError("密码不能为空");
            } else {
                baseView.showError("用户名不能为空");
            }
        }
            addDisposable(apiServer.LoginByRx(name, pwd), new BaseObserver(baseView) {
                @Override
                public void onSuccess(Object o) {
                    BaseModel model = (BaseModel) o;
                    Log.e("调试", "数据:" + model.getData());
                    //保存token,写入文件
                    if (model.getCode()==200) {
                        baseView.loginSuccess();
                        baseView.hideLoading();
                    }else if (model.getCode()==105){
                        baseView.showVerifyFailed();
                        baseView.hideLoading();
                    }

                }
                @Override
                public void onError(String msg) {
                    baseView.showNetworkError(msg);
                    Log.e("调试", "网络连接失败:"+msg );
                }

                @Override
                public void FristLogin(Object o) {
                    baseView.fristLogin();
                }
            });
        }

}
