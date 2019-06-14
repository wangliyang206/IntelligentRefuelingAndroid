package com.axzl.mobile.refueling.mvp.ui.holder;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.axzl.mobile.refueling.R;
import com.axzl.mobile.refueling.mvp.model.entity.MessageBean;
import com.facebook.drawee.view.SimpleDraweeView;
import com.jess.arms.base.BaseHolder;

import butterknife.BindView;

public class WidgetContentViewHolder extends BaseHolder<MessageBean> {
    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.sub_title)
    TextView subTitle;

    @BindView(R.id.image)
    SimpleDraweeView imageView;

    public WidgetContentViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(@NonNull MessageBean message, int position) {
        title.setText(message.getTitle());
        subTitle.setText(message.getSubTitle());
        imageView.setImageURI(Uri.parse(message.getImgUrl()));
    }
}
