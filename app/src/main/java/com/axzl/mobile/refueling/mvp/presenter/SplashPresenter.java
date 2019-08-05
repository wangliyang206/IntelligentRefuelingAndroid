package com.axzl.mobile.refueling.mvp.presenter;

import android.app.Application;
import android.content.ComponentName;
import android.content.pm.PackageManager;

import com.axzl.mobile.refueling.BuildConfig;
import com.axzl.mobile.refueling.app.global.AccountManager;
import com.axzl.mobile.refueling.app.global.Constant;
import com.axzl.mobile.refueling.mvp.contract.SplashContract;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.ThreadUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.RxLifecycleUtils;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import timber.log.Timber;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 06/05/2019 13:57
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
public class SplashPresenter extends BasePresenter<SplashContract.Model, SplashContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;
    @Inject
    AccountManager accountManager;

    @Inject
    public SplashPresenter(SplashContract.Model model, SplashContract.View rootView) {
        super(model, rootView);
    }

    /**
     * 控制业务逻辑
     */
    public void initPresenter() {
        // 创建文件夹
        initFile();

        // 定时清理日志
        initLog();

        // 计算工作日和休息日，切换相应的图标
        initIcon();

        // 倒计时5秒后跳转至主界面
        jumbToMain();
    }


    /**
     * 创建文件夹
     */
    private void initFile() {
        FileUtils.createOrExistsDir(Constant.IMAGE_PATH);                                           // 创建图片目录
        FileUtils.createOrExistsDir(Constant.CACHE_PATH);                                           // 创建缓存目录
        FileUtils.createOrExistsDir(Constant.LOG_PATH);                                             // 创建日志目录
        FileUtils.createOrExistsDir(Constant.APP_UPDATE_PATH);                                      // 创建升级目录
        FileUtils.createOrExistsDir(Constant.VIDEO_PATH);                                           // 设置拍摄视频缓存路径
    }

    /**
     * 定时清理日志
     * ps:正式环境下每启动10次清理一次Log日志。
     */
    private void initLog() {
        if (!BuildConfig.DEBUG) {
            int num = accountManager.getStartTime();
            if (num >= 10) {
                //清理日志
                ThreadUtils.getFixedPool(3).execute(() -> {
                    try {
                        FileUtils.deleteFile(Constant.LOG_PATH + "log.txt");
                        accountManager.setStartTime(0);
                    } catch (Exception e) {
                    }
                });
            } else {
                //不到清理日期，暂时先增加APP启动次数
                accountManager.setStartTime(++num);
            }
        }
    }

    /**
     * 计算工作日和休息日，切换相应的图标
     */
    private void initIcon() {
        // 初始化对象
        PackageManager pm = mApplication.getPackageManager();
        // 获取当前是星期几
        String week = TimeUtils.getChineseWeek(TimeUtils.getNowDate());
        if (week.contains("六") || week.contains("日")) {
            pm.setComponentEnabledSetting(new ComponentName(mRootView.getActivity().getPackageName(),Constant.ICON_DOUBLE_DAY), PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
            pm.setComponentEnabledSetting(new ComponentName(mRootView.getActivity().getPackageName(),Constant.ICON_SINGLE_DAY), PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
        } else {
            pm.setComponentEnabledSetting(new ComponentName(mRootView.getActivity().getPackageName(),Constant.ICON_DOUBLE_DAY), PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
            pm.setComponentEnabledSetting(new ComponentName(mRootView.getActivity().getPackageName(),Constant.ICON_SINGLE_DAY), PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
        }
        Timber.i(" " + week);
    }

    /**
     * 倒计时5秒后跳转至主界面
     */
    private void jumbToMain() {
        Observable.timer(5, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())                                          // 切换线程
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))                               // 使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Long aLong) {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        mRootView.jumbToMain();
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;
    }
}
