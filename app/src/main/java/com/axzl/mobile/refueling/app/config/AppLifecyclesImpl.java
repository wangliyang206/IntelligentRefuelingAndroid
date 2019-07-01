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
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDex;
import android.view.WindowManager;

import com.aispeech.aios.sdk.AIOSForCarSDK;
import com.aispeech.aios.sdk.listener.AIOSReadyListener;
import com.aispeech.aios.sdk.manager.AIOSAudioManager;
import com.aispeech.aios.sdk.manager.AIOSCustomizeManager;
import com.aispeech.aios.sdk.manager.AIOSUIManager;
import com.aitangba.swipeback.ActivityLifecycleHelper;
import com.axzl.mobile.refueling.BuildConfig;
import com.axzl.mobile.refueling.app.global.AccountManager;
import com.axzl.mobile.refueling.app.listener.BridgeAudioListener;
import com.axzl.mobile.refueling.app.listener.CustomizeListener;
import com.axzl.mobile.refueling.app.utils.AppCrashHandler;
import com.axzl.mobile.refueling.app.utils.FileLoggingTree;
import com.blankj.utilcode.util.Utils;
import com.jess.arms.base.delegate.AppLifecycles;
import com.jess.arms.integration.cache.IntelligentCache;
import com.jess.arms.utils.ArmsUtils;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.tencent.bugly.crashreport.CrashReport;

import butterknife.ButterKnife;
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

        //初始化AIOS
        initAIOS(application);

        // 右滑关闭Activity
        application.registerActivityLifecycleCallbacks(ActivityLifecycleHelper.build());

    }

    @Override
    public void onTerminate(@NonNull Application application) {

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
     * 初始化AIOS
     */
    private void initAIOS(Application application) {
        AccountManager accountManager = new AccountManager(application);

        AIOSForCarSDK.initialize(application, new AIOSReadyListener() {
            @Override
            public void onAIOSReady() {
                Timber.i(TAG + " AIOSReadyListener onAIOS Ready");
                //定制录音机
                AIOSCustomizeManager.getInstance().customizeRecorder(
                        accountManager.getIsAecEnabled(false),
                        accountManager.getIsInterruptEnabled(false), false
                );

                //定制主唤醒词
//                List<MajorWakeup> majorWakeups= new ArrayList<MajorWakeup>();
//                majorWakeups.add(new MajorWakeup("你好小驰", "ni hao xiao chi", 0.13f));
//                majorWakeups.add(new MajorWakeup("你好阿星", "ni hao a xing", 0.13f));
//                AIOSCustomizeManager.getInstance().setMajorWakeup(majorWakeups);

                //定制悬浮窗为全屏
                WindowManager.LayoutParams layoutParams = AIOSUIManager.getInstance().obtainLayoutParams();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
                } else {
                    layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
                }
                layoutParams.flags = WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
                AIOSUIManager.getInstance().setLayoutParams(layoutParams);

                //设置音频管理监听器，请实现或者使用以下监听器
                AIOSAudioManager.getInstance().registerAudioListener(new BridgeAudioListener());
                //如果静态注册了自定义命令，请注册以下监听器
                AIOSCustomizeManager.getInstance().registerCustomizeListener(new CustomizeListener(application));
                AIOSCustomizeManager.getInstance().setScanAppEnabled(false);

                AIOSCustomizeManager.getInstance().setWakeupThreshPercent(1.0f);
            }

            /**
             * AIOS（Daemon或者Adapter）重启完成后将会调用该回调
             */
            @Override
            public void onAIOSRebooted() {
                Timber.i(TAG + " AIOSReadyListener onAIOS Rebooted");
                //您可以选择此时跟随AIOS重启
                //也可以在此时重新初始化：除了需要手动调用初始化接口外，还需要还原部分初始化接口外调用的主动接口
                onAIOSReady();
            }
        });
    }
}
