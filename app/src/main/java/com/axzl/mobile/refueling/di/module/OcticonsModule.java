package com.axzl.mobile.refueling.di.module;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.axzl.mobile.refueling.mvp.contract.OcticonsContract;
import com.axzl.mobile.refueling.mvp.model.OcticonsModel;
import com.axzl.mobile.refueling.mvp.ui.adapter.OcticonsAdapter;
import com.jess.arms.di.scope.ActivityScope;
import com.mikepenz.octicons_typeface_library.Octicons;

import java.util.ArrayList;
import java.util.List;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;


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
@Module
public abstract class OcticonsModule {

    @Binds
    abstract OcticonsContract.Model bindOcticonsModel(OcticonsModel model);

    @ActivityScope
    @Provides
    static RecyclerView.LayoutManager provideLayoutManager(OcticonsContract.View view) {
        return new GridLayoutManager(view.getActivity(), 4);
    }

    @ActivityScope
    @Provides
    static List<Octicons.Icon> provideUserList() {
        return new ArrayList<>();
    }

    @ActivityScope
    @Provides
    static RecyclerView.Adapter provideUserAdapter(List<Octicons.Icon> list) {
        return new OcticonsAdapter(list);
    }
}