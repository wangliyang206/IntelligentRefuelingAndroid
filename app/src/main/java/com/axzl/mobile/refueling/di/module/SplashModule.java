package com.axzl.mobile.refueling.di.module;

import com.axzl.mobile.refueling.app.global.AccountManager;
import com.jess.arms.di.scope.ActivityScope;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

import com.axzl.mobile.refueling.mvp.contract.SplashContract;
import com.axzl.mobile.refueling.mvp.model.SplashModel;


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
@Module
public abstract class SplashModule {

    @Binds
    abstract SplashContract.Model bindSplashModel(SplashModel model);

    @ActivityScope
    @Provides
    static AccountManager provideAccountManager(SplashContract.View view) {
        return new AccountManager(view.getActivity());
    }
}