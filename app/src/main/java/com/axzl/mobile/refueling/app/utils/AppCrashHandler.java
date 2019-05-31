package com.axzl.mobile.refueling.app.utils;

import android.content.Context;
import android.os.Looper;

import com.axzl.mobile.refueling.app.global.AccountManager;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.CleanUtils;
import com.jess.arms.utils.ArmsUtils;

import timber.log.Timber;

/**
 * @Title: AppCrashHandler
 * @Package com.zqw.mobile.recycling.api
 * @Description: 异常捕获类
 * @author: wly
 * @date: 2017/03/13 14:24
 */
public class AppCrashHandler implements Thread.UncaughtExceptionHandler {

    private Thread.UncaughtExceptionHandler mDefaultHandler;
    private static AppCrashHandler INSTANCE = new AppCrashHandler();
    private Context mContext;
    private AccountManager accountManager;

    private AppCrashHandler() {
    }

    public static AppCrashHandler getInstance() {
        return INSTANCE;
    }

    public void init(Context context) {
        mContext = context;

        Thread.UncaughtExceptionHandler exceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        if (exceptionHandler == this)
            return;

        mDefaultHandler = exceptionHandler;
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {

        if (ex != null) {
            // 打印日志
            Timber.i("####异常咯：" + ex.toString());
        }

        if (mDefaultHandler != null && !handleException(ex)) {
            // 如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            AppUtils.relaunchApp();
        }
    }

    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }
        ex.printStackTrace();

        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                ArmsUtils.makeText(mContext, "APP异常；正准备重启！！");
                Looper.loop();
            }
        }.start();

        accountManager = new AccountManager(mContext);

        try {
            // 清除内部缓存
            CleanUtils.cleanInternalCache();
            // 清除外部缓存
            CleanUtils.cleanExternalCache();
            // 清除内部文件
            CleanUtils.cleanInternalFiles();
            // 清除自定义目录下的文件
//            CleanUtils.cleanCustomDir();
            // 清除内存缓存
            accountManager.clearAccountInfo();
        } catch (Exception e) {

        } finally {
            accountManager = null;
        }

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return true;
    }
}
