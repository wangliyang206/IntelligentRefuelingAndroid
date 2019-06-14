package com.axzl.mobile.refueling.mvp.ui.holder;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.axzl.mobile.refueling.R;
import com.axzl.mobile.refueling.mvp.model.entity.MessageBean;
import com.jess.arms.base.BaseHolder;

import butterknife.BindView;

/** 系统输出 */
public class OutputViewHolder extends BaseHolder<MessageBean> {

    @BindView(R.id.content)
    TextView content;

    public OutputViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(@NonNull MessageBean message, int position) {
        content.setText(message.getText());
    }
}