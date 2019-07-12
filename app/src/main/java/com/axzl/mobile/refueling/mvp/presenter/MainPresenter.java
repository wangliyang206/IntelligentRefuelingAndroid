package com.axzl.mobile.refueling.mvp.presenter;

import android.app.Application;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.OnLifecycleEvent;

import com.axzl.mobile.refueling.app.global.AccountManager;
import com.axzl.mobile.refueling.app.service.LocationService;
import com.axzl.mobile.refueling.app.service.MyLocationListener;
import com.axzl.mobile.refueling.mvp.contract.MainContract;
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
 * Created by MVPArmsTemplate on 05/31/2019 10:34
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
public class MainPresenter extends BasePresenter<MainContract.Model, MainContract.View> {
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
    public MainPresenter(MainContract.Model model, MainContract.View rootView) {
        super(model, rootView);
    }

    public void initPresenter() {
        Timber.i("###宽度=" + ScreenUtils.getScreenWidth() + "高度=" + ScreenUtils.getScreenHeight());

        // 开启定位
        locationService = new LocationService(mApplication.getApplicationContext());
        //定位回调监听
        locationService.registerListener(new MyLocationListener(mApplication.getApplicationContext()));

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    void onResume() {
        this.locationService.onStart();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    void onPause() {
        this.locationService.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;
        this.mAccountManager = null;

        this.locationService.onDestroy();
        this.locationService = null;
    }
}
