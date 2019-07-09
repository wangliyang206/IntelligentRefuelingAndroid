package com.axzl.mobile.refueling.mvp.ui.adapter;

import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import com.aispeech.ailog.AILog;
import com.axzl.mobile.refueling.R;
import com.axzl.mobile.refueling.mvp.model.entity.MessageBean;
import com.axzl.mobile.refueling.mvp.ui.holder.IntputViewHolder;
import com.axzl.mobile.refueling.mvp.ui.holder.OutputViewHolder;
import com.axzl.mobile.refueling.mvp.ui.holder.QrViewHolder;
import com.axzl.mobile.refueling.mvp.ui.holder.WebViewHolder;
import com.axzl.mobile.refueling.mvp.ui.holder.WidgetContentViewHolder;
import com.axzl.mobile.refueling.mvp.ui.holder.WidgetListViewHolder;
import com.jess.arms.base.BaseHolder;
import com.jess.arms.base.DefaultAdapter;

import java.util.LinkedList;

public class DialogAdapter extends DefaultAdapter<MessageBean> {
    private static final String TAG = "DialogAdapter";

    /**
     * 当前状态
     */
    private String mState;

    public void setmState(String mState) {
        this.mState = mState;
    }

    public DialogAdapter(LinkedList<MessageBean> infos) {
        super(infos);
    }

    @NonNull
    @Override
    public BaseHolder<MessageBean> getHolder(@NonNull View v, int viewType) {
        AILog.i(TAG, "getHolder" + viewType);
        switch (viewType) {
            case MessageBean.TYPE_INPUT:
                return new IntputViewHolder(v);

            case MessageBean.TYPE_OUTPUT:
                return new OutputViewHolder(v);

            case MessageBean.TYPE_WIDGET_CONTENT:
                return new WidgetContentViewHolder(v);

            case MessageBean.TYPE_WIDGET_LIST:
                return new WidgetListViewHolder(v, mState, mInfos);

            case MessageBean.TYPE_WIDGET_WEB:
                return new WebViewHolder(v);

            case MessageBean.TYPE_WIDGET_MEDIA:
                return new WebViewHolder(v);

            case MessageBean.TYPE_QR_CODE:
                return new QrViewHolder(v);
        }
        return new IntputViewHolder(v);
    }

    @Override
    public int getLayoutId(int viewType) {
        AILog.i(TAG, "getLayoutId" + viewType);
        switch (viewType) {
            case MessageBean.TYPE_INPUT:
                return R.layout.msg_input;

            case MessageBean.TYPE_OUTPUT:
                return R.layout.msg_output;

            case MessageBean.TYPE_WIDGET_CONTENT:
                return R.layout.msg_widget_content;

            case MessageBean.TYPE_WIDGET_LIST:
                return R.layout.msg_widget_list;

            case MessageBean.TYPE_WIDGET_WEB:
                return R.layout.msg_widget_web;

            case MessageBean.TYPE_WIDGET_MEDIA:
                return R.layout.msg_widget_web;

            case MessageBean.TYPE_QR_CODE:
                return R.layout.msg_widget_qr;
        }
        return R.layout.msg_input;
    }

    @Override
    public int getItemViewType(int position) {
        Log.d(TAG, "getItemViewType " + position);
        return mInfos.get(position).getType();
    }
}
