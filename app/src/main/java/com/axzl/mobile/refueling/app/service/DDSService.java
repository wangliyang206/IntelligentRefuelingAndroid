package com.axzl.mobile.refueling.app.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.text.TextUtils;
import android.widget.Toast;

import com.aispeech.dui.dds.DDS;
import com.aispeech.dui.dds.DDSAuthListener;
import com.aispeech.dui.dds.DDSConfig;
import com.aispeech.dui.dds.DDSInitListener;
import com.aispeech.dui.dds.exceptions.DDSNotInitCompleteException;
import com.axzl.mobile.refueling.BuildConfig;
import com.axzl.mobile.refueling.app.observer.DuiCommandObserver;
import com.axzl.mobile.refueling.app.observer.DuiMessageObserver;
import com.axzl.mobile.refueling.app.observer.DuiNativeApiObserver;
import com.axzl.mobile.refueling.app.observer.DuiUpdateObserver;
import com.axzl.mobile.refueling.app.utils.EventBusTags;
import com.axzl.mobile.refueling.mvp.model.entity.MessageBean;
import com.blankj.utilcode.util.PhoneUtils;

import org.simple.eventbus.EventBus;

import java.util.UUID;

import timber.log.Timber;

/**
 * 参见Android SDK集成文档: https://www.dui.ai/docs/operation/#/ct_common_Andriod_SDK
 */
public class DDSService extends Service implements DDSAuthListener, DDSInitListener, DuiUpdateObserver.UpdateCallback, DuiMessageObserver.MessageCallback {
    public static final String TAG = "DDSService";

    // 消息监听器
    private DuiMessageObserver mMessageObserver;
    // 命令监听器
    private DuiCommandObserver mCommandObserver;
    // 本地方法回调监听器
    private DuiNativeApiObserver mNativeApiObserver;
    // dds更新监听器
    private DuiUpdateObserver mUpdateObserver;

    public DDSService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        init();
        return super.onStartCommand(intent, flags, startId);
    }

    // 初始化dds组件
    private void init() {
        // 消息监听器
        mMessageObserver = new DuiMessageObserver();
        // 命令监听器
        mCommandObserver = new DuiCommandObserver();
        // 本地方法回调监听器
        mNativeApiObserver = new DuiNativeApiObserver(getApplicationContext());
        // dds更新监听器
        mUpdateObserver = new DuiUpdateObserver();

        DDS.getInstance().setDebugMode(2); //在调试时可以打开sdk调试日志，在发布版本时，请关闭
        DDS.getInstance().init(getApplicationContext(), createConfig(), this, this);
    }

    /**
     * dds认证状态监听器,监听auth成功
     */
    @Override
    public void onAuthSuccess() {
        // 第一步，开启各大服务监听器。
        registerListener();

        // 认证成功，通知欢迎界面继续走以下流程。
        Timber.i(TAG + " onAuthSuccess");
        EventBus.getDefault().post("onAuthSuccess", EventBusTags.ddsAuthSuccess);

    }

    /**
     * 注册监听器
     */
    private void registerListener() {
        mUpdateObserver.regist(this);                                               // DDS更新监听器
        mCommandObserver.regist(this);                                                  // DDS命令响应监听器
        mNativeApiObserver.regist();                                                                // DDS资源调用指令
        mMessageObserver.regist(this);                                            // DDS注册消息监听器
    }

    /**
     * dds认证状态监听器,监听auth失败
     */
    @Override
    public void onAuthFailed(final String errId, final String error) {
        Timber.e(TAG + "onAuthFailed: " + errId + ", error:" + error);
        new Handler(Looper.getMainLooper()).post(() -> Toast.makeText(getApplicationContext(),
                "授权错误:" + errId + ":\n" + error + "\n请查看手册处理", Toast.LENGTH_SHORT).show());
        EventBus.getDefault().post("onAuthFailed", EventBusTags.ddsAuthFailed);
    }

    /**
     * dds初始状态监听器,监听init成功
     */
    @Override
    public void onInitComplete(boolean isFull) {
        // isFull参数如果为false，则表示不完全初始化，缺失资源包，您需要等待资源包的更新下载完成或者内置一份资源包;
        // 如果为true，则表示完全初始化，所有功能就绪。
        if (isFull) {
            Timber.i(TAG + " onInitComplete");

            // 第一步，验证是否注册过监听器。
            if (!mCommandObserver.isRegist())
                registerListener();

            // 第二步，打开唤醒
            enableWakeup();

            // 第三步，通知改变UI界面
            EventBus.getDefault().post("onInitComplete", EventBusTags.ddsInitSuccess);
        }
    }

    /**
     * dds初始状态监听器,监听init失败
     */
    @Override
    public void onError(int what, final String msg) {
        Timber.e(TAG + " Init onError: " + what + ", error: " + msg);
        new Handler(Looper.getMainLooper()).post(() -> Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show());
    }

    /**
     * DuiMessageObserver中当前消息的回调
     */
    @Override
    public void onMessageAdd(MessageBean bean) {
        synchronized (this) {
            EventBus.getDefault().post(bean, EventBusTags.mainRefreshAdapter);
        }
    }


    /**
     * DuiMessageObserver中当前消息的回调
     */
    public void onMessagePollLast() {
        synchronized (this) {
            EventBus.getDefault().post("onMessagePollLast", EventBusTags.mainMessagePollLast);
        }
    }

    /**
     * DuiMessageObserver中当前状态的回调
     */
    @Override
    public void onState(String state) {
        EventBus.getDefault().post(state, EventBusTags.mainReceivingStatus);
    }

    /**
     * DuiUpdateObserver的更新dds回调
     */
    @Override
    public void onUpdate(int type, String result) {
        if (type == DuiUpdateObserver.START) {
            EventBus.getDefault().post(false, EventBusTags.mainControlTextView);
        } else if (type == DuiUpdateObserver.FINISH) {
            EventBus.getDefault().post(true, EventBusTags.mainControlTextView);
        }

        EventBus.getDefault().post(result, EventBusTags.mainRefreshTextView);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // 关闭唤醒
        disableWakeup();

        // 停止所有监听器
        mMessageObserver.unregist();
        mUpdateObserver.unregist();
        mCommandObserver.unregist();
        mNativeApiObserver.unregist();

        // 在退出app时将dds组件注销
        DDS.getInstance().release();
    }

    /**
     * 打开唤醒，调用后才能语音唤醒
     */
    private void enableWakeup() {
        try {
            DDS.getInstance().getAgent().getWakeupEngine().enableWakeup();
        } catch (DDSNotInitCompleteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭唤醒, 调用后将无法语音唤醒
     */
    private void disableWakeup() {
        try {
            DDS.getInstance().getAgent().stopDialog();
            DDS.getInstance().getAgent().getWakeupEngine().disableWakeup();
        } catch (DDSNotInitCompleteException e) {
            e.printStackTrace();
        }
    }

    // 创建dds配置信息
    private DDSConfig createConfig() {
        DDSConfig config = new DDSConfig();
        // 基础配置项
        config.addConfig(DDSConfig.K_PRODUCT_ID, "278582628"); // 产品ID -- 必填
        config.addConfig(DDSConfig.K_USER_ID, "aispeech");  // 用户ID -- 必填
        config.addConfig(DDSConfig.K_ALIAS_KEY, "test");   // 产品的发布分支 -- 必填
        config.addConfig(DDSConfig.K_PRODUCT_KEY, "72140d47d46018aed4df62e82b8acebb");// Product Key -- 必填
        config.addConfig(DDSConfig.K_PRODUCT_SECRET, "018c34b28ea9e8d2720e347b2332c936");// Product Secre -- 必填
        if(BuildConfig.DEBUG){
            config.addConfig(DDSConfig.K_API_KEY, "b030497735f51c987f1f77e75d107d7b");  // 产品授权秘钥，服务端生成，用于产品授权 -- 必填
        }else {
            config.addConfig(DDSConfig.K_API_KEY, "f0f9898b5f6a1dbd3b04bf345d107dba");  // 产品授权秘钥，服务端生成，用于产品授权 -- 必填
        }
        config.addConfig(DDSConfig.K_DEVICE_ID, getDeviceId(getApplicationContext()));//填入唯一的deviceId -- 选填

        // 更多高级配置项,请参考文档: https://www.dui.ai/docs/ct_common_Andriod_SDK 中的 --> 四.高级配置项


        // 资源更新配置项
//        config.addConfig(DDSConfig.K_DUICORE_ZIP, "duicore.zip"); // 预置在指定目录下的DUI内核资源包名, 避免在线下载内核消耗流量, 推荐使用
//        config.addConfig(DDSConfig.K_CUSTOM_ZIP, "custom.zip"); // 预置在指定目录下的DUI产品配置资源包名, 避免在线下载产品配置消耗流量, 推荐使用
//        config.addConfig(DDSConfig.K_USE_UPDATE_DUICORE, "false"); //设置为false可以关闭dui内核的热更新功能，可以配合内置dui内核资源使用
//        config.addConfig(DDSConfig.K_USE_UPDATE_NOTIFICATION, "false"); // 是否使用内置的资源更新通知栏

        // 录音配置项
        // config.addConfig(DDSConfig.K_RECORDER_MODE, "internal"); //录音机模式：external（使用外置录音机，需主动调用拾音接口）、internal（使用内置录音机，DDS自动录音）
        // config.addConfig(DDSConfig.K_IS_REVERSE_AUDIO_CHANNEL, "false"); // 录音机通道是否反转，默认不反转
        // config.addConfig(DDSConfig.K_AUDIO_SOURCE, AudioSource.DEFAULT); // 内置录音机数据源类型
        // config.addConfig(DDSConfig.K_AUDIO_BUFFER_SIZE, (16000 * 1 * 16 * 100 / 1000)); // 内置录音机读buffer的大小

        // TTS配置项
        // config.addConfig(DDSConfig.K_STREAM_TYPE, AudioManager.STREAM_MUSIC); // 内置播放器的STREAM类型
        // config.addConfig(DDSConfig.K_TTS_MODE, "internal"); // TTS模式：external（使用外置TTS引擎，需主动注册TTS请求监听器）、internal（使用内置DUI TTS引擎）
        // config.addConfig(DDSConfig.K_CUSTOM_TIPS, "{\"71304\":\"请讲话\",\"71305\":\"不知道你在说什么\",\"71308\":\"咱俩还是聊聊天吧\"}"); // 指定对话错误码的TTS播报。若未指定，则使用产品配置。

        //唤醒配置项
        // config.addConfig(DDSConfig.K_WAKEUP_ROUTER, "dialog"); //唤醒路由：partner（将唤醒结果传递给partner，不会主动进入对话）、dialog（将唤醒结果传递给dui，会主动进入对话）
        // config.addConfig(DDSConfig.K_WAKEUP_BIN, "/sdcard/wakeup.bin"); //商务定制版唤醒资源的路径。如果开发者对唤醒率有更高的要求，请联系商务申请定制唤醒资源。
        // config.addConfig(DDSConfig.K_ONESHOT_MIDTIME, "500");// OneShot配置：
        // config.addConfig(DDSConfig.K_ONESHOT_ENDTIME, "2000");// OneShot配置：

        //识别配置项
        // config.addConfig(DDSConfig.K_ASR_ENABLE_PUNCTUATION, "false"); //识别是否开启标点
        // config.addConfig(DDSConfig.K_ASR_ROUTER, "dialog"); //识别路由：partner（将识别结果传递给partner，不会主动进入语义）、dialog（将识别结果传递给dui，会主动进入语义）
        // config.addConfig(DDSConfig.K_VAD_TIMEOUT, 5000); // VAD静音检测超时时间，默认8000毫秒
        // config.addConfig(DDSConfig.K_ASR_ENABLE_TONE, "true"); // 识别结果的拼音是否带音调
        // config.addConfig(DDSConfig.K_ASR_TIPS, "true"); // 识别完成是否播报提示音
        // config.addConfig(DDSConfig.K_VAD_BIN, "/sdcard/vad.bin"); // 商务定制版VAD资源的路径。如果开发者对VAD有更高的要求，请联系商务申请定制VAD资源。

        // 调试配置项
        // config.addConfig(DDSConfig.K_CACHE_PATH, "/sdcard/cache"); // 调试信息保存路径,如果不设置则保存在默认路径"/sdcard/Android/data/包名/cache"
        // config.addConfig(DDSConfig.K_WAKEUP_DEBUG, "true"); // 用于唤醒音频调试, 开启后在 "/sdcard/Android/data/包名/cache" 目录下会生成唤醒音频
        // config.addConfig(DDSConfig.K_VAD_DEBUG, "true"); // 用于过vad的音频调试, 开启后在 "/sdcard/Android/data/包名/cache" 目录下会生成过vad的音频
        // config.addConfig(DDSConfig.K_ASR_DEBUG, "true"); // 用于识别音频调试, 开启后在 "/sdcard/Android/data/包名/cache" 目录下会生成识别音频
        // config.addConfig(DDSConfig.K_TTS_DEBUG, "true");  // 用于tts音频调试, 开启后在 "/sdcard/Android/data/包名/cache/tts/" 目录下会自动生成tts音频

        // 麦克风阵列配置项
        // config.addConfig(DDSConfig.K_MIC_TYPE, "1"); // 设置硬件采集模组的类型 0：无。默认值。 1：单麦回消 2：线性四麦 3：环形六麦 4：车载双麦 5：家具双麦 6: 环形四麦  7: 新车载双麦
        // config.addConfig(DDSConfig.K_MIC_ARRAY_AEC_CFG, "/data/aec.bin"); // 麦克风阵列aec资源的磁盘绝对路径,需要开发者确保在这个路径下这个资源存在
        // config.addConfig(DDSConfig.K_MIC_ARRAY_BEAMFORMING_CFG, "/data/beamforming.bin"); // 麦克风阵列beamforming资源的磁盘绝对路径，需要开发者确保在这个路径下这个资源存在
        // config.addConfig(DDSConfig.K_MIC_ARRAY_WAKEUP_CFG, "/data/wakeup_cfg.bin"); // 麦克风阵列wakeup配置资源的磁盘绝对路径，需要开发者确保在这个路径下这个资源存在。

        Timber.i(TAG + " config->" + config.toString());
        return config;
    }

    // 获取手机的唯一标识符: deviceId
    private String getDeviceId(Context context) {
        String imei = "";
        try {
            imei = PhoneUtils.getDeviceId();
        } catch (Exception e) {
            try {
                imei = PhoneUtils.getIMEI();
            } catch (Exception e1) {
                try {
                    imei = PhoneUtils.getMEID();
                } catch (Exception e2) {
                    imei = "201906171551";//正版设备：869545030806024
                }
            }
        }

        String serial = Build.SERIAL;
        String uuid;
        if (TextUtils.isEmpty(imei)) {
            imei = "unkown";
        } else if (TextUtils.isEmpty(serial)) {
            serial = "unkown";
        }
        Timber.i(TAG + " imei=" + imei + " serial=" + serial);
        uuid = UUID.nameUUIDFromBytes((imei + serial).getBytes()).toString();
        return uuid;
    }

}
