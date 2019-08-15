package com.axzl.mobile.refueling.mvp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.axzl.mobile.refueling.app.utils.DividerGridItemDecoration;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import com.axzl.mobile.refueling.di.component.DaggerOcticonsComponent;
import com.axzl.mobile.refueling.mvp.contract.OcticonsContract;
import com.axzl.mobile.refueling.mvp.presenter.OcticonsPresenter;

import com.axzl.mobile.refueling.R;


import javax.inject.Inject;

import butterknife.BindView;

import static com.jess.arms.utils.Preconditions.checkNotNull;


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
public class OcticonsActivity extends BaseActivity<OcticonsPresenter> implements OcticonsContract.View {
    @BindView(R.id.revi_octicons_recyclerview)
    RecyclerView mRecyclerView;

    @BindView(R.id.tobr_octicons_toolbar)
    Toolbar mToolbar;

    @Inject
    RecyclerView.LayoutManager mLayoutManager;

    @Inject
    RecyclerView.Adapter mAdapter;

    @Override
    protected void onDestroy() {
        super.onDestroy();

        this.mAdapter = null;
        this.mLayoutManager = null;
    }
    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerOcticonsComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_octicons;
    }

    @Override
    public int useStatusBarColor() {
        return R.color.colorPrimary;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        // 绑定Toolbar
        setSupportActionBar(mToolbar);
        // 给左上角图标的左边加上一个返回的图标 。对应ActionBar.DISPLAY_HOME_AS_UP
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // 设置标题
        getSupportActionBar().setTitle(R.string.drawer_item_icon_Octicons);

        // 初始化RecyclerView
        ArmsUtils.configRecyclerView(mRecyclerView, mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        // 设置分割线
        mRecyclerView.addItemDecoration(new DividerGridItemDecoration(ArmsUtils.dip2px(getApplicationContext(), 2), getResources().getColor(android.R.color.transparent)));

        // 初始化业务逻辑
        mPresenter.initPaginate();
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(@NonNull String message) {
        checkNotNull(message);
        ArmsUtils.snackbarText(message);
    }

    @Override
    public void launchActivity(@NonNull Intent intent) {
        checkNotNull(intent);
        ArmsUtils.startActivity(intent);
    }

    @Override
    public void killMyself() {
        finish();
    }
}
