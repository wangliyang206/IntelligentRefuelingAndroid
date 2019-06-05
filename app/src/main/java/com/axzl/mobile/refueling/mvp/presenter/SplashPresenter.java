package com.axzl.mobile.refueling.mvp.presenter;

import android.app.Application;

import com.axzl.mobile.refueling.BuildConfig;
import com.axzl.mobile.refueling.app.global.AccountManager;
import com.axzl.mobile.refueling.app.global.Constant;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.ThreadUtils;
import com.jess.arms.integration.AppManager;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.http.imageloader.ImageLoader;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;

import javax.inject.Inject;

import com.axzl.mobile.refueling.mvp.contract.SplashContract;


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

        // 验证Token
//        validToken();
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;
    }
}
