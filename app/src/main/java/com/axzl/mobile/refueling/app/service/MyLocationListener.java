package com.axzl.mobile.refueling.app.service;

import android.content.Context;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.axzl.mobile.refueling.app.global.AccountManager;

import timber.log.Timber;

/**
 * 包名： com.zqw.mobile.recycling.service
 * 对象名： MyLocationListener
 * 描述：定位结果
 * 作者： wly
 * 邮箱：wangliyang206@163.com
 * 创建日期： 2017/8/18 15:44
 */

public class MyLocationListener implements AMapLocationListener {
    private static final String TAG = "MyLocationListener";
    private Context context = null;

    public MyLocationListener(Context context) {
        this.context = context;
    }

    /**
     * 更新内容
     */
    private void updateData(AMapLocation location) {
        AccountManager accountManager = new AccountManager(context);
        //更新信息
        accountManager.updateLocation(location.getLatitude(), location.getLongitude(), location.getAddress());
    }

    @Override
    public void onLocationChanged(AMapLocation location) {
        if (location != null) {
            if (location.getErrorCode() == 0) {
                //可在其中解析amapLocation获取相应内容。
                StringBuffer sb = new StringBuffer(256);
                sb.append("time : ");
                /**
                 * 时间也可以使用systemClock.elapsedRealtime()方法 获取的是自从开机以来，每次回调的时间；
                 * location.getTime() 是指服务端出本次结果的时间，如果位置不发生变化，则时间不变
                 */
                sb.append(location.getTime());
                sb.append("\nlocType : ");// 定位类型
                sb.append(location.getLocationType());
                sb.append("\nlatitude : ");// 纬度
                sb.append(location.getLatitude());
                sb.append("\nlontitude : ");// 经度
                sb.append(location.getLongitude());
                sb.append("\naccuracy : ");// 精度
                sb.append(location.getAccuracy());
                sb.append("\nCountry : ");// 国家名称
                sb.append(location.getCountry());
                sb.append("\nProvince : ");// 省
                sb.append(location.getProvince());
                sb.append("\ncitycode : ");// 城市编码
                sb.append(location.getCityCode());
                sb.append("\ncity : ");// 城市
                sb.append(location.getCity());
                sb.append("\nAdCode : ");// 区域编码
                sb.append(location.getAdCode());
                sb.append("\nDistrict : ");// 区
                sb.append(location.getDistrict());
                sb.append("\nStreet : ");// 街道
                sb.append(location.getStreet());
                sb.append("\nStreetNum : ");// 街道门牌号
                sb.append(location.getStreetNum());
                sb.append("\naddr : ");// 地址信息
                sb.append(location.getAddress());
                sb.append("\nPoi: ");// 当前位置POI名称
                sb.append(location.getPoiName());
                sb.append("\nAoiName: ");// 当前位置所处AOI名称
                sb.append(location.getAoiName());

                if (location.getLocationType() == 1) {
                    sb.append("gps定位成功");
                    updateData(location);
                } else if (location.getLocationType() == 2) {
                    sb.append("前次定位结果");
                } else if (location.getLocationType() == 4) {
                    sb.append("缓存定位结果");
                } else if (location.getLocationType() == 5) {
                    sb.append("Wifi定位成功");
                    updateData(location);
                } else if (location.getLocationType() == 6) {
                    sb.append("基站定位成功");
                    updateData(location);
                } else if (location.getLocationType() == 8) {
                    sb.append("离线定位结果");
                } else if (location.getLocationType() == 9) {
                    sb.append("最后位置缓存");
                }

                //打印
                Timber.i(TAG + " "+sb.toString());
            } else {
                //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                Timber.e(TAG + " location Error, ErrCode:" + location.getErrorCode() + ", errInfo:" + location.getErrorInfo());
            }
        }
    }
}
