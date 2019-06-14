package com.axzl.mobile.refueling.app.observer;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.aispeech.dui.dds.DDS;
import com.aispeech.dui.dsk.duiwidget.CommandObserver;

import org.json.JSONObject;

import timber.log.Timber;

/**
 * 客户端CommandObserver, 用于处理客户端动作的执行以及快捷唤醒中的命令响应.
 * 例如在平台配置客户端动作： command://call?phone=$phone$&name=#name#,
 * 那么在CommandObserver的onCall方法中会回调topic为"call", data为
 */
public class DuiCommandObserver implements CommandObserver {
    private String TAG = "DuiCommandObserver";
    private static final String COMMAND_CALL = "sys.action.call";
    private static final String COMMAND_SELECT = "sys.action.call.select";
    private static final String OPEN_WINDOW = "open_window";
    private String mSelectedPhone = null;
    private Context mContent;

    public DuiCommandObserver() {
    }

    // 注册当前更新消息
    public void regist(Context mContent) {
        this.mContent = mContent;
        DDS.getInstance().getAgent().subscribe(new String[]{COMMAND_CALL, COMMAND_SELECT, OPEN_WINDOW},
                this);
    }

    // 注销当前更新消息
    public void unregist() {
        DDS.getInstance().getAgent().unSubscribe(this);
    }

    @Override
    public void onCall(String command, String data) {
        Timber.i(TAG, "command: " + command + "  data: " + data);
        try {
            if (COMMAND_CALL.equals(command)) {
                String number = new JSONObject(data).optString("phone");
                if (number == null) {
                    phoneDial(mSelectedPhone);
                    mSelectedPhone = null;
                } else {
                    phoneDial(number);
                }
            } else if (COMMAND_SELECT.equals(command)) {
                mSelectedPhone = new JSONObject(data).optString("phone");
            } else if (OPEN_WINDOW.equals(command)) {
                JSONObject jsonData = new JSONObject(data);
                String intentName = jsonData.optString("intentName");
                String w = jsonData.optString("w");

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 拨打电话
    private void phoneDial(String number) {
        if (number == null) {
            return;
        }
        Timber.e(TAG, "phoneDial:" + number);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_CALL);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setData(Uri.parse("tel:" + number));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        mContent.startActivity(intent);
    }

}
