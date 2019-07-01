package com.axzl.mobile.refueling.mvp.model;

import com.axzl.mobile.refueling.mvp.contract.SearchForGasStationsContract;
import com.axzl.mobile.refueling.mvp.model.api.service.AccountService;
import com.axzl.mobile.refueling.mvp.model.entity.GasStationsResponse;
import com.jess.arms.cj.ApiOperator;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

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
@ActivityScope
public class SearchForGasStationsModel extends BaseModel implements SearchForGasStationsContract.Model {
    @Inject
    ApiOperator apiOperator;                                                                        // 数据转换

    @Inject
    public SearchForGasStationsModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.apiOperator = null;
    }

    @Override
    public Observable<GasStationsResponse> getSearchForStations(int page, int rows) {
        Map<String, Object> params = new HashMap<>();
        params.put("page", page);
        params.put("rows", rows);
        return apiOperator.chain(params, request -> mRepositoryManager.obtainRetrofitService(AccountService.class).getSearchForStations(request));
    }
}