package com.axzl.mobile.refueling.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.axzl.mobile.refueling.di.module.MapModule;
import com.axzl.mobile.refueling.mvp.contract.MapContract;

import com.jess.arms.di.scope.ActivityScope;
import com.axzl.mobile.refueling.mvp.ui.activity.MapActivity;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 07/29/2019 08:59
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
@Component(modules = MapModule.class, dependencies = AppComponent.class)
public interface MapComponent {
    void inject(MapActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        MapComponent.Builder view(MapContract.View view);

        MapComponent.Builder appComponent(AppComponent appComponent);

        MapComponent build();
    }
}