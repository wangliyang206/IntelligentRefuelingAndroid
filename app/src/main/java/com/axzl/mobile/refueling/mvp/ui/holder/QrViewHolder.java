package com.axzl.mobile.refueling.mvp.ui.holder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.axzl.mobile.refueling.R;
import com.axzl.mobile.refueling.app.utils.EventBusTags;
import com.axzl.mobile.refueling.mvp.model.entity.MessageBean;
import com.jess.arms.base.BaseHolder;
import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.http.imageloader.glide.ImageConfigImpl;
import com.jess.arms.utils.ArmsUtils;

import org.simple.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

public class QrViewHolder extends BaseHolder<MessageBean> {
    @BindView(R.id.btn_msgwidgetqr_succ)
    Button btnSucc;

    @BindView(R.id.img_msgwidgetqr_icon)
    ImageView imgIcon;

    private AppComponent mAppComponent;
    /**
     * 用于加载图片的管理类, 默认使用 Glide, 使用策略模式, 可替换框架
     */
    private ImageLoader mImageLoader;

    public QrViewHolder(View itemView) {
        super(itemView);

        //可以在任何可以拿到 Context 的地方, 拿到 AppComponent, 从而得到用 Dagger 管理的单例对象
        mAppComponent = ArmsUtils.obtainAppComponentFromContext(itemView.getContext());
        mImageLoader = mAppComponent.imageLoader();
    }

    @Override
    public void setData(@NonNull MessageBean message, int position) {
        imgIcon.setImageResource(R.mipmap.qr_code);
    }

    @OnClick(R.id.btn_msgwidgetqr_succ)
    public void onClick(View v) {
        MessageBean bean = new MessageBean();
        bean.setText("凭证号码：190709");
        bean.setType(MessageBean.TYPE_OUTPUT);
        EventBus.getDefault().post(bean, EventBusTags.mainRefreshAdapter);
    }


    /**
     * 在 Activity 的 onDestroy 中使用 {@link DefaultAdapter#releaseAllHolder(RecyclerView)} 方法 (super.onDestroy() 之前)
     * {@link BaseHolder#onRelease()} 才会被调用, 可以在此方法中释放一些资源
     */
    @Override
    protected void onRelease() {
        //只要传入的 Context 为 Activity, Glide 就会自己做好生命周期的管理, 其实在上面的代码中传入的 Context 就是 Activity
        //所以在 onRelease 方法中不做 clear 也是可以的, 但是在这里想展示一下 clear 的用法
        mImageLoader.clear(mAppComponent.application(), ImageConfigImpl.builder()
                .imageViews(imgIcon)
                .build());
        this.imgIcon = null;
        this.btnSucc = null;
        this.mAppComponent = null;
        this.mImageLoader = null;
    }
}
