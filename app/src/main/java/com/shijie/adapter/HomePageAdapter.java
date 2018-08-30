package com.shijie.adapter;


import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shijie.R;
import com.shijie.entity.ResponestBean.MySection;
import com.shijie.entity.tableBean.VideoBean;

import java.util.List;

/**
 * Created by haoge on 2018/8/23.
 */

public class HomePageAdapter extends BaseSectionQuickAdapter<MySection, BaseViewHolder> {
    public HomePageAdapter(int layoutResId, int sectionHeadResId, List data) {
        super(layoutResId, sectionHeadResId, data);
    }
    @Override
    protected void convertHead(BaseViewHolder helper, MySection item) {
        helper.setText(R.id.header, item.header);
        helper.setVisible(R.id.more, item.isMore());
        helper.addOnClickListener(R.id.more);
    }

    @Override
    protected void convert(BaseViewHolder helper, MySection item) {
        VideoBean video =  item.t;
        ImageView iv = helper.getView(R.id.img_video_cover);
        Glide.with(mContext).load(video.getVideoCoverUrl()).into(iv);
        helper.setText(R.id.tv_video_title, video.getVideoTitle());
        helper.setText(R.id.tv_play_count,"播放量："+video.getPlayerCount());
        helper.setText(R.id.tv_video_time,"时长："+video.getVideoTime());
    }
}
