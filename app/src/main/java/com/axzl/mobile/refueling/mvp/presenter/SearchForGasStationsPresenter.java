package com.axzl.mobile.refueling.mvp.presenter;

import android.app.Application;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.OnLifecycleEvent;
import android.support.v4.app.Fragment;
import android.support.v4.app.SupportActivity;

import com.axzl.mobile.refueling.app.global.AccountManager;
import com.axzl.mobile.refueling.app.global.Constant;
import com.axzl.mobile.refueling.mvp.contract.SearchForGasStationsContract;
import com.axzl.mobile.refueling.mvp.model.entity.GasStations;
import com.axzl.mobile.refueling.mvp.model.entity.GasStationsResponse;
import com.axzl.mobile.refueling.mvp.ui.adapter.GasStationsAdapter;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.RxLifecycleUtils;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import me.jessyan.rxerrorhandler.handler.RetryWithDelay;

import static com.axzl.mobile.refueling.BuildConfig.IS_DEBUG_DATA;


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
public class SearchForGasStationsPresenter extends BasePresenter<SearchForGasStationsContract.Model, SearchForGasStationsContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;
    @Inject
    List<GasStations> mGasStationsList;
    @Inject
    GasStationsAdapter mAdapter;
    @Inject
    AccountManager mAccountManager;

    // 当前页数
    private int lastUserId = 1;
    private int preEndIndex;

    @Inject
    public SearchForGasStationsPresenter(SearchForGasStationsContract.Model model, SearchForGasStationsContract.View rootView) {
        super(model, rootView);
    }

    /**
     * 使用 2017 Google IO 发布的 Architecture Components 中的 Lifecycles 的新特性 (此特性已被加入 Support library)
     * 使 {@code Presenter} 可以与 {@link SupportActivity} 和 {@link Fragment} 的部分生命周期绑定
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    void onCreate() {
        //打开 App 时自动加载列表
        requestUsers(true);
    }

    public void requestUsers(boolean pullToRefresh) {
        // 下拉刷新默认只请求第一页
        if (pullToRefresh)
            lastUserId = 1;

        if (IS_DEBUG_DATA) {
            if (pullToRefresh)
                //如果是下拉刷新则清空列表
                mGasStationsList.clear();

            if (pullToRefresh)
                //显示下拉刷新的进度条
                mRootView.showLoading();
            else
                //显示上拉加载更多的进度条
                mRootView.startLoadMore();

            mGasStationsList.add(new GasStations(1, "中国石油(石家庄市中油第十加油站)", "裕华区翟营大街303号"));
            mGasStationsList.add(new GasStations(2, "中国石化加油站(裕东站)", "河北省石家庄市裕华区裕华东路152号"));
            mGasStationsList.add(new GasStations(3, "中国石油(和平东路)", "河北省石家庄市长安区和平东路269-281号(阳光宜家家装专业广场E座西)"));
            mGasStationsList.add(new GasStations(4, "中国石化加油站(建设大街站)", "石家庄市建设南大街39号(建设大街与裕华路口往北150米)"));
            mGasStationsList.add(new GasStations(5, "中国石化加油站(槐安中路站)", "槐安路休门街对面"));

            if (pullToRefresh)
                //隐藏下拉刷新的进度条
                mRootView.hideLoading();
            else
                //隐藏上拉加载更多的进度条
                mRootView.endLoadMore();

            //更新之前列表总长度,用于确定加载更多的起始位置
            preEndIndex = mGasStationsList.size();

            if (pullToRefresh)
                mAdapter.notifyDataSetChanged();
            else
                mAdapter.notifyItemRangeInserted(preEndIndex, 3);

            if (mGasStationsList.size() == 0) {
                // 显示“暂无信息”
                mRootView.noInfo();

            }

            if (10 > lastUserId) {
                lastUserId++;
                mRootView.hasLoadedAllItems(false);
            } else
                //禁用加载更多
                mRootView.hasLoadedAllItems(true);
        } else {
            mModel.getSearchForStations(lastUserId, Constant.PAGESIZE)
                    .subscribeOn(Schedulers.io())
                    .retryWhen(new RetryWithDelay(3, 2))                // 遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                    .doOnSubscribe(disposable -> {
                        if (pullToRefresh)
                            // 显示下拉刷新的进度条
                            mRootView.showLoading();
                        else
                            // 显示上拉加载更多的进度条
                            mRootView.startLoadMore();
                    }).subscribeOn(AndroidSchedulers.mainThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doFinally(() -> {
                        if (pullToRefresh)
                            // 隐藏下拉刷新的进度条
                            mRootView.hideLoading();
                        else
                            // 隐藏上拉加载更多的进度条
                            mRootView.endLoadMore();
                    })
                    .compose(RxLifecycleUtils.bindToLifecycle(mRootView))                           // 使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                    .subscribe(new ErrorHandleSubscriber<GasStationsResponse>(mErrorHandler) {
                        @Override
                        public void onNext(GasStationsResponse response) {

                            if (pullToRefresh)
                                // 如果是下拉刷新则清空列表
                                mGasStationsList.clear();

                            //更新之前列表总长度,用于确定加载更多的起始位置
                            preEndIndex = mGasStationsList.size();

                            mGasStationsList.addAll(response.getGasStationsList());

                            if (mGasStationsList.size() == 0) {
                                // 显示“暂无信息”
                                mRootView.noInfo();

                            }

                            if (pullToRefresh)
                                mAdapter.notifyDataSetChanged();
                            else
                                mAdapter.notifyItemRangeInserted(preEndIndex, response.getGasStationsList().size());

                            if (response.getTotalPage() > lastUserId) {
                                lastUserId++;
                                mRootView.hasLoadedAllItems(false);
                            } else
                                //禁用加载更多
                                mRootView.hasLoadedAllItems(true);
                        }
                    });
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;

        this.mAdapter = null;
        this.mGasStationsList = null;
        this.mAccountManager = null;
    }
}
