package com.axzl.mobile.refueling.app.receiver;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.aispeech.aios.common.config.SDKBroadcast;

import timber.log.Timber;

public class BridgeReceiver extends BroadcastReceiver {
    private static final String TAG = "BridgeReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Timber.i(TAG + " onReceive - " + intent.getAction());

        String action = intent.getAction();
        if (SDKBroadcast.Action.BOOT_PHONE.equals(action)) {
            //模拟打开电话应用，并完成电话监听器的初始化
//            Intent phoneIntent = new Intent(BridgeApplication.getContext(), PhoneActivity.class);
//            phoneIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            BridgeApplication.getContext().startActivity(phoneIntent);

        } else if (SDKBroadcast.Action.BOOT_MUSIC.equals(action)) {
            //模拟打开音乐应用，并完成音乐监听器的初始化
//            Intent musicIntent = new Intent(BridgeApplication.getContext(), MusicActivity.class);
//            musicIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            BridgeApplication.getContext().startActivity(musicIntent);

        } else if (SDKBroadcast.Action.BOOT_MAP.equals(action)) {
            //模拟打开地图应用，并完成地图监听器的初始化
//            Intent mapIntent = new Intent(BridgeApplication.getContext(), MapActivity.class);
//            mapIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            BridgeApplication.getContext().startActivity(mapIntent);

        } else if (SDKBroadcast.Action.BOOT_FM.equals(action)) {
            //模拟打开电台应用,并完成电台监听器的初始化
//            Intent fmIntent = new Intent(BridgeApplication.getContext(), FMActivity.class);
//            fmIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            BridgeApplication.getContext().startActivity(fmIntent);

        }
    }
}
