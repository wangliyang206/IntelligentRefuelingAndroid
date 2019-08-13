/*
 * Copyright 2017 JessYan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.axzl.mobile.refueling.mvp.ui.adapter;

import android.support.annotation.NonNull;
import android.view.View;

import com.axzl.mobile.refueling.R;
import com.axzl.mobile.refueling.mvp.ui.holder.FontAwesomeItemHolder;
import com.axzl.mobile.refueling.mvp.ui.holder.GoogleMaterialItemHolder;
import com.jess.arms.base.BaseHolder;
import com.jess.arms.base.DefaultAdapter;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;

import java.util.List;

/**
 * ================================================
 * 展示 {@link DefaultAdapter} 的用法
 * <p>
 * Created by JessYan on 09/04/2016 12:57
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * ================================================
 */
public class GoogleMaterialAdapter extends DefaultAdapter<GoogleMaterial.Icon> {

    public GoogleMaterialAdapter(List<GoogleMaterial.Icon> infos) {
        super(infos);
    }

    @NonNull
    @Override
    public BaseHolder<GoogleMaterial.Icon> getHolder(@NonNull View v, int viewType) {
        return new GoogleMaterialItemHolder(v);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_font_awesome;
    }
}
