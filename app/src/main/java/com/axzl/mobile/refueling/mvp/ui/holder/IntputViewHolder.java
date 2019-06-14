package com.axzl.mobile.refueling.mvp.ui.holder;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.axzl.mobile.refueling.R;
import com.axzl.mobile.refueling.mvp.model.entity.MessageBean;
import com.jess.arms.base.BaseHolder;

import butterknife.BindView;

/** 用户输入 */
public class IntputViewHolder extends BaseHolder<MessageBean> {

    @BindView(R.id.content)
    TextView content;

    public IntputViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(@NonNull MessageBean message, int position) {
        content.setText(message.getText());
    }
}
