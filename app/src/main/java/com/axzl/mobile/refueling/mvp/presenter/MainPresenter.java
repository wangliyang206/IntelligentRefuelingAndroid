package com.axzl.mobile.refueling.mvp.presenter;

import android.app.Application;

import com.aispeech.aios.sdk.AIOSForCarSDK;
import com.aispeech.aios.sdk.manager.AIOSSystemManager;
import com.axzl.mobile.refueling.app.global.AccountManager;
import com.jess.arms.integration.AppManager;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.http.imageloader.ImageLoader;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;

import javax.inject.Inject;

import com.axzl.mobile.refueling.mvp.contract.MainContract;


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

    @Inject
    public MainPresenter(MainContract.Model model, MainContract.View rootView) {
        super(model, rootView);
    }

    public void initPresenter(){

    }

    /** 是否开启AIOS */
    public void setAIOS(boolean isChecked){
        mAccountManager.setIsAiosSwitch(isChecked);
        if(isChecked){
            // 开启AIOS
            AIOSForCarSDK.enableAIOS();
        }else {
            // 关闭AIOS
            AIOSForCarSDK.disableAIOS();
        }
    }

    /** 是否开启录音 */
    public void setRecorder(boolean isChecked){
        if (isChecked) {
            AIOSSystemManager.getInstance().startRecorder();
        } else {
            AIOSSystemManager.getInstance().stopRecorder();
        }
    }

    /** 手动唤醒AIOS */
    public void startInteraction(){
        AIOSSystemManager.getInstance().startInteraction();
    }

    /** 是否开启ACC */
    public void setACC(boolean isChecked){
        if (isChecked) {
            AIOSSystemManager.getInstance().setACCOn();
        } else {
            AIOSSystemManager.getInstance().setACCOff();
        }
    }

    /** 开启语音唤醒 */
    public void setSwitchVoiceWakeup(boolean isChecked){
        mAccountManager.setIsWakeupSwitch(isChecked);
        AIOSSystemManager.getInstance().setVoiceWakeupEnabled(isChecked);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;
        this.mAccountManager = null;
    }
}
