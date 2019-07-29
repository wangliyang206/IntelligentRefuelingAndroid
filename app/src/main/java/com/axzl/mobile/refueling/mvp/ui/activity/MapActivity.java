package com.axzl.mobile.refueling.mvp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.axzl.mobile.refueling.R;
import com.axzl.mobile.refueling.di.component.DaggerMapComponent;
import com.axzl.mobile.refueling.mvp.contract.MapContract;
import com.axzl.mobile.refueling.mvp.presenter.MapPresenter;
import com.blankj.utilcode.util.ActivityUtils;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import butterknife.BindView;
import butterknife.OnClick;
import timber.log.Timber;

import static com.jess.arms.utils.Preconditions.checkNotNull;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 07/29/2019 08:59
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
public class MapActivity extends BaseActivity<MapPresenter> implements MapContract.View, AMap.OnMyLocationChangeListener {
    @BindView(R.id.mavi_mapview)
    MapView mMapView;                                                                               // 地图

    // 地图控制器对象
    private AMap mAmap;
    // 当前中心点位置
    private LatLng mCurrentPosition;

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        this.mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        this.mMapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.mCurrentPosition = null;
        this.mAmap = null;
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        if (this.mMapView != null) {
            this.mMapView.onDestroy();
            this.mMapView = null;
        }
    }

    /**
     * 关闭滑动返回
     */
    @Override
    public boolean supportSlideBack() {
        return false;
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerMapComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_map;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        // 创建地图
        initMapView(savedInstanceState);
        setLocation();
        // 初始化
        mPresenter.initPresenter();
    }

    /**
     * 初始化地图
     */
    private void initMapView(@Nullable Bundle savedInstanceState) {
        mMapView.onCreate(savedInstanceState);
        mAmap = mMapView.getMap();
    }

    /**
     * 设置当前位置
     */
    private void setLocation() {
        // 初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        // 连续定位、蓝点不会移动到地图中心点，并且蓝点会跟随设备移动。
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW_NO_CENTER);
        // 调整定位频次 5秒一次
        myLocationStyle.interval(5000);
        // 设置圆形的边框颜色
        myLocationStyle.strokeColor(Color.argb(0, 0, 0, 0));
        // 设置圆形的填充颜色
        myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));
        // 设置中心点图标
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.mipmap.skin3));
        // 设置定位蓝点的Style
        mAmap.setMyLocationStyle(myLocationStyle);
        // 设置隐藏缩放按钮
        mAmap.getUiSettings().setZoomControlsEnabled(false);
        // 设置希望展示的地图缩放级别
        mAmap.moveCamera(CameraUpdateFactory.zoomTo(17));
        // 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        mAmap.setMyLocationEnabled(true);
        // 显示默认的定位按钮(true:显示地图默认右上方圆形定位图标  false:不显示)
        mAmap.getUiSettings().setMyLocationButtonEnabled(false);
        // 监听当前位置改变
        mAmap.setOnMyLocationChangeListener(this);
    }

    @OnClick({
            R.id.btn_open_refueling,                                                                // 快捷加油
            R.id.imvi_maplocation_icon                                                              // 回到当前位置
    })
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_open_refueling:                                                           // 快捷加油
                ActivityUtils.startActivity(SearchForGasStationsActivity.class);
                break;
            case R.id.imvi_maplocation_icon:                                                        // 回到当前位置
                if (mAmap != null) {
                    // 回到当前位置
                    mAmap.animateCamera(CameraUpdateFactory.newLatLngZoom(mCurrentPosition, 17));
                }
                break;
        }
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(@NonNull String message) {
        checkNotNull(message);
//        ArmsUtils.snackbarText(message);
        ArmsUtils.makeText(getApplicationContext(), message);
    }

    @Override
    public void launchActivity(@NonNull Intent intent) {
        checkNotNull(intent);
        ArmsUtils.startActivity(intent);
    }

    @Override
    public void killMyself() {
        finish();
    }

    /**
     * 通过当前位置获取经纬度信息
     */
    @Override
    public void onMyLocationChange(Location location) {
        Timber.i(TAG + " 经度=" + location.getLongitude() + "纬度=" + location.getLatitude());
        // 拿到当前位置
        mCurrentPosition = new LatLng(location.getLatitude(), location.getLongitude());
    }
}

