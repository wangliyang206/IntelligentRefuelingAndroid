package com.axzl.mobile.refueling.mvp.presenter;

import android.app.Application;
import android.os.Bundle;

import com.axzl.mobile.refueling.app.utils.chinesecalendar.ChineseCalendar;
import com.axzl.mobile.refueling.mvp.contract.CalculationResultContract;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.mvp.BasePresenter;

import java.util.Date;

import javax.inject.Inject;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 10/15/2019 10:06
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
public class CalculationResultPresenter extends BasePresenter<CalculationResultContract.Model, CalculationResultContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;

    // 需要测算的时间
    private Date mDate;

    @Inject
    public CalculationResultPresenter(CalculationResultContract.Model model, CalculationResultContract.View rootView) {
        super(model, rootView);
    }

    /**
     * 获取传进来的值
     */
    public void getBundleValues(Bundle bundle) {
        if (bundle != null) {
            mDate = (Date) bundle.getSerializable("date");
        }
    }

    /**
     * 开始测算
     */
    public void startCalculation() {
        if (mDate == null) {
            mRootView.showMessage("时间格式不正确，请联系管理员");
            return;
        }

        ChineseCalendar mChineseCalendar = new ChineseCalendar(mDate);
        mRootView.loadData(mChineseCalendar);
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
