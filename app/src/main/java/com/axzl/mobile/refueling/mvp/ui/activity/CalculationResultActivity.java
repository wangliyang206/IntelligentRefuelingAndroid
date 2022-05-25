package com.axzl.mobile.refueling.mvp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.axzl.mobile.refueling.R;
import com.axzl.mobile.refueling.app.utils.chinesecalendar.ChineseCalendar;
import com.axzl.mobile.refueling.app.utils.cl.LunarCalendarUtils;
import com.axzl.mobile.refueling.di.component.DaggerCalculationResultComponent;
import com.axzl.mobile.refueling.mvp.contract.CalculationResultContract;
import com.axzl.mobile.refueling.mvp.presenter.CalculationResultPresenter;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import java.util.Calendar;

import butterknife.BindView;

import static com.jess.arms.utils.Preconditions.checkNotNull;


/**
 * ================================================
 * Description: 测算结果
 * <p>
 * Created by MVPArmsTemplate on 10/15/2019 10:06
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
public class CalculationResultActivity extends BaseActivity<CalculationResultPresenter> implements CalculationResultContract.View {

    @BindView(R.id.toba_calculationresultactivity_toolbar)
    Toolbar mToolbar;

    @BindView(R.id.txvi_result_glrq)
    TextView txviGregorianDate;// 公历日期

    @BindView(R.id.txvi_result_nlrq)
    TextView txviLunarDate;// 农历日期

    @BindView(R.id.txvi_result_zj)
    TextView txviWhichDay;// 周几

    @BindView(R.id.txvi_result_sx)
    TextView txviZodiac;// 属相

    @BindView(R.id.txvi_result_xs)
    TextView txviStar;// 星宿

    @BindView(R.id.txvi_result_sc)
    TextView txviHour;// 时辰

    @BindView(R.id.txvi_result_jq)
    TextView txviSolarTerms;// 节气

    @BindView(R.id.txvi_result_xz)
    TextView txviConstellation;// 星座

    @BindView(R.id.txvi_result_jr)
    TextView txviFestival;// 节日

    @BindView(R.id.txvi_result_gz)
    TextView txviDryBranch;// 干支

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerCalculationResultComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_calculation_result;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        // 绑定Toolbar
        setSupportActionBar(mToolbar);
        // 给左上角图标的左边加上一个返回的图标 。对应ActionBar.DISPLAY_HOME_AS_UP
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // 设置标题
        getSupportActionBar().setTitle(R.string.drawer_item_fortune_telling_result);
        // 设置监听
        mToolbar.setNavigationOnClickListener(v -> onBackPressed());

        // 接收参数
        mPresenter.getBundleValues(getIntent().getExtras());
        // 开始测算
        mPresenter.startCalculation();
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(@NonNull String message) {
        checkNotNull(message);
        ArmsUtils.snackbarText(message);
    }

    @Override
    public void launchActivity(@NonNull Intent intent) {
        checkNotNull(intent);
        ArmsUtils.startActivity(intent);
    }

    @Override
    public void killMyself() {
        finish();
    }

    @Override
    public void loadData(ChineseCalendar mChineseCalendar) {
        Calendar cld = Calendar.getInstance();
        LunarCalendarUtils.Lunar mLunar = LunarCalendarUtils.solarToLunar(new LunarCalendarUtils.Solar(cld.get(Calendar.YEAR),cld.get(Calendar.MONTH + 1) , cld.get(Calendar.DAY_OF_MONTH)));


        // 公历日期中文表示法
        txviGregorianDate.setText(mChineseCalendar.DateString());

        // 取农历日期
        txviLunarDate.setText(mChineseCalendar.ChineseDateString());

        // 周几的字符
        txviWhichDay.setText(mChineseCalendar.WeekDayStr());

        // 取属相
        txviZodiac.setText(mChineseCalendar.AnimalString());

        // 计算28星宿
        txviStar.setText(mChineseCalendar.ChineseConstellation());

        // 获得当前时间的时辰
        txviHour.setText(mChineseCalendar.ChineseHour());

        // 计算二十四节气
        txviSolarTerms.setText(mChineseCalendar.ChineseTwentyFourDay());
        // 当前日期后一个最近节气
        mChineseCalendar.ChineseTwentyFourNextDay();
        // 当前日期前一个最近节气
        mChineseCalendar.ChineseTwentyFourPrevDay();

        // 计算指定日期的星座序号
        txviConstellation.setText(mChineseCalendar.Constellation());
        // 按公历日计算的节日
        txviFestival.setText(mChineseCalendar.DateHoliday());
        // 取当前日期的干支表示法
        txviDryBranch.setText(mChineseCalendar.GanZhiDateString());
    }
}
