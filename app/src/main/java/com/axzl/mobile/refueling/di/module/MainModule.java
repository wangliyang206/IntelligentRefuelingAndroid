package com.axzl.mobile.refueling.di.module;

import com.axzl.mobile.refueling.app.global.AccountManager;
import com.axzl.mobile.refueling.mvp.ui.fragment.FortuneTellingFragment;
import com.axzl.mobile.refueling.mvp.ui.fragment.HomeFragment;
import com.axzl.mobile.refueling.mvp.ui.fragment.SettingFragment;
import com.jess.arms.di.scope.ActivityScope;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

import com.axzl.mobile.refueling.mvp.contract.MainContract;
import com.axzl.mobile.refueling.mvp.model.MainModel;


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
@Module
public abstract class MainModule {

    @Binds
    abstract MainContract.Model bindMainModel(MainModel model);

    @ActivityScope
    @Provides
    static AccountManager provideAccountManager(MainContract.View view) {
        return new AccountManager(view.getActivity());
    }

    @ActivityScope
    @Provides
    static HomeFragment provideHomeFragment() {
        return HomeFragment.newInstance();
    }

    @ActivityScope
    @Provides
    static SettingFragment provideSettingFragment() {
        return SettingFragment.newInstance();
    }

    @ActivityScope
    @Provides
    static FortuneTellingFragment provideFortuneTellingFragment() {
        return FortuneTellingFragment.newInstance();
    }
}