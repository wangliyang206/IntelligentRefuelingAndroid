package com.axzl.mobile.refueling.mvp.model.entity;

/** 加油站信息 */
public class GasStations {
    public GasStations(int id, String title, String subTitle) {
        this.id = id;
        this.title = title;
        this.subTitle = subTitle;
    }

    private final int id;
    private final String title;
    private final String subTitle;

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getSubTitle() {
        return subTitle;
    }
}
