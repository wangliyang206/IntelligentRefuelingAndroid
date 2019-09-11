package com.axzl.mobile.refueling.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.axzl.mobile.refueling.mvp.ui.fragment.FortuneTellingFragment;
import com.axzl.mobile.refueling.mvp.ui.fragment.HomeFragment;
import com.axzl.mobile.refueling.mvp.ui.fragment.SettingFragment;
import com.jess.arms.di.component.AppComponent;

import com.axzl.mobile.refueling.di.module.MainModule;
import com.axzl.mobile.refueling.mvp.contract.MainContract;

import com.jess.arms.di.scope.ActivityScope;
import com.axzl.mobile.refueling.mvp.ui.activity.MainActivity;


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
@Component(modules = MainModule.class, dependencies = AppComponent.class)
public interface MainComponent {
    void inject(MainActivity activity);
    void inject(HomeFragment fragment);
    void inject(SettingFragment fragment);
    void inject(FortuneTellingFragment fragment);

    @Component.Builder
    interface Builder {
        @BindsInstance
        MainComponent.Builder view(MainContract.View view);

        MainComponent.Builder appComponent(AppComponent appComponent);

        MainComponent build();
    }
}