package com.axzl.mobile.refueling.app.observer;

import com.aispeech.dui.dds.DDS;
import com.aispeech.dui.dds.agent.MessageObserver;
import com.aispeech.dui.dds.exceptions.DDSNotInitCompleteException;
import com.aispeech.dui.dds.update.DDSUpdateListener;

import timber.log.Timber;

/**
 * 更新Observer,用于更新当前dds组件
 */
public class DuiUpdateObserver implements MessageObserver {
    private static final String Tag = "DuiUpdateObserver";
    public static final int START = 0; // 开始更新
    public static final int UPDATEING = 1; // 正在更新
    public static final int FINISH = 2;// 更新完成
    public static final int ERROR = 3;// 更新失败

    private UpdateCallback mUpdateCallback;

    // 更新回调
    public interface UpdateCallback {
        void onUpdate(int type, String result);
    }

    // 注册当前更新消息
    public void regist(UpdateCallback updateCallback) {
        mUpdateCallback = updateCallback;
        DDS.getInstance().getAgent().subscribe("sys.resource.updated", this);
        initUpdate();
    }

    // 注销当前更新消息
    public void unregist() {
        this.mUpdateCallback = null;
        this.ddsUpdateListener = null;
        DDS.getInstance().getAgent().unSubscribe(this);
    }

    // 初始化更新
    private void initUpdate() {
        try {
            Timber.i(Tag + " initUpdate");
            DDS.getInstance().getUpdater().update(ddsUpdateListener);
        } catch (DDSNotInitCompleteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMessage(String s, String s1) {
        Timber.i(Tag + " onMessage s=" + s + " s1=" + s1);
        initUpdate();
    }

    private DDSUpdateListener ddsUpdateListener = new DDSUpdateListener() {
        @Override
        public void onUpdateFound(String detail) {
            try {
                Timber.i(Tag + " onUpdateFound detail=" + detail);
                if (mUpdateCallback != null) {
                    mUpdateCallback.onUpdate(START, "发现新版本");
                }
                DDS.getInstance().getAgent().getTTSEngine().speak("发现新版本,正在为您更新", 1);
            } catch (DDSNotInitCompleteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpdateFinish() {
            Timber.i(Tag + " onUpdateFinish");
            if (mUpdateCallback != null) {
                mUpdateCallback.onUpdate(FINISH, "更新成功");
            }
            // 更新成功后不要立即调用speak提示用户更新成功, 这个时间DDS正在初始化
        }

        @Override
        public void onDownloadProgress(float progress) {
            Timber.i(Tag + " onDownloadProgress progress=" + progress);
            if (mUpdateCallback != null) {
                mUpdateCallback.onUpdate(UPDATEING, "正在更新 -> " + progress + " / 100");
            }
        }

        @Override
        public void onError(int what, String error) {
            if (mUpdateCallback != null) {
                mUpdateCallback.onUpdate(ERROR, "更新失败,详情看Log");
            }
            Timber.e(Tag + "what = " + what + ", error = " + error);
        }

        @Override
        public void onUpgrade(String version) {
            Timber.i(Tag + " onUpgrade version=" + version);
            if (mUpdateCallback != null) {
                mUpdateCallback.onUpdate(ERROR, "更新失败 -> 当前sdk版本过低，和dui平台上的dui内核不匹配，请更新sdk");
            }
        }
    };
}
