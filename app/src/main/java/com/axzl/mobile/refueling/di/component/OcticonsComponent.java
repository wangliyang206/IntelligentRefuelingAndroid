package com.axzl.mobile.refueling.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.axzl.mobile.refueling.di.module.OcticonsModule;
import com.axzl.mobile.refueling.mvp.contract.OcticonsContract;

import com.jess.arms.di.scope.ActivityScope;
import com.axzl.mobile.refueling.mvp.ui.activity.OcticonsActivity;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 08/15/2019 10:58
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
@Component(modules = OcticonsModule.class, dependencies = AppComponent.class)
public interface OcticonsComponent {
    void inject(OcticonsActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        OcticonsComponent.Builder view(OcticonsContract.View view);

        OcticonsComponent.Builder appComponent(AppComponent appComponent);

        OcticonsComponent build();
    }
}