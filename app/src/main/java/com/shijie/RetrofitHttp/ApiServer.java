package com.shijie.RetrofitHttp;


import com.shijie.base.BaseModel;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * 请求接口
 * Created by haoge on 2018/8/16.
 */

public interface ApiServer {

    @GET("user/video/{videoId}")
    Observable<BaseModel> getVideoBean(@Path("videoId") Integer videoId);

    @POST("login")
    Observable<BaseModel> LoginByRx(@Query("username") String name, @Query("password") String pwd);
}
