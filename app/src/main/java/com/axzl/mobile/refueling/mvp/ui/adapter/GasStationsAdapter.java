package com.axzl.mobile.refueling.mvp.ui.adapter;

import android.support.annotation.NonNull;
import android.view.View;

import com.axzl.mobile.refueling.R;
import com.axzl.mobile.refueling.mvp.model.entity.GasStations;
import com.axzl.mobile.refueling.mvp.ui.holder.GasStationsListViewHolder;
import com.jess.arms.base.BaseHolder;
import com.jess.arms.base.DefaultAdapter;

import java.util.List;

/**
 * 加油站列表 适配器
 */
public class GasStationsAdapter extends DefaultAdapter<GasStations> {

    public GasStationsAdapter(List<GasStations> infos) {
        super(infos);
    }

    @NonNull
    @Override
    public BaseHolder<GasStations> getHolder(@NonNull View v, int viewType) {
        return new GasStationsListViewHolder(v);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_horizontal_grid2;
    }
}
