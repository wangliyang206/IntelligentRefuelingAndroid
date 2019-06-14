package com.axzl.mobile.refueling.mvp.ui.holder;

import android.support.annotation.NonNull;
import android.view.View;

import com.aispeech.ailog.AILog;
import com.aispeech.dui.dds.DDS;
import com.axzl.mobile.refueling.R;
import com.axzl.mobile.refueling.mvp.model.entity.MessageBean;
import com.axzl.mobile.refueling.mvp.ui.adapter.ListWidgetAdapter;
import com.axzl.mobile.refueling.mvp.ui.widget.pageview.view.PageRecyclerView;
import com.axzl.mobile.refueling.mvp.ui.widget.pageview.view.PageView;
import com.jess.arms.base.BaseHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class WidgetListViewHolder extends BaseHolder<MessageBean> {

    @BindView(R.id.pageView)
    PageView viewPager;

    /** 总数据源 */
    private List<MessageBean> mList;
    /** 当前状态 */
    private String mState;

    public WidgetListViewHolder(View itemView, String mState, List<MessageBean> infos) {
        super(itemView);
        this.mState = mState;
        this.mList = infos;

    }

    @Override
    public void setData(@NonNull MessageBean message, int position) {
        setIsRecyclable(false);
        bindPageView(viewPager, message, position);

    }

    private void bindPageView(final PageView pageview, final MessageBean message, final int position) {
        AILog.i(TAG, "bindPageView" + position + ", listwidget page: " + message.getCurrentPage() + ", pageview: " + pageview);
        ArrayList<MessageBean> items = message.getMessageBeanList();
        pageview.setPageRow(message.getItemsPerPage());
        ListWidgetAdapter adapter = new ListWidgetAdapter(itemView.getContext(), R.layout.item_horizontal_grid2, position,mState,mList);
        pageview.setAdapter(adapter);
        pageview.updateAll(items);

        Runnable setCurrentItemRunnable = () -> pageview.setCurrentItem(message.getCurrentPage() - 1, false);

        Runnable addPageChangeListenerRunnable = () -> pageview.addOnPageChangeListener(new ListWidgetPageChangeListener(position));
        pageview.postDelayed(setCurrentItemRunnable, 200);
        pageview.postDelayed(addPageChangeListenerRunnable, 1000);
    }

    public class ListWidgetPageChangeListener implements PageRecyclerView.OnPageChangeListener {

        private int mPosition;

        ListWidgetPageChangeListener(int position) {
            mPosition = position;
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            MessageBean message = mList.get(mPosition);
            message.setCurrentPage(position + 1);
            if (!mState.equals("avatar.silence") && mPosition == mList.size() - 1) {
                int targetPage = position + 1;
                DDS.getInstance().getAgent().publishSticky("list.page.switch", "{\"pageNumber\":" + targetPage + "}");
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}
