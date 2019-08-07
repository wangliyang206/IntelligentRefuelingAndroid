package com.axzl.mobile.refueling.app.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.axzl.mobile.refueling.app.global.Constant;
import com.axzl.mobile.refueling.mvp.model.api.service.AccountService;
import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.FileUtils;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import okhttp3.ResponseBody;
import timber.log.Timber;

/**
 * 下载图片
 */
public class DownloadService extends IntentService {
    private AppComponent appComponent;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name `
     */
    public DownloadService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String fileName = intent.getStringExtra("name");
        String downloadUrl = intent.getStringExtra("downloadUrl");
        appComponent = ArmsUtils.obtainAppComponentFromContext(getApplicationContext());
        if (downloadUrl.isEmpty()) {
            appComponent.rxErrorHandler().getHandlerFactory().handleError(new Throwable("连接地址不能为空"));
        }

        appComponent.repositoryManager().obtainRetrofitService(AccountService.class)
                .download(downloadUrl)
                .subscribe(new ErrorHandleSubscriber<ResponseBody>(appComponent.rxErrorHandler()) {

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        // 保存文件到sd卡
                        try {
                            String filePath = Constant.IMAGE_PATH + fileName;
                            // 判断目录是否存在，不存在则创建一个
                            FileUtils.createOrExistsDir(filePath);
                            // 将流写入文件。
                            FileIOUtils.writeFileFromIS(filePath, responseBody.byteStream());
                        } catch (Exception e) {
                            Timber.e(e);
                            appComponent.rxErrorHandler().getHandlerFactory().handleError(e);
                        }
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        appComponent = null;
    }
}
