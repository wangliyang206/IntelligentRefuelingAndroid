package com.axzl.mobile.refueling.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.axzl.mobile.refueling.di.module.CalculationResultModule;
import com.axzl.mobile.refueling.mvp.contract.CalculationResultContract;

import com.jess.arms.di.scope.ActivityScope;
import com.axzl.mobile.refueling.mvp.ui.activity.CalculationResultActivity;


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
@Component(modules = CalculationResultModule.class, dependencies = AppComponent.class)
public interface CalculationResultComponent {
    void inject(CalculationResultActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        CalculationResultComponent.Builder view(CalculationResultContract.View view);

        CalculationResultComponent.Builder appComponent(AppComponent appComponent);

        CalculationResultComponent build();
    }
}