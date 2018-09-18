package com.shijie.base;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.shijie.utils.DesUtil;

import org.json.JSONException;

import java.io.InterruptedIOException;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.text.ParseException;

import io.reactivex.observers.DisposableObserver;
import retrofit2.HttpException;


/**
 *
 * Created by haoge on 2018/8/16.
 */

public abstract class BaseObserver<T> extends DisposableObserver<T> {
    protected BaseView view;
    /**
     * 解析数据失败
     */
    public static final int PARSE_ERROR = 1001;
    /**
     * 网络问题
     */
    public static final int BAD_NETWORK = 1002;
    /**
     * 连接错误
     */
    public static final int CONNECT_ERROR = 1003;
    /**
     * 连接超时
     */
    public static final int CONNECT_TIMEOUT = 1004;

    /**
     * 密匙
     */
    private final static String key = "haogedad";


    public BaseObserver(BaseView view) {
        this.view = view;
    }

    @Override
    protected void onStart() {
        if (view != null) {
            view.showLoading();
        }
    }

    /**
     * 网络请求结果对象
     * @param o
     */
    @Override
    public void onNext(T o) {
        try {
            BaseModel model = (BaseModel) o;
            if (model.getCode() == 200 || model.getCode() == 105 || model.getCode() == 199 || model.getCode() == 401) {
                String s = model.getData().toString();
                //获取到的数据进行解密
                String data =  DesUtil.decrypt(s, key);
                if (data!=null){
                    Object obj=new Gson().fromJson(data,Object.class);
                    model.setData(obj);
                }
                onSuccess(model);
            } else {
                if (view != null) {
                    view.showError(new Gson().toJson(model.getData()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            onError(e.toString());
        }

    }



    @Override
    public void onError(Throwable e) {
        if (view != null) {
            view.hideLoading();
        }
        if (e instanceof HttpException) {
            //   HTTP错误
            onException(BAD_NETWORK);
        } else if (e instanceof ConnectException
                || e instanceof UnknownHostException) {
            //   连接错误
            onException(CONNECT_ERROR);
        } else if (e instanceof InterruptedIOException) {
            //  连接超时
            onException(CONNECT_TIMEOUT);
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException) {
            //  解析错误
            onException(PARSE_ERROR);
        } else {
            if (e != null) {
                onError(e.toString());
            } else {
                onError("未知错误");
            }
        }

    }

    private void onException(int unknownError) {
        switch (unknownError) {
            case CONNECT_ERROR:
                onNetworkError("连接错误");
                break;

            case CONNECT_TIMEOUT:
                onNetworkError("连接超时");
                break;

            case BAD_NETWORK:
                onNetworkError("网络问题");
                break;

            case PARSE_ERROR:
                onNetworkError("解析数据失败");
                break;

            default:
                break;
        }
    }
    @Override
    public void onComplete() {
        if (view != null) {
            view.hideLoading();
        }

    }
    //成功事件
    public abstract void onSuccess(BaseModel o);
    //失败事件(网络错误)
    public abstract void onNetworkError(String msg);
    //失败事件
    public abstract void onError(String msg);

}
