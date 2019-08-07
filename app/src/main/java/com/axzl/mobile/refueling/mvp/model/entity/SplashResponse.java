package com.axzl.mobile.refueling.mvp.model.entity;

import java.util.List;

/**
 * 启动页图片集
 */
public class SplashResponse {
    public SplashResponse() {
    }

    public SplashResponse(List<SplashInfo> infoList, String mark) {
        this.infoList = infoList;
        this.mark = mark;
    }

    /** 图片集 */
    private List<SplashInfo> infoList;

    /** 标识(请求时需要带上，用于判断数据是否更新) */
    private String mark = "";

    public List<SplashInfo> getInfoList() {
        return infoList;
    }

    public void setInfoList(List<SplashInfo> infoList) {
        this.infoList = infoList;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }
}
