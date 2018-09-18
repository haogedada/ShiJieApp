package com.shijie.entity.ResponestBean;


import com.shijie.entity.tableBean.VideoBean;

import java.util.List;

public class TypeListBean {
    private String videoType;

    private List<VideoBean> videos;


    public TypeListBean() {

    }

    public String getVideoType() {
        return videoType;
    }

    public void setVideoType(String videoType) {
        this.videoType = videoType;
    }

    public List<VideoBean> getVideos() {
        return videos;
    }

    public void setVideos(List<VideoBean> videos) {
        this.videos = videos;
    }

}
