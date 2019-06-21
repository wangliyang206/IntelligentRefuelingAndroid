package com.axzl.mobile.refueling.app.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.axzl.mobile.refueling.BuildConfig;
import com.axzl.mobile.refueling.mvp.ui.activity.SplashActivity;


/**
 * 开机自启
 */
public class BootUpReceiver extends BroadcastReceiver {

    public BootUpReceiver() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (BuildConfig.IS_INDEPENDENCE)
            if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
                Intent i = new Intent(context, SplashActivity.class);
                // 非常重要，如果缺少的话，程序将在启动时报错
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
    }
}
