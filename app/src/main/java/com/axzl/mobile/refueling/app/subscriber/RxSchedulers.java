package com.axzl.mobile.refueling.app.subscriber;

import com.axzl.mobile.refueling.app.config.CommonRetryWithDelay;
import com.jess.arms.mvp.IView;
import com.jess.arms.utils.RxLifecycleUtils;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 包名： com.zqw.mobile.aptitude.app.utils
 * 对象名： RxSchedulers
 * 描述：切换线程
 * 作者： wly
 * 邮箱：wangliyang206@163.com
 * 创建日期： 2018/6/11 16:26
 */
public class RxSchedulers {

    public static <T> ObservableTransformer<T, T> applySchedulers(final IView view) {
        return applySchedulers(view, false, false);
    }

    /**
     * 通用配置(1、设置切换线程；2、设置loading显隐；3、View 同步生命周期；4、错误时重试机制；)
     * @param view        视图
     * @param isShowEmpty 请求时是否隐藏loading(false 代表 执行显示Loading，true 代表 不执行显示Loading)
     * @param isHideEmpty 结束时是否可控制loading(false 代表 执行隐藏Loading，true 代表 不执行隐藏Loading)
     * @param <T>         Observable<T>
     * @return            返回Observable<T>
     */
    public static <T> ObservableTransformer<T, T> applySchedulers(final IView view, final boolean isShowEmpty, final boolean isHideEmpty) {
        return upstream ->
                upstream.subscribeOn(Schedulers.io())
                        .retryWhen(new CommonRetryWithDelay(2, 2))    // 遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                        .doOnSubscribe(disposable -> {
                            if (!isShowEmpty)
                                view.showLoading();                                                     // 显示进度条
                        })
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doFinally(() -> {
                            if (!isHideEmpty)
                                view.hideLoading();                                                     // 隐藏进度条
                        }).compose(RxLifecycleUtils.bindToLifecycle(view));

    }

    /** 主线程做操作 */
    public static void doOnUIThread(final IView view,UITask uiTask){
        Observable.just(uiTask)
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifecycleUtils.bindToLifecycle(view))
                .subscribe(uiTask1 -> uiTask1.doOnUI());

    }

    /** io线程做操作 */
    public static void doOnThread(final IView view,ThreadTask threadTask){
        Observable.just(threadTask)
                .observeOn(Schedulers.io())
                .compose(RxLifecycleUtils.bindToLifecycle(view))
                .subscribe(threadTask1 -> threadTask1.doOnThread());
    }

    public interface ThreadTask{
        void doOnThread();
    }

    public interface UITask{
        void doOnUI();
    }
}
