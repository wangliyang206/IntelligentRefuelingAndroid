package com.axzl.mobile.refueling.di.component;

import com.axzl.mobile.refueling.di.module.SearchForGasStationsModule;
import com.axzl.mobile.refueling.mvp.contract.SearchForGasStationsContract;
import com.axzl.mobile.refueling.mvp.ui.activity.SearchForGasStationsActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.di.scope.ActivityScope;

import dagger.BindsInstance;
import dagger.Component;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 06/28/2019 10:36
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
@Component(modules = SearchForGasStationsModule.class, dependencies = AppComponent.class)
public interface SearchForGasStationsComponent {

    void inject(SearchForGasStationsActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        SearchForGasStationsComponent.Builder view(SearchForGasStationsContract.View view);

        SearchForGasStationsComponent.Builder appComponent(AppComponent appComponent);

        SearchForGasStationsComponent build();
    }
}