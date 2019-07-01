package com.axzl.mobile.refueling.mvp.ui.holder;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.axzl.mobile.refueling.R;
import com.axzl.mobile.refueling.mvp.model.entity.GasStations;
import com.jess.arms.base.BaseHolder;

import butterknife.BindView;

/**
 * 显示加油站item
 */
public class GasStationsListViewHolder extends BaseHolder<GasStations> {

    @BindView(R.id.t_index)
    TextView mIndex;
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.subtitle)
    TextView mSubTitle;

    public GasStationsListViewHolder(View itemView) {
        super(itemView);

    }

    @Override
    public void setData(@NonNull GasStations item, int position) {
        mIndex.setText(String.valueOf(position + 1));
        mTitle.setText(item.getTitle());
        mSubTitle.setText(item.getSubTitle());

    }
}
