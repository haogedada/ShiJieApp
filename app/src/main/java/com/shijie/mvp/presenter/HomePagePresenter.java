package com.shijie.mvp.presenter;

import com.google.gson.Gson;
import com.shijie.base.BaseModel;
import com.shijie.base.BaseObserver;
import com.shijie.base.BasePresenter;
import com.shijie.entity.ResponestBean.HomePageBean;
import com.shijie.entity.ResponestBean.MySection;
import com.shijie.entity.ResponestBean.TypeListBean;
import com.shijie.entity.tableBean.VideoBean;
import com.shijie.mvp.view.HomePageView;
import com.shijie.utils.StrJudgeUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by haoge on 2018/8/23.
 */

public class HomePagePresenter extends BasePresenter<HomePageView> {

    public HomePagePresenter(HomePageView homePageView) {
        super(homePageView);
    }

    /**
     * 获取首页业务
     * @param pageSize
     */
    public void getHomePage(int pageSize){
        if (!StrJudgeUtil.isCorrectInt(pageSize)){
            baseView.showError("参数错误");
        }
        addDisposable(apiServer.getHomePage(pageSize), new BaseObserver(baseView) {
            @Override
            public void onSuccess(BaseModel model) {
                String data=new Gson().toJson(model.getData());
                HomePageBean homePageBean=new Gson().fromJson(data,HomePageBean.class);
                List<MySection> list = new ArrayList<>();
                for (TypeListBean t:homePageBean.getVideoTypeList()) {
                    list.add(new MySection(true,t.getVideoType(),true));
                  for (VideoBean videoBean:t.getVideos()){
                      list.add(new MySection(videoBean));
                  }
                }
                baseView.showView(list);
                baseView.hideLoading();
            }

            @Override
            public void onNetworkError(String msg) {
                baseView.showNetworkError(msg);
            }

            @Override
            public void onError(String msg) {
                baseView.showError(msg);
            }
        });
    }
}
