package com.axzl.mobile.refueling.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.axzl.mobile.refueling.di.module.GoogleMaterialModule;
import com.axzl.mobile.refueling.mvp.contract.GoogleMaterialContract;

import com.jess.arms.di.scope.ActivityScope;
import com.axzl.mobile.refueling.mvp.ui.activity.GoogleMaterialActivity;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 08/13/2019 11:13
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
@Component(modules = GoogleMaterialModule.class, dependencies = AppComponent.class)
public interface GoogleMaterialComponent {
    void inject(GoogleMaterialActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        GoogleMaterialComponent.Builder view(GoogleMaterialContract.View view);

        GoogleMaterialComponent.Builder appComponent(AppComponent appComponent);

        GoogleMaterialComponent build();
    }
}