package com.axzl.mobile.refueling.mvp.ui.activity;

import android.Manifest;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.KeyEvent;

import com.afollestad.materialdialogs.MaterialDialog;
import com.axzl.mobile.refueling.R;
import com.axzl.mobile.refueling.di.component.DaggerSplashComponent;
import com.axzl.mobile.refueling.mvp.contract.SplashContract;
import com.axzl.mobile.refueling.mvp.presenter.SplashPresenter;
import com.axzl.mobile.refueling.mvp.ui.widget.FixedImageView;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.http.imageloader.glide.ImageConfigImpl;
import com.jess.arms.utils.ArmsUtils;

import javax.inject.Inject;

import butterknife.BindView;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;

import static com.jess.arms.utils.Preconditions.checkNotNull;


/**
 * ================================================
 * Description: 欢迎界面
 * ================================================
 */
@RuntimePermissions
public class SplashActivity extends BaseActivity<SplashPresenter> implements SplashContract.View {
    @BindView(R.id.splash_img)
    FixedImageView splashImg;

    @Inject
    ImageLoader mImageLoader;

    /**
     * 对话框
     */
    private MaterialDialog askDialog;

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mImageLoader = null;

        if (askDialog != null) {
            askDialog.dismiss();
            askDialog = null;
        }
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
        // 获取权限
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
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.RECORD_AUDIO
    })
    public void runApp() {
        mPresenter.initPresenter();
    }

    /**
     * 拒绝权限后的提醒
     */
    @OnPermissionDenied({
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.RECORD_AUDIO
    })
    public void showMissingPermissionDialog() {

        askDialog = new MaterialDialog.Builder(this)
                .title(R.string.common_tips)
                .content(R.string.common_permissions_denied_tips)
                .positiveText(R.string.common_no)
                .onPositive((dialog, which) -> {
                    // 关闭
                    killMyself();
                })
                .negativeText(R.string.common_setting)
                .onNegative((dialog, which) -> {
                    // 打开设置
                    PermissionUtils.launchAppDetailsSettings();
                }).cancelable(false)
                .show();
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
        ArmsUtils.snackbarText(message);
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
     * 加载图片
     */
    @Override
    public void loadImg(String url) {
        mImageLoader.loadImage(getApplicationContext(),
                ImageConfigImpl.builder().url(url)
                        .errorPic(R.mipmap.splash_default)
                        .placeholder(R.mipmap.splash_default)
                        .imageView(splashImg).build());

        // 开启动画
        animWelcomeImage();
    }

    /**
     * 开启动画
     */
    private void animWelcomeImage() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(splashImg, "translationX", -100F);
        animator.setDuration(4000L).start();
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                jumbToMain();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }


    /**
     * 跳转到主界面
     */
    public void jumbToMain() {
        ActivityUtils.startActivity(MainActivity.class);
        killMyself();
    }
}
