package com.axzl.mobile.refueling.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.axzl.mobile.refueling.di.module.FontAwesomeModule;
import com.axzl.mobile.refueling.mvp.contract.FontAwesomeContract;

import com.jess.arms.di.scope.ActivityScope;
import com.axzl.mobile.refueling.mvp.ui.activity.FontAwesomeActivity;


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
@Component(modules = FontAwesomeModule.class, dependencies = AppComponent.class)
public interface FontAwesomeComponent {
    void inject(FontAwesomeActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        FontAwesomeComponent.Builder view(FontAwesomeContract.View view);

        FontAwesomeComponent.Builder appComponent(AppComponent appComponent);

        FontAwesomeComponent build();
    }
}