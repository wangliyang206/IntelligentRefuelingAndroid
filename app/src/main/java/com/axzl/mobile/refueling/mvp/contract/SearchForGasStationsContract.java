package com.axzl.mobile.refueling.mvp.contract;

import com.axzl.mobile.refueling.mvp.model.entity.GasStationsResponse;
import com.jess.arms.mvp.IModel;
import com.jess.arms.mvp.IView;

import io.reactivex.Observable;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 06/28/2019 10:36
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
public interface SearchForGasStationsContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View extends IView {
        /**
         * 开始加载更多
         */
        void startLoadMore();

        /**
         * 结束加载更多
         */
        void endLoadMore();

        /**
         * 控制列表是否到底
         */
        void hasLoadedAllItems(boolean val);

        /**
         * 暂无信息
         */
        void noInfo();
    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model extends IModel {
        /**
         * 获取附近加油站
         */
        Observable<GasStationsResponse> getSearchForStations(int page, int rows);
    }
}
