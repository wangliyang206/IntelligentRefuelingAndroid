package com.axzl.mobile.refueling.mvp.presenter;

import com.aispeech.ailog.AILog;
import com.aispeech.dui.dds.DDS;
import com.aispeech.dui.dds.exceptions.DDSNotInitCompleteException;
import com.axzl.mobile.refueling.BuildConfig;
import com.axzl.mobile.refueling.app.global.AccountManager;
import com.axzl.mobile.refueling.app.global.Constant;
import com.axzl.mobile.refueling.app.utils.RxUtils;
import com.axzl.mobile.refueling.mvp.contract.SplashContract;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.ThreadUtils;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.RxLifecycleUtils;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;


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
    AccountManager accountManager;

    // 授权次数,用来记录自动授权
    private int mAuthCount = 0;

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

        // 开启检查dds初始
        RxUtils.doOnThread(mRootView, () -> checkDDSReady());

    }

    /**
     * 检查dds是否初始成功
     */
    private void checkDDSReady() {
        while (true) {
            if (DDS.getInstance().getInitStatus() == DDS.INIT_COMPLETE_FULL ||
                    DDS.getInstance().getInitStatus() == DDS.INIT_COMPLETE_NOT_FULL) {
                try {
                    if (DDS.getInstance().isAuthSuccess()) {
                        mRootView.jumbToMain();
                        break;
                    } else {
                        // 自动授权
                        doAutoAuth();
                    }
                } catch (DDSNotInitCompleteException e) {
                    e.printStackTrace();
                }
                break;
            } else {
                AILog.w(TAG, "waiting  init complete finish...");
            }
            try {
                Thread.sleep(800);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 执行自动授权
     */
    public void doAutoAuth() {
        // 自动执行授权5次,如果5次授权失败之后,给用户弹提示框
        if (mAuthCount < 5) {
            try {
                DDS.getInstance().doAuth();
                mAuthCount++;
            } catch (DDSNotInitCompleteException e) {
                e.printStackTrace();
            }
        } else {
            RxUtils.doOnUIThread(mRootView, () -> mRootView.showDoAuthDialog());
        }
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
     * 倒计时5秒后跳转至主界面
     */
    @Deprecated
    private void jumbToMain() {
        Observable.timer(3, TimeUnit.SECONDS)
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
        this.accountManager = null;
    }
}
