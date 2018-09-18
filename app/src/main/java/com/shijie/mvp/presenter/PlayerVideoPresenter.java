package com.shijie.mvp.presenter;

import com.shijie.base.BaseModel;
import com.shijie.base.BaseObserver;
import com.shijie.base.BasePresenter;
import com.shijie.mvp.view.PlayerVideoView;
import com.shijie.utils.StrJudgeUtil;

/**
 * Created by haoge on 2018/8/30.
 */

public class PlayerVideoPresenter extends BasePresenter<PlayerVideoView> {
    public PlayerVideoPresenter(PlayerVideoView baseView) {
        super(baseView);
    }

    public void getVideoById(int videoId) {
        if (!StrJudgeUtil.isCorrectInt(videoId)) {
            baseView.showError("参数错误");
        }
        addDisposable(apiServer.getVideoByVId(videoId), new BaseObserver(baseView) {
            @Override
            public void onSuccess(BaseModel o) {
                baseView.showView(o);
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
