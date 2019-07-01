package com.axzl.mobile.refueling.mvp.model.entity;

import java.util.List;

/** 获取加油站 响应结构 */
public class GasStationsResponse {
    public GasStationsResponse() {
    }

    public GasStationsResponse(List<GasStations> gasStationsList, int totalNumber, int totalPage) {
        this.gasStationsList = gasStationsList;
        this.totalNumber = totalNumber;
        this.totalPage = totalPage;
    }

    /** 列表 */
    private List<GasStations> gasStationsList;

    /** 总条数 */
    private int totalNumber;
    /** 总页数 */
    private int totalPage;

    public List<GasStations> getGasStationsList() {
        return gasStationsList;
    }

    public void setGasStationsList(List<GasStations> gasStationsList) {
        this.gasStationsList = gasStationsList;
    }

    public int getTotalNumber() {
        return totalNumber;
    }

    public void setTotalNumber(int totalNumber) {
        this.totalNumber = totalNumber;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }
}
