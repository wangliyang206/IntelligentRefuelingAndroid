package com.axzl.mobile.refueling.mvp.presenter;

import android.app.Application;
import android.support.v7.widget.RecyclerView;

import com.axzl.mobile.refueling.mvp.contract.FontAwesomeContract;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.mvp.BasePresenter;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 08/12/2019 11:52
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
public class FontAwesomePresenter extends BasePresenter<FontAwesomeContract.Model, FontAwesomeContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;
    @Inject
    RecyclerView.Adapter mAdapter;
    @Inject
    List<FontAwesome.Icon> mIconList;

    @Inject
    public FontAwesomePresenter(FontAwesomeContract.Model model, FontAwesomeContract.View rootView) {
        super(model, rootView);
    }

    public void initPaginate() {
        mIconList.addAll(Arrays.asList(FontAwesome.Icon.values()));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;
        this.mAdapter = null;
        this.mIconList = null;
    }
}
