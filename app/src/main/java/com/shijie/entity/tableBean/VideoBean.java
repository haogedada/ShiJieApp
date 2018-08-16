package com.shijie.entity.tableBean;


import java.sql.Timestamp;

public class VideoBean {
    private String videoTitle, videoContent, videoUrl, videoCoverUrl, videoTime, videoType;
    private Integer videoId, userId, playerCount, videoTipNum, videoTrampleNum;
    private Timestamp videoCreatTime;
    private UserBean userBean;

    public VideoBean() {

    }

    public VideoBean(String videoTitle, String videoContent, String videoUrl, String videoCoverUrl, String videoTime, String videoType, Integer videoId, Integer userId, Integer playerCount, Integer videoTipNum, Integer videoTrampleNum, Timestamp videoCreatTime, UserBean userBean) {
        this.videoTitle = videoTitle;
        this.videoContent = videoContent;
        this.videoUrl = videoUrl;
        this.videoCoverUrl = videoCoverUrl;
        this.videoTime = videoTime;
        this.videoType = videoType;
        this.videoId = videoId;
        this.userId = userId;
        this.playerCount = playerCount;
        this.videoTipNum = videoTipNum;
        this.videoTrampleNum = videoTrampleNum;
        this.videoCreatTime = videoCreatTime;
        this.userBean = userBean;
    }

    public String getVideoTitle() {
        return videoTitle;
    }

    public void setVideoTitle(String videoTitle) {
        this.videoTitle = videoTitle;
    }

    public String getVideoContent() {
        return videoContent;
    }

    public void setVideoContent(String videoContent) {
        this.videoContent = videoContent;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public Timestamp getVideoCreatTime() {
        return videoCreatTime;
    }

    public void setVideoCreatTime(Timestamp videoCreatTime) {
        this.videoCreatTime = videoCreatTime;
    }

    public Integer getVideoId() {
        return videoId;
    }

    public void setVideoId(Integer videoId) {
        this.videoId = videoId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getPlayerCount() {
        return playerCount;
    }

    public void setPlayerCount(Integer playerCount) {
        this.playerCount = playerCount;
    }

    public Integer getVideoTipNum() {
        return videoTipNum;
    }

    public void setVideoTipNum(Integer videoTipNum) {
        this.videoTipNum = videoTipNum;
    }

    public Integer getVideoTrampleNum() {
        return videoTrampleNum;
    }

    public void setVideoTrampleNum(Integer videoTrampleNum) {
        this.videoTrampleNum = videoTrampleNum;
    }

    public UserBean getUserBean() {
        return userBean;
    }

    public void setUserBean(UserBean userBean) {
        userBean.setUserPassword("");
        userBean.setRegisterTime(null);
        userBean.setUserEmail("");
        this.userBean = userBean;
    }

    public String getVideoCoverUrl() {
        return videoCoverUrl;
    }

    public void setVideoCoverUrl(String videoCoverUrl) {
        this.videoCoverUrl = videoCoverUrl;
    }

    public String getVideoTime() {
        return videoTime;
    }

    public void setVideoTime(String videoTime) {
        this.videoTime = videoTime;
    }

    public String getVideoType() {
        return videoType;
    }

    public void setVideoType(String videoType) {
        this.videoType = videoType;
    }
}
