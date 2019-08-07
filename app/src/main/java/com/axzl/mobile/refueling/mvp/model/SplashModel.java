package com.axzl.mobile.refueling.mvp.model;

import com.axzl.mobile.refueling.mvp.contract.SplashContract;
import com.axzl.mobile.refueling.mvp.model.api.service.AccountService;
import com.axzl.mobile.refueling.mvp.model.entity.SplashResponse;
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
 * Created by MVPArmsTemplate on 06/05/2019 13:57
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
public class SplashModel extends BaseModel implements SplashContract.Model {
    @Inject
    ApiOperator apiOperator;                                                                        // 数据转换

    @Inject
    public SplashModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.apiOperator = null;
    }

    @Override
    public Observable<SplashResponse> loadSplashImage(String mark) {
        Map<String, Object> params = new HashMap<>();
        params.put("mark", mark);
        return apiOperator.chain(params, request -> mRepositoryManager.obtainRetrofitService(AccountService.class).loadSplashImage(request));
    }
}