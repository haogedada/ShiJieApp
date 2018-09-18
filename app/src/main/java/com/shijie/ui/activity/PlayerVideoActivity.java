package com.shijie.ui.activity;

import android.util.Log;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.shijie.R;
import com.shijie.base.BaseActivity;
import com.shijie.base.BaseModel;
import com.shijie.entity.tableBean.VideoBean;
import com.shijie.mvp.presenter.PlayerVideoPresenter;
import com.shijie.mvp.view.PlayerVideoView;

import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;

import static cn.jzvd.JZVideoPlayer.SCREEN_WINDOW_NORMAL;

public class PlayerVideoActivity extends BaseActivity<PlayerVideoPresenter> implements PlayerVideoView {

    private int videoId;
    private JZVideoPlayerStandard jzVideoPlayerStandard;
    @Override
    protected PlayerVideoPresenter createPresenter() {
        return new PlayerVideoPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_player_video;
    }

    @Override
    protected void initData() {
        videoId = getIntent().getIntExtra("videoId",0);
        presenter.getVideoById(videoId);
    }

    @Override
    protected void initView() {
         jzVideoPlayerStandard = findViewById(R.id.video_player);
    }

    @Override
    public void onNetworkViewRefresh() {
        presenter.getVideoById(videoId);
    }

    @Override
    public void reconnectNetwork() {
    }
    @Override
    public void showView(BaseModel baseModel) {
        String videoStr=new Gson().toJson(baseModel.getData());
        VideoBean videoBean= new Gson().fromJson(videoStr,VideoBean.class);
        Log.e("调试", "showView: "+videoBean.getVideoUrl() );
        jzVideoPlayerStandard.setUp(videoBean.getVideoUrl(),
                SCREEN_WINDOW_NORMAL,videoBean.getVideoTitle());
        Glide.with(this).load(videoBean.getVideoCoverUrl()).into(jzVideoPlayerStandard.thumbImageView);
    }
    @Override
    public void onBackPressed() {
        if (JZVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }
    @Override
    protected void onPause() {
        super.onPause();
        JZVideoPlayer.releaseAllVideos();
    }
}
