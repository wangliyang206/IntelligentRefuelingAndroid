package com.axzl.mobile.refueling.app.listener;

import android.content.Context;
import android.support.annotation.NonNull;

import com.aispeech.aios.sdk.listener.AIOSCustomizeListener;
import com.axzl.mobile.refueling.mvp.ui.activity.SearchForGasStationsActivity;
import com.blankj.utilcode.util.ActivityUtils;

import timber.log.Timber;

public class CustomizeListener implements AIOSCustomizeListener {
    private static final String TAG = "CustomizeListener";
    private Context mContext;

    public CustomizeListener(Context var) {
        this.mContext = var;
    }

    /**
     * 打开#我要加油#应用
     */
    private static final String OPEN_APP = "/customize/open/app";

    /** 列表选择 */
    private static final String LIST_SELECT = "/customize/list/select";

    /**
     * 执行注册的自定义命令
     *
     * @param command 自定义命令
     */
    @Override
    public void onCommandEffect(@NonNull String command) {
        Timber.i(TAG + " Command effect: " + command);
        if (OPEN_APP.equals(command)) {
            // 打开 搜索加油站 列表
            ActivityUtils.startActivity(SearchForGasStationsActivity.class);
        }

        if(LIST_SELECT.equals(command)){

        }
    }

    /**
     * 当识别到定制的快捷唤醒词时执行
     *
     * @param pinyin 识别的快捷唤醒拼音
     */
    @Override
    public void onShortcutWakeUp(@NonNull String pinyin) {
        Timber.i(TAG + " Short cut wakeup:" + pinyin);
    }
}
