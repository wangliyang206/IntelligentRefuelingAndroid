package com.axzl.mobile.refueling.di.module;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.axzl.mobile.refueling.mvp.ui.adapter.FontAwesomeAdapter;
import com.jess.arms.di.scope.ActivityScope;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

import com.axzl.mobile.refueling.mvp.contract.FontAwesomeContract;
import com.axzl.mobile.refueling.mvp.model.FontAwesomeModel;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;

import java.util.ArrayList;
import java.util.List;


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
@Module
public abstract class FontAwesomeModule {

    @Binds
    abstract FontAwesomeContract.Model bindFontAwesomeModel(FontAwesomeModel model);

    @ActivityScope
    @Provides
    static RecyclerView.LayoutManager provideLayoutManager(FontAwesomeContract.View view) {
        return new GridLayoutManager(view.getActivity(), 4);
    }

    @ActivityScope
    @Provides
    static List<FontAwesome.Icon> provideUserList() {
        return new ArrayList<>();
    }

    @ActivityScope
    @Provides
    static RecyclerView.Adapter provideUserAdapter(List<FontAwesome.Icon> list){
        return new FontAwesomeAdapter(list);
    }
}