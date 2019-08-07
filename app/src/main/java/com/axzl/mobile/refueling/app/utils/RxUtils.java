/*
 * Copyright 2017 JessYan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.axzl.mobile.refueling.app.utils;

import com.axzl.mobile.refueling.app.config.CommonRetryWithDelay;
import com.jess.arms.mvp.IView;
import com.jess.arms.utils.RxLifecycleUtils;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.RxLifecycle;

import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * ================================================
 * 放置便于使用 RxJava 的一些工具方法
 * <p>
 * Created by JessYan on 11/10/2016 16:39
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * ================================================
 */
public class RxUtils {

    private RxUtils() {
    }

    public static <T> ObservableTransformer<T, T> applySchedulers(final IView view) {
        return applySchedulers(view, false, false);
    }

    /**
     * 通用配置(1、设置切换线程；2、设置loading显隐；3、View 同步生命周期；4、错误时重试机制；)
     *
     * @param view        视图
     * @param isShowEmpty 请求时是否隐藏loading(false 代表 执行显示Loading，true 代表 不执行显示Loading)
     * @param isHideEmpty 结束时是否可控制loading(false 代表 执行隐藏Loading，true 代表 不执行隐藏Loading)
     * @param <T>         Observable<T>
     * @return 返回Observable<T>
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

    /**
     * 此方法已废弃
     *
     * @param view
     * @param <T>
     * @return
     * @see RxLifecycleUtils 此类可以实现 {@link RxLifecycle} 的所有功能, 使用方法和之前一致
     * @deprecated Use {@link RxLifecycleUtils#bindToLifecycle(IView)} instead
     */
    @Deprecated
    public static <T> LifecycleTransformer<T> bindToLifecycle(IView view) {
        return RxLifecycleUtils.bindToLifecycle(view);
    }

}
