package com.axzl.mobile.refueling.mvp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.aispeech.ailog.AILog;
import com.aispeech.dui.dds.DDS;
import com.aispeech.dui.dds.exceptions.DDSNotInitCompleteException;
import com.axzl.mobile.refueling.BuildConfig;
import com.axzl.mobile.refueling.R;
import com.axzl.mobile.refueling.app.service.DDSService;
import com.axzl.mobile.refueling.app.utils.EventBusTags;
import com.axzl.mobile.refueling.app.utils.RxUtils;
import com.axzl.mobile.refueling.di.component.DaggerMainComponent;
import com.axzl.mobile.refueling.mvp.contract.MainContract;
import com.axzl.mobile.refueling.mvp.model.entity.MessageBean;
import com.axzl.mobile.refueling.mvp.presenter.MainPresenter;
import com.axzl.mobile.refueling.mvp.ui.adapter.DialogAdapter;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;

import java.util.LinkedList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import timber.log.Timber;

import static com.jess.arms.utils.Preconditions.checkNotNull;


/**
 * ================================================
 * Description:首页
 * ================================================
 */
public class MainActivity extends BaseActivity<MainPresenter> implements MainContract.View {

    /*--------------------------------控件信息--------------------------------*/
    @BindView(R.id.input_tv)
    TextView mInputTv;                                                                              // 下面的状态展示textview

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;                                                                     // 列表展示控件

    /*--------------------------------业务信息--------------------------------*/
    @Inject
    DialogAdapter mAdapter;
    @Inject
    RecyclerView.LayoutManager mLayoutManager;
    @Inject
    LinkedList<MessageBean> mMessageList;                                                           // 当前消息容器

    /**
     * Loading 效果
     */
    private MaterialDialog mDialog;
    /**
     * 是否是服务调起的
     */
    private boolean isService = false;

    @Override
    protected void onResume() {
        if (isService) {
            isService = false;
        } else {
            sendHiMessage();
        }
        refreshTv("等待唤醒...");
        super.onResume();
    }

    @Override
    protected void onPause() {
        refreshTv("等待唤醒...");
        super.onPause();
    }

    @Override
    protected void onStop() {
        AILog.d(TAG, "onStop() " + this.hashCode());
        hideLoading();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        //super.onDestroy()之后会unbind,所有view被置为null,所以必须在之前调用
        DefaultAdapter.releaseAllHolder(mRecyclerView);
        super.onDestroy();
        this.mAdapter = null;
        this.mLayoutManager = null;
        this.mMessageList = null;
        this.mDialog = null;

        if (!BuildConfig.IS_INDEPENDENCE) {
            // 非独立
            // 停止service, 释放dds组件
            stopService(new Intent(MainActivity.this, DDSService.class));
        }

    }

    /**
     * 关闭滑动返回
     */
    @Override
    public boolean supportSlideBack() {
        return false;
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerMainComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_main;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        // 初始化控件
        initRecyclerView();

        // 初始化Loading对话框
        mDialog = new MaterialDialog.Builder(this)
                .content(R.string.common_execute)
                .progress(true, 0)
                .cancelable(false)
                .build();

        // 处理业务
        getBundleValues();
    }

    /**
     * 处理“语音服务”收到消息后调起界面
     */
    private void getBundleValues() {
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            isService = bundle.getBoolean("isService", false);
            if (isService) {
                String tips = bundle.getString("tips", "");
                MessageBean bean = new MessageBean();
                bean.setText(tips);
                bean.setType(MessageBean.TYPE_OUTPUT);
                notifyItemInserted(bean);
            }

        }
    }

    /**
     * 初始化RecyclerView
     */
    private void initRecyclerView() {
        ArmsUtils.configRecyclerView(mRecyclerView, mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    /**
     * dds初始化成功之后,展示一个打招呼消息,告诉用户可以开始使用
     */
    public void sendHiMessage() {
        String[] wakeupWords = new String[0];
        String minorWakeupWord = null;
        try {
            // 获取主唤醒词
            wakeupWords = DDS.getInstance().getAgent().getWakeupEngine().getWakeupWords();
            // 获取副唤醒词
            minorWakeupWord = DDS.getInstance().getAgent().getWakeupEngine().getMinorWakeupWord();
        } catch (DDSNotInitCompleteException e) {
            e.printStackTrace();
        }
        String hiStr = "";
        if (wakeupWords != null && minorWakeupWord != null) {
            hiStr = getString(R.string.hi_str2, wakeupWords[0], minorWakeupWord);
        } else if (wakeupWords != null && wakeupWords.length == 2) {
            hiStr = getString(R.string.hi_str2, wakeupWords[0], wakeupWords[1]);
        } else if (wakeupWords != null && wakeupWords.length > 0) {
            hiStr = getString(R.string.hi_str, wakeupWords[0]);
        }
        Timber.i(TAG + "histr = " + hiStr);
        if (!TextUtils.isEmpty(hiStr)) {
            MessageBean bean = new MessageBean();
            bean.setText(hiStr);
            bean.setType(MessageBean.TYPE_OUTPUT);
            mMessageList.add(bean);
            mAdapter.notifyItemInserted(mMessageList.size());
            mRecyclerView.smoothScrollToPosition(mMessageList.size());
        }
    }

    /**
     * 更新 tv状态
     */
    @Subscriber(tag = EventBusTags.mainRefreshTextView, mode = ThreadMode.POST)
    private void refreshTv(final String text) {
        RxUtils.doOnUIThread(this, () -> mInputTv.setText(text));
    }

    /**
     * dds状态监听器，监听init是否成功    回调
     */
    @Subscriber(tag = EventBusTags.ddsInitSuccess, mode = ThreadMode.POST)
    private void DDSInitSuccess(String str) {
        refreshTv("等待唤醒...");
        // 此处等待200ms,等待wakeup节点完成初始成功
        // 我们已知此问题,待下一版本我们会将此等待去除,
        // 开发者使用时就认为此处不需要等待即可,dds在后续的升级中会解决此问题
        new Thread(() -> {
            SystemClock.sleep(200);
            RxUtils.doOnUIThread(this, () -> sendHiMessage());
        }).start();
    }

    @OnClick({
            R.id.input_tv
    })
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.input_tv:                                                                     // 手动改变状态(等待唤醒、监听中、理解中、播放语音中)
                try {
                    DDS.getInstance().getAgent().avatarClick();
                } catch (DDSNotInitCompleteException e) {
                    e.printStackTrace();
                }
                break;
        }

    }

    /**
     * 控制显示与隐藏进度
     */
    @Subscriber(tag = EventBusTags.mainLoading, mode = ThreadMode.POST)
    private void mainLoading(Boolean val) {
        if (val)
            showLoading();
        else
            hideLoading();
    }

    @Subscriber(tag = EventBusTags.mainOpenAppTips, mode = ThreadMode.POST)
    @Override
    public void showMessage(@NonNull String message) {
        checkNotNull(message);
//        ArmsUtils.snackbarText(message);
        ArmsUtils.makeText(getApplicationContext(), message);
    }

    /**
     * 更新ui列表展示
     */
    @Subscriber(tag = EventBusTags.mainRefreshAdapter, mode = ThreadMode.POST)
    private void notifyItemInserted(MessageBean bean) {
        this.mMessageList.add(bean);
        RxUtils.doOnUIThread(this, () -> {
            mAdapter.notifyDataSetChanged();
            mRecyclerView.smoothScrollToPosition(mMessageList.size());
        });
    }

    /**
     * 删除数据最后一行(去除重复数据)
     */
    @Subscriber(tag = EventBusTags.mainMessagePollLast, mode = ThreadMode.POST)
    private void onMessagePollLast(String str) {
        if (mMessageList != null)
            mMessageList.pollLast();
    }

    /**
     * 接收DDS Service中服务状态
     */
    @Subscriber(tag = EventBusTags.mainReceivingStatus, mode = ThreadMode.POST)
    private void onState(String state) {
        switch (state) {
            case "avatar.silence":
                refreshTv("等待唤醒...");
                break;
            case "avatar.listening":
                refreshTv("监听中...");
                break;
            case "avatar.understanding":
                refreshTv("理解中...");
                break;
            case "avatar.speaking":
                refreshTv("播放语音中...");
                break;
        }
        mAdapter.setmState(state);
    }

    /**
     * 后台服务控制显示框是否可用
     */
    @Subscriber(tag = EventBusTags.mainControlTextView, mode = ThreadMode.POST)
    private void setEnabledTv(Boolean val) {
        RxUtils.doOnUIThread(this, () -> {
            mInputTv.setEnabled(val);
        });

    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void showLoading() {
        if (mDialog != null && !mDialog.isShowing())
            mDialog.show();
    }

    @Override
    public void hideLoading() {
        if (mDialog != null && mDialog.isShowing())
            mDialog.dismiss();
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
}
