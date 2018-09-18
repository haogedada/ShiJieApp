package com.shijie.RetrofitHttp;

import com.shijie.constant.Constants;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Retrofit的封装
 * Created by haoge on 2018/8/16.
 */

public class RetrofitManager {
    private final static String ROOTURL = Constants.requestRootURL.RootURL.getName();
    public static OkHttpClient okHttpClient;
    private static RetrofitManager mRetrofitManager;

    //静态块,获取OkHttpClient对象
    static {
        getOkHttpClient();
    }

    private Retrofit mRetrofit;
    private String baseUrl;

    private RetrofitManager(String baseUrl) {
        this.baseUrl = baseUrl;
        initRetrofit();
    }

    public static synchronized RetrofitManager getInstance() {
        if (mRetrofitManager == null) {
            synchronized (RetrofitManager.class) {
                if (mRetrofitManager == null) {
                    mRetrofitManager = new RetrofitManager(ROOTURL);
                }
            }
        }
        return mRetrofitManager;
    }

    //单例模式获取okhttp
    public static OkHttpClient getOkHttpClient() {
        if (okHttpClient == null) {
            synchronized (OkHttpClient.class) {
                if (okHttpClient == null) {
                    //配置okhttp
                    okHttpClient = new OkHttpClient.Builder()
                            //打印拦截器日志
                            .addNetworkInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                            .connectTimeout(15, TimeUnit.SECONDS)//设置连接超时时间
                            .readTimeout(15, TimeUnit.SECONDS)//设置读取超时时间
                            .writeTimeout(15, TimeUnit.SECONDS)//设置写入超时时间
                            .build();
                }
            }
        }
        return okHttpClient;
    }

    private void initRetrofit() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build();
    }

    /**
     * 创建相应的服务接口
     */
    public <T> T setCreate(Class<T> reqServer) {

        return mRetrofit.create(reqServer);
    }


}
