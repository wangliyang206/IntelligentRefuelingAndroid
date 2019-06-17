package com.axzl.mobile.refueling.mvp.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.KeyEvent;

import com.afollestad.materialdialogs.MaterialDialog;
import com.aispeech.dui.dds.DDS;
import com.aispeech.dui.dds.exceptions.DDSNotInitCompleteException;
import com.axzl.mobile.refueling.R;
import com.axzl.mobile.refueling.app.service.DDSService;
import com.axzl.mobile.refueling.app.utils.EventBusTags;
import com.axzl.mobile.refueling.di.component.DaggerSplashComponent;
import com.axzl.mobile.refueling.mvp.contract.SplashContract;
import com.axzl.mobile.refueling.mvp.presenter.SplashPresenter;
import com.blankj.utilcode.util.ActivityUtils;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

import static com.jess.arms.utils.Preconditions.checkNotNull;


/**
 * ================================================
 * Description: 欢迎界面
 * ================================================
 */
@RuntimePermissions
public class SplashActivity extends BaseActivity<SplashPresenter> implements SplashContract.View {

    /*--------------------------------业务信息--------------------------------*/

    /**
     * 认证弹出框
     */
    private MaterialDialog mDialog;

    @Override
    protected void onStop() {
        super.onStop();
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
        killMyself();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDialog = null;
    }

    /**
     * dds认证状态监听器,监听auth是否成功    回调
     */
    @Subscriber(tag = EventBusTags.ddsAuthSuccess, mode = ThreadMode.POST)
    private void DDSAuthSuccess(String str) {
        jumbToMain();
    }


    /**
     * dds认证状态监听器,监听auth是否失败    回调
     */
    @Subscriber(tag = EventBusTags.ddsAuthFailed, mode = ThreadMode.POST)
    private void DDSAuthFailed(String str) {
        mPresenter.doAutoAuth();
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerSplashComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_splash;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        //获取权限
        SplashActivityPermissionsDispatcher.runAppWithPermissionCheck(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        SplashActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    /**
     * 申请权限成功后的逻辑
     */
    @NeedsPermission({
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO
    })
    public void runApp() {
        // 初始化Loading对话框
        mDialog = new MaterialDialog.Builder(this)
                .content("产品未授权，请先授权！")
                .positiveText("授权")
                .negativeText("退出")
                .onPositive((dialog, which) -> {
                    try {
                        DDS.getInstance().doAuth();
                    } catch (DDSNotInitCompleteException e) {
                        e.printStackTrace();
                    }
                    dialog.dismiss();
                })
                .onNegative((dialog, which) -> {
                    dialog.dismiss();
                    killMyself();
                })
                .cancelable(false)
                .build();
        // 启动后台服务
        startService(new Intent(this, DDSService.class));
        // 初始化数据
        mPresenter.initPresenter();

    }

    /**
     * 显示授权弹框给用户
     */
    public void showDoAuthDialog() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
        mDialog.show();
    }


    /**
     * 关闭滑动返回
     */
    @Override
    public boolean supportSlideBack() {
        return false;
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(@NonNull String message) {
        checkNotNull(message);
        ArmsUtils.makeText(getApplicationContext(), message);
    }

    @Override
    public void launchActivity(@NonNull Intent intent) {
        checkNotNull(intent);
        ArmsUtils.startActivity(intent);
    }

    @Override
    public void killMyself() {
        finish();
    }

    /**
     * 屏蔽返回按钮
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        return false;
    }

    /**
     * 屏蔽返回按钮
     */
    @Override
    public void onBackPressed() {

    }

    /**
     * 跳转到主界面
     */
    @Override
    public void jumbToMain() {
        ActivityUtils.startActivity(MainActivity.class);
        killMyself();
    }

}
