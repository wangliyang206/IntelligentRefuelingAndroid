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
package com.axzl.mobile.refueling.app.config;

import android.app.Application;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDex;
import android.widget.ImageView;

import com.axzl.mobile.refueling.BuildConfig;
import com.axzl.mobile.refueling.R;
import com.axzl.mobile.refueling.app.global.AccountManager;
import com.axzl.mobile.refueling.app.global.Constant;
import com.axzl.mobile.refueling.app.utils.AppCrashHandler;
import com.axzl.mobile.refueling.app.utils.FileLoggingTree;
import com.blankj.utilcode.util.Utils;
import com.jess.arms.base.delegate.AppLifecycles;
import com.jess.arms.http.imageloader.glide.GlideArms;
import com.jess.arms.integration.cache.IntelligentCache;
import com.jess.arms.utils.ArmsUtils;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerUIUtils;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.tencent.bugly.crashreport.CrashReport;

import butterknife.ButterKnife;
import cn.bmob.v3.Bmob;
import timber.log.Timber;

/**
 * ================================================
 * 展示 {@link AppLifecycles} 的用法
 * <p>
 * Created by JessYan on 04/09/2017 17:12
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * ================================================
 */
public class AppLifecyclesImpl implements AppLifecycles {
    private static final String TAG = "AppLifecyclesImpl";

    @Override
    public void attachBaseContext(@NonNull Context base) {
        //这里比 onCreate 先执行,常用于 MultiDex 初始化,插件化框架的初始化
        MultiDex.install(base);
    }

    @Override
    public void onCreate(@NonNull Application application) {
        if (LeakCanary.isInAnalyzerProcess(application)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }

        // 初始化工具类
        Utils.init(application);

        initTimber();

        if (!BuildConfig.DEBUG) {
            // 初始化异常捕获类(与Bugly初始化千万别搞反了，会造成bugly一直不上传日志到控制台)
            AppCrashHandler.getInstance().init(application);
        }

        initBugly(application);

        initLeakCanary(application);

        // 初始化定位
        initLocation(application);

        // 初始化Bmob
        Bmob.initialize(application, Constant.BMOB_APPLICATION_KEY);

        // 初始化MaterialDrawer
        initMaterialDrawer();

    }

    @Override
    public void onTerminate(@NonNull Application application) {

    }

    /**
     * 初始化MaterialDrawer
     */
    private void initMaterialDrawer() {

        //initialize and create the image loader logic
        DrawerImageLoader.init(new AbstractDrawerImageLoader() {
            @Override
            public void set(ImageView imageView, Uri uri, Drawable placeholder, String tag) {
                GlideArms.with(imageView.getContext()).load(uri).placeholder(placeholder).into(imageView);
            }

            @Override
            public void cancel(ImageView imageView) {
                GlideArms.with(imageView.getContext()).clear(imageView);
            }

            @Override
            public Drawable placeholder(Context ctx, String tag) {
                //define different placeholders for different imageView targets
                //default tags are accessible via the DrawerImageLoader.Tags
                //custom ones can be checked via string. see the CustomUrlBasePrimaryDrawerItem LINE 111
                if (DrawerImageLoader.Tags.PROFILE.name().equals(tag)) {
                    return DrawerUIUtils.getPlaceHolder(ctx);
                } else if (DrawerImageLoader.Tags.ACCOUNT_HEADER.name().equals(tag)) {
                    return new IconicsDrawable(ctx).iconText(" ").backgroundColorRes(com.mikepenz.materialdrawer.R.color.primary).sizeDp(56);
                } else if ("customUrlItem".equals(tag)) {
                    return new IconicsDrawable(ctx).iconText(" ").backgroundColorRes(R.color.md_red_500).sizeDp(56);
                }

                //we use the default one for
                //DrawerImageLoader.Tags.PROFILE_DRAWER_ITEM.name()

                return super.placeholder(ctx, tag);
            }
        });
    }

    /**
     * 初始化log
     */
    private void initTimber() {
        if (BuildConfig.DEBUG) {
            //测试环境
            //Timber 是一个日志框架容器,外部使用统一的Api,内部可以动态的切换成任何日志框架(打印策略)进行日志打印
            //并且支持添加多个日志框架(打印策略),做到外部调用一次 Api,内部却可以做到同时使用多个策略
            //比如添加三个策略,一个打印日志,一个将日志保存本地,一个将日志上传服务器
            Timber.plant(new Timber.DebugTree() {
                @Override
                protected String createStackElementTag(StackTraceElement element) {
                    return super.createStackElementTag(element) + ":" + element.getLineNumber();
                }
            });
            // 如果你想将框架切换为 Logger 来打印日志,请使用下面的代码,如想切换为其他日志框架请根据下面的方式扩展
//                    Logger.addLogAdapter(new AndroidLogAdapter());
//                    Timber.plant(new Timber.DebugTree() {
//                        @Override
//                        protected void log(int priority, String tag, String message, Throwable t) {
//                            Logger.log(priority, tag, message, t);
//                        }
//                    });
            ButterKnife.setDebug(true);

        } else {
            //正式环境
            Timber.plant(new FileLoggingTree());
        }

    }

    /**
     * 初始化Bugly
     */
    private void initBugly(Application application) {
        // 初始化腾讯Bugly SDK
        CrashReport.initCrashReport(application, BuildConfig.BUGLY_APP_ID, BuildConfig.DEBUG);
    }

    /**
     * leakCanary内存泄露检查
     */
    private void initLeakCanary(Application application) {
        //LeakCanary 内存泄露检查
        //使用 IntelligentCache.KEY_KEEP 作为 key 的前缀, 可以使储存的数据永久存储在内存中
        //否则存储在 LRU 算法的存储空间中, 前提是 extras 使用的是 IntelligentCache (框架默认使用)
        ArmsUtils.obtainAppComponentFromContext(application).extras()
                .put(IntelligentCache.getKeyOfKeep(RefWatcher.class.getName())
                        , BuildConfig.USE_CANARY ? LeakCanary.install(application) : RefWatcher.DISABLED);
    }

    /**
     * 初始始化定位
     */
    private void initLocation(Application application) {
        //验证一下经纬度，如果没有经纬度则需要初始一个默认经纬度
        AccountManager accountManager = new AccountManager(application);
        try {
            accountManager.getLongitude();
        } catch (Exception e) {
            accountManager.updateLocation(38.031693, 114.540032, "中国河北省石家庄市裕华区体育南大街227号");
        }
    }
}
