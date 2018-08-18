package com.shijie.RetrofitHttp;


import com.shijie.base.BaseModel;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * 请求接口
 * Created by haoge on 2018/8/16.
 */

public interface ApiServer {

    /**
     * 根据视频id获取视频
     * @param videoId
     * @return
     */
    @GET("user/video/{videoId}")
    Observable<BaseModel> getVideoByVId(@Path("videoId") Integer videoId);

    /**
     * 登录接口
     * @param name
     * @param pwd
     * @return
     */
    @POST("login")
    Observable<BaseModel> LoginByRx(@Query("username") String name, @Query("password") String pwd);

    /**
     * 修改用户资料
     * @param nickname 用户昵称
     * @param sex 用户性别
     * @param birthday 用户生日
     * @param sign 用户签名
     * @param imgfile 头像文件
     * @return
     */
    @PUT("user")
    Observable<BaseModel> modifyUser(@Query("nickname") String nickname, @Query("sex") String sex,
                                     @Query("birthday") String birthday, @Query("sign") String sign,
                                     @Part("imgfile") List<MultipartBody.Part> imgfile);
}
