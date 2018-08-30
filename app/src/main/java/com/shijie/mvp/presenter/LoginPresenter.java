package com.shijie.mvp.presenter;

import android.util.Log;

import com.shijie.App;
import com.shijie.base.BaseModel;
import com.shijie.base.BaseObserver;
import com.shijie.base.BasePresenter;
import com.shijie.mvp.view.LoginView;
import com.shijie.utils.SharedPreferencesHelper;
import com.shijie.utils.StrJudgeUtil;
import com.shijie.utils.UpLoadFormUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;

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
        //调用登陆接口，发起请求
            addDisposable(apiServer.LoginByRx(name, pwd), new BaseObserver(baseView) {
                @Override
                public void onSuccess(BaseModel model) {
                    String token= (String) model.getData();
                    if (model.getCode()==200) {
                        //保存token,写入文件
                        new SharedPreferencesHelper(App.getApplication(),"user_token")
                                .put("token",token);
                        baseView.loginSuccess();
                        baseView.hideLoading();
                    }else if (model.getCode()==105){
                        baseView.showVerifyFailed();
                        baseView.hideLoading();
                    }else if(model.getCode()==199){
                        new SharedPreferencesHelper(App.getApplication(),"user_token")
                                .put("token",token);
                        baseView.fristLogin();
                    }else if (model.getCode()==401){
                        new SharedPreferencesHelper(App.getApplication(),"user_token")
                                .remove("token");
                          login(name,pwd);
                    }
                }

                @Override
                public void onNetworkError(String msg) {
                    baseView.showNetworkError(msg);
                }

                @Override
                public void onError(String msg) {

                }
            });
        }
    /**
     * 发送修改用户信息请求
     * @param userName
     */
    public void modifyUserMsg(String userName){
        if(!StrJudgeUtil.isCorrectStr(userName)){
            return;
        }
        Map<String, ?> userMsg =  new SharedPreferencesHelper(App.getApplication(), "user_msg_" + userName).getAll();
        String imgpath= (String) userMsg.get("imgfile");

        File imgfile=new File(imgpath);
        if (!imgfile.exists()){
            //照片文件不存在
            return;
        }
        List imgfiles=new ArrayList();
        imgfiles.add(imgfile);
        userMsg.remove("imgfile");
        MultipartBody multipartBody = UpLoadFormUtil.formToMultipartBody(userMsg,imgfiles);
            addDisposable(apiServer.modifyUser(multipartBody), new BaseObserver(baseView) {
            @Override
            public void onSuccess(BaseModel model) {
                Log.e("修改用户信息成功", "onSuccess: " );
            }
            @Override
            public void onNetworkError(String msg) {

            }

            @Override
            public void onError(String msg) {

            }
        });
    }

}
