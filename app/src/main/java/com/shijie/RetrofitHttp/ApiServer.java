package com.shijie.RetrofitHttp;


import com.shijie.base.BaseModel;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * 请求接口
 * Created by haoge on 2018/8/16.
 */

public interface ApiServer {

    /**
     * 根据视频id获取视频请求接口
     * @param videoId
     * @return
     */
    @GET("user/video/{videoId}")
    Observable<BaseModel> getVideoByVId(@Path("videoId") Integer videoId);

    /**
     * 根据视频id获取评论请求接口
     * @param videoId
     * @return
     */
    @GET("comments/{videoId}")
    Observable<BaseModel> getCommentsByVId(@Path("videoId") Integer videoId);

    /**
     * 登录接口
     * @param name
     * @param pwd
     * @return
     */
    @POST("login")
    Observable<BaseModel> LoginByRx(@Query("username") String name, @Query("password") String pwd);

    /**
     * 修改用户资料请求接口
     * @param multipartBody
     * @return
     */
    @POST("modifyUser")
    Observable<BaseModel> modifyUser(@Body MultipartBody multipartBody);

    /**
     * 获取首页视频请求接口
     * @param pageSize
     * @return
     */
    @GET("app/homepage/{pagesize}")
    Observable<BaseModel> getHomePage(@Path("pagesize")int pageSize);
}
