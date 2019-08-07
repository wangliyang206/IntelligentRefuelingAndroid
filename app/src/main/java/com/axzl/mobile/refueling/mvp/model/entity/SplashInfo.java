package com.axzl.mobile.refueling.mvp.model.entity;

/** 启动页图片 */
public class SplashInfo {
    public SplashInfo() {
    }

    public SplashInfo(String url) {
        this.url = url;
    }

    private String url = "";

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
