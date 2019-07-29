package com.axzl.mobile.refueling.mvp.presenter;

import android.app.Application;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.OnLifecycleEvent;

import com.axzl.mobile.refueling.app.global.AccountManager;
import com.axzl.mobile.refueling.app.service.LocationService;
import com.axzl.mobile.refueling.mvp.contract.MapContract;
import com.blankj.utilcode.util.ScreenUtils;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.mvp.BasePresenter;

import javax.inject.Inject;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import timber.log.Timber;


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
@ActivityScope
public class MapPresenter extends BasePresenter<MapContract.Model, MapContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;
    @Inject
    AccountManager mAccountManager;

    /**
     * 定位对象
     */
    private LocationService locationService;

    @Inject
    public MapPresenter(MapContract.Model model, MapContract.View rootView) {
        super(model, rootView);
    }

    public void initPresenter() {
        Timber.i("###宽度=" + ScreenUtils.getScreenWidth() + "高度=" + ScreenUtils.getScreenHeight());

//        // 开启定位
//        locationService = new LocationService(mApplication.getApplicationContext());
//        //定位回调监听
//        locationService.registerListener(new MyLocationListener(mApplication.getApplicationContext()));

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    void onResume() {
//        this.locationService.onStart();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    void onPause() {
//        this.locationService.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;
        this.mAccountManager = null;

//        this.locationService.onDestroy();
        this.locationService = null;
    }
}
