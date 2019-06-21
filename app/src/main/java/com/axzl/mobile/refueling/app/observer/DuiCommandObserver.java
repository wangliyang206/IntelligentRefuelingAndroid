package com.axzl.mobile.refueling.app.observer;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.aispeech.dui.dds.DDS;
import com.aispeech.dui.dsk.duiwidget.CommandObserver;
import com.axzl.mobile.refueling.app.utils.CommonUtils;
import com.axzl.mobile.refueling.app.utils.EventBusTags;
import com.axzl.mobile.refueling.mvp.model.entity.AppInfo;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.ThreadUtils;
import com.jess.arms.widget.etoast2.EToast2;
import com.jess.arms.widget.etoast2.Toast;

import org.json.JSONObject;
import org.simple.eventbus.EventBus;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * 客户端CommandObserver, 用于处理客户端动作的执行以及快捷唤醒中的命令响应.
 * 例如在平台配置客户端动作： command://call?phone=$phone$&name=#name#,
 * 那么在CommandObserver的onCall方法中会回调topic为"call", data为
 */
public class DuiCommandObserver implements CommandObserver {
    private final String TAG = "DuiCommandObserver";
    private static final String COMMAND_CALL = "sys.action.call";
    private static final String COMMAND_SELECT = "sys.action.call.select";
    private static final String OPEN_WINDOW = "open_window";
    private static final String NAVI_ROUTE = "navi.route";
    private String mSelectedPhone = null;
    private Context mContent;

    public DuiCommandObserver() {
    }

    // 注册当前更新消息
    public void regist(Context mContent) {
        this.mContent = mContent;
        DDS.getInstance().getAgent().subscribe(new String[]{COMMAND_CALL, COMMAND_SELECT, OPEN_WINDOW,NAVI_ROUTE},
                this);
    }

    // 注销当前更新消息
    public void unregist() {
        DDS.getInstance().getAgent().unSubscribe(this);
    }

    @Override
    public void onCall(String command, String data) {
        Timber.i(TAG + "command: " + command + "  data: " + data);
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
            } else if(NAVI_ROUTE.equals(command)){
                JSONObject jsonData = new JSONObject(data);
//                String intentName = jsonData.optString("intentName");
                String lat = jsonData.optString("lat");
                String lng = jsonData.optString("lng");
                goToGaodeMap(lat,lng);
            }else if (OPEN_WINDOW.equals(command)) {
                JSONObject jsonData = new JSONObject(data);
                String intentName = jsonData.optString("intentName");
                String w = jsonData.optString("w");
                Timber.i(TAG + intentName + w);

                // 根据APP名称搜索本机应用程序，得到结果后打开。
                Observable.just("")
                        .subscribeOn(Schedulers.io())
                        .doOnSubscribe(disposable -> {
                            Timber.i("###显示进度条：" + ThreadUtils.isMainThread());
                            // 显示进度条
                            EventBus.getDefault().post(true, EventBusTags.mainLoading);
                        })
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doFinally(() -> {
                            Timber.i("###隐藏进度条：" + ThreadUtils.isMainThread());
                            // 隐藏进度条
                            EventBus.getDefault().post(false, EventBusTags.mainLoading);
                        })
                        .subscribe(new Observer<String>() {
                            private Disposable disposable;

                            @Override
                            public void onSubscribe(Disposable d) {
                                Timber.i("###onSubscribe：" + ThreadUtils.isMainThread());
                                disposable = d;
                                AppInfo app = CommonUtils.getAppMessage(mContent, w);
                                if (app != null) {
                                    try {
                                        Intent intent = mContent.getPackageManager().getLaunchIntentForPackage(app.getPageName());
                                        mContent.startActivity(intent);
                                    } catch (Exception e) {
                                        EventBus.getDefault().post("检查您是否有安装" + w, EventBusTags.mainOpenAppTips);
                                    }
                                }
                            }

                            @Override
                            public void onNext(String s) {
                                Timber.i("###onNext：" + ThreadUtils.isMainThread());
                            }

                            @Override
                            public void onError(Throwable e) {
                                Timber.i("###onError：" + e.getMessage());
                            }

                            @Override
                            public void onComplete() {
                                Timber.i("###onComplete：" + ThreadUtils.isMainThread());
                                disposable.dispose();
                            }
                        });

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
        Timber.i(TAG + "phoneDial:" + number);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_CALL);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setData(Uri.parse("tel:" + number));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        mContent.startActivity(intent);
    }

    /**
     * 跳转高德地图
     */
    private void goToGaodeMap(String latitude, String longitude) {
        if (!AppUtils.isAppInstalled("com.autonavi.minimap")) {
            Toast.makeText(mContent.getApplicationContext(), "请先安装高德地图客户端", EToast2.LENGTH_LONG);
            return;
        }

        StringBuffer stringBuffer = new StringBuffer("androidamap://navi?sourceApplication=").append("amap");
        stringBuffer.append("&lat=").append(latitude)
                .append("&lon=").append(longitude)//.append("&keywords=" + "a1")
                .append("&dev=").append(0)
                .append("&style=").append(2);
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(stringBuffer.toString()));
        intent.setPackage("com.autonavi.minimap");
        mContent.startActivity(intent);
    }
}
