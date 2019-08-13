package com.axzl.mobile.refueling.mvp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.axzl.mobile.refueling.R;
import com.axzl.mobile.refueling.di.component.DaggerSearchForGasStationsComponent;
import com.axzl.mobile.refueling.mvp.contract.SearchForGasStationsContract;
import com.axzl.mobile.refueling.mvp.presenter.SearchForGasStationsPresenter;
import com.axzl.mobile.refueling.mvp.ui.adapter.GasStationsAdapter;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.paginate.Paginate;

import javax.inject.Inject;

import butterknife.BindView;

import static com.jess.arms.utils.Preconditions.checkNotNull;


/**
 * ================================================
 * Description: 搜索附近加油站
 * <p>
 * Created by MVPArmsTemplate on 06/28/2019 10:36
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
public class SearchForGasStationsActivity extends BaseActivity<SearchForGasStationsPresenter> implements SearchForGasStationsContract.View, SwipeRefreshLayout.OnRefreshListener {

    /*--------------------------------控件信息--------------------------------*/
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;                                                                     // 列表展示控件

    @BindView(R.id.linla_searchforgasstationsactivity_not_data)
    View notData;                                                                                   // 没有相关人员

    /*--------------------------------业务信息--------------------------------*/
    @Inject
    GasStationsAdapter mAdapter;
    @Inject
    RecyclerView.LayoutManager mLayoutManager;

    // 数据分页
    private Paginate mPaginate;
    // 是否加载更多
    private boolean isLoadingMore;
    private boolean hasLoadedAllItems;

    @Override
    protected void onDestroy() {
        //super.onDestroy()之后会unbind,所有view被置为null,所以必须在之前调用
        DefaultAdapter.releaseAllHolder(mRecyclerView);
        super.onDestroy();
        this.mAdapter = null;
        this.mLayoutManager = null;
        this.mPaginate = null;
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerSearchForGasStationsComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_search_for_gas_stations;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        // 初始化控件
        initRecyclerView();

        // 初始化分页
        initPaginate();
    }

    /**
     * 初始化RecyclerView
     */
    private void initRecyclerView() {
        mSwipeRefreshLayout.setOnRefreshListener(this);
        ArmsUtils.configRecyclerView(mRecyclerView, mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    /**
     * 初始化Paginate,用于加载更多
     */
    private void initPaginate() {
        if (mPaginate == null) {
            Paginate.Callbacks callbacks = new Paginate.Callbacks() {
                @Override
                public void onLoadMore() {
                    mPresenter.requestUsers(false);
                }

                @Override
                public boolean isLoading() {
                    return isLoadingMore;
                }

                @Override
                public boolean hasLoadedAllItems() {
                    return hasLoadedAllItems;
                }
            };

            mPaginate = Paginate.with(mRecyclerView, callbacks)
                    .setLoadingTriggerThreshold(0)
                    .build();
            mPaginate.setHasMoreDataToLoad(false);
        }
    }

    /**
     * 刷新
     */
    @Override
    public void onRefresh() {
        mPresenter.requestUsers(true);
    }

    /**
     * 开始加载更多
     */
    @Override
    public void startLoadMore() {
        isLoadingMore = true;
    }

    /**
     * 结束加载更多
     */
    @Override
    public void endLoadMore() {
        isLoadingMore = false;
    }

    @Override
    public void hasLoadedAllItems(boolean val) {
        hasLoadedAllItems = val;
    }

    @Override
    public void noInfo() {
        // 显示没有内容布局
        notData.setVisibility(View.VISIBLE);
        // 不加载更多
        hasLoadedAllItems(true);
    }

    @Override
    public void showLoading() {
        mSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        mSwipeRefreshLayout.setRefreshing(false);
    }


    @Override
    public Activity getActivity() {
        return this;
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
