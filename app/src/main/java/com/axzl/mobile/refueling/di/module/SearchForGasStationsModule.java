package com.axzl.mobile.refueling.di.module;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.axzl.mobile.refueling.app.global.AccountManager;
import com.axzl.mobile.refueling.app.global.RequestMapper;
import com.axzl.mobile.refueling.mvp.contract.SearchForGasStationsContract;
import com.axzl.mobile.refueling.mvp.model.SearchForGasStationsModel;
import com.axzl.mobile.refueling.mvp.model.entity.GasStations;
import com.axzl.mobile.refueling.mvp.ui.adapter.GasStationsAdapter;
import com.jess.arms.cj.ApiOperator;
import com.jess.arms.cj.IRequestMapper;
import com.jess.arms.di.scope.ActivityScope;

import java.util.ArrayList;
import java.util.List;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;


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
@Module
public abstract class SearchForGasStationsModule {

    @Binds
    abstract SearchForGasStationsContract.Model bindSearchForGasStationsModel(SearchForGasStationsModel model);

    @ActivityScope
    @Provides
    static RecyclerView.LayoutManager provideLayoutManager(SearchForGasStationsContract.View view) {
        return new LinearLayoutManager(view.getActivity());
    }

    @ActivityScope
    @Provides
    static AccountManager provideAccountManager(SearchForGasStationsContract.View view) {
        return new AccountManager(view.getActivity());
    }

    @ActivityScope
    @Provides
    static IRequestMapper providerRequestMapper(SearchForGasStationsContract.View view, AccountManager mAccountManager) {
        return new RequestMapper(view.getActivity(), mAccountManager);
    }

    @ActivityScope
    @Provides
    static ApiOperator providerOperator(IRequestMapper requestMapper) {
        return new ApiOperator(requestMapper);
    }

    @ActivityScope
    @Provides
    static List<GasStations> provideMessageList() {
        return new ArrayList<>();
    }

    @ActivityScope
    @Provides
    static GasStationsAdapter provideMessageAdapter(List<GasStations> list) {
        return new GasStationsAdapter(list);
    }
}