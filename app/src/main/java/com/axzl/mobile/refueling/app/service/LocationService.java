package com.axzl.mobile.refueling.app.service;

import android.content.Context;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

import timber.log.Timber;

/** 高德定位 */
public class LocationService {

    private Object objLock = new Object();
    /** 声明mlocationClient对象 */
    private AMapLocationClient mlocationClient;
    /** 声明mLocationOption对象 */
    private AMapLocationClientOption mLocationOption,DIYoption = null;

    /***
     * 构造方法
     *
     * @param locationContext 上下文(句柄)
     */
    public LocationService(Context locationContext) {
        synchronized (objLock) {
            if (mlocationClient == null) {
                mlocationClient = new AMapLocationClient(locationContext);
                mlocationClient.setLocationOption(getDefaultLocationClientOption());
            }
        }
    }

    /***
     * 注册监听，监听定位结果回调
     *
     * @param listener 定位结果回调
     * @return
     */
    public boolean registerListener(AMapLocationListener listener) {
        boolean isSuccess = false;
        if (listener != null) {
            mlocationClient.setLocationListener(listener);
            isSuccess = true;
        }
        return isSuccess;
    }

    /**
     * 注销掉监听
     *
     * @param listener
     */
    public void unregisterListener(AMapLocationListener listener) {
        if (listener != null) {
            mlocationClient.unRegisterLocationListener(listener);
        }
    }

    /***
     * 配置定位信息
     *
     * @param option 定位信息
     * @return isSuccessSetOption
     */
    public boolean setLocationOption(AMapLocationClientOption option) {
        boolean isSuccess = false;
        if (option != null) {
            if (mlocationClient.isStarted())
                mlocationClient.stopLocation();
            DIYoption = option;
            mlocationClient.setLocationOption(option);
        }
        return isSuccess;
    }

    /**
     * 对外提供定位配置信息
     *
     * @return
     */
    public AMapLocationClientOption getOption() {
        return DIYoption;
    }

    /***
     * 默认配置信息
     *
     * @return DefaultLocationClientOption
     */
    public AMapLocationClientOption getDefaultLocationClientOption() {
        if(mLocationOption == null){
            //初始化定位参数
            mLocationOption = new AMapLocationClientOption();
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
            mLocationOption.setGpsFirst(false);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
            mLocationOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
            mLocationOption.setInterval(2000);//可选，设置定位间隔。默认为2秒
            mLocationOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
            mLocationOption.setOnceLocation(false);//可选，设置是否单次定位。默认是false
            mLocationOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
            AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
            mLocationOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
            mLocationOption.setWifiScan(true); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
            mLocationOption.setLocationCacheEnable(true); //可选，设置是否使用缓存定位，默认为true
            mLocationOption.setGeoLanguage(AMapLocationClientOption.GeoLanguage.DEFAULT);//可选，设置逆地理信息的语言，默认值为默认语言（根据所在地区选择语言）
        }
        return mLocationOption;
    }

    /**
     * 开启定位
     */
    public void onStart() {
        synchronized (objLock) {
            if (mlocationClient != null) {
                mlocationClient.startLocation();
            }
        }
    }

    /**
     * 关闭定位
     */
    public void onStop() {
        synchronized (objLock) {
            if (mlocationClient != null) {
                mlocationClient.stopLocation();
            }
        }
    }

    /** 销毁 */
    public void onDestroy() {
        synchronized (objLock) {
            if (mlocationClient != null){
                mlocationClient.onDestroy();
            }
        }
    }
}
