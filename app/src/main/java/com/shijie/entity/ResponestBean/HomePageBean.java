package com.shijie.entity.ResponestBean;


import java.util.List;

public class HomePageBean {

    private Object homePageHead;
    private List<TypeListBean> videoTypeList;
    private Object tail;

    public HomePageBean() {

    }

    public Object getHomePageHead() {
        return homePageHead;
    }

    public void setHomePageHead(Object homePageHead) {
        this.homePageHead = homePageHead;
    }

    public List<TypeListBean> getVideoTypeList() {
        return videoTypeList;
    }

    public void setVideoTypeList(List<TypeListBean> videoTypeList) {
        this.videoTypeList = videoTypeList;
    }

    public Object getTail() {
        return tail;
    }

    public void setTail(Object tail) {
        this.tail = tail;
    }
}
