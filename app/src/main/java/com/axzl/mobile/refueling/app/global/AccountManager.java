package com.axzl.mobile.refueling.app.global;

import android.content.Context;
import android.text.TextUtils;

import com.axzl.mobile.refueling.BuildConfig;
import com.axzl.mobile.refueling.app.utils.AppPreferencesHelper;
import com.tencent.bugly.crashreport.CrashReport;

import javax.inject.Inject;
import javax.inject.Singleton;

import timber.log.Timber;

/**
 * @Title: AccountManager
 * @Package com.zqw.mobile.recycling.api
 * @Description: 用户信息存取类：从SharedPreferences中读取用户登录信息
 * @author: wly
 * @date: 2017/03/13 14:24
 */
@Singleton
public final class AccountManager {

    /*----------------------------------------------业务常量-------------------------------------------------*/
    /**
     * 头像
     */
    private final String PHOTOURL = "photoUrl";

    /**
     * 账号
     */
    private final String ACCOUNT = "Account";

    /**
     * 密码
     */
    private final String PASSWORD = "Password";

    /**
     * Token
     */
    private final String TOKEN = "Token";

    /**
     * 用户id
     */
    private final String USERID = "Userid";

    /**
     * 用户名称(昵称)
     */
    private final String USER_NAME = "UserName";

    /**
     * 电话
     */
    private final String RECYCLE_PHONE = "recyclePhone";

    /**
     * APP启动次数(达到一定次数后清理日志)
     */
    private final String START_TIME = "startTime";


    /**
     * 定位 - 经度
     */
    private static final String LONGITUDE = "Longitude";

    /**
     * 定位 - 纬度
     */
    private static final String LATITUDE = "latitude";

    /**
     * 定位 - 半径
     */
    private static final String RADIUS = "radius";

    /**
     * 定位 - 地址
     */
    private static final String ADDRESS = "address";


    public static final String IS_AEC_ENABLED = "is_aec_enabled";
    public static final String IS_INTERRUPT_ENABLED = "is_interrupt_enabled";
    public static final String IS_AIOS_SWITCH = "is_aios_switch";
    public static final String IS_NATIVE_SHORTCUT_ENABLE = "is_native_shortcut_enable";
    public static final String AIOS_VOLUME = "aios_volume";
    public static final String IS_SHOW_GLOBAL_MIC = "is_show_global_mic";
    public static final String IS_ONESHOT_SWITCH = "is_oneshot_switch";
    public static final String IS_WAKEUP_SWITCH= "is_wakeup_switch";
    /*----------------------------------------------操作对象-------------------------------------------------*/

    public void setIsWakeupSwitch(boolean def){
        spHelper.put(IS_WAKEUP_SWITCH,def);
    }

    public void setIsAiosSwitch(boolean def){
        spHelper.put(IS_AIOS_SWITCH,def);
    }

    public boolean getIsAecEnabled(boolean def){
        return spHelper.getPref(IS_AEC_ENABLED,def);
    }

    public boolean getIsInterruptEnabled(boolean def){
        return spHelper.getPref(IS_INTERRUPT_ENABLED,def);
    }

    private AppPreferencesHelper spHelper;

    @Inject
    public AccountManager(Context context) {
        this.spHelper = new AppPreferencesHelper(context.getApplicationContext(), BuildConfig.SHARED_NAME_INVEST, 1);

        updateBugly();
    }

    /**
     * 更新Bugly状态
     */
    private void updateBugly() {
        try {
            String userId = getUserid();
            if (!isLogin())
                userId = "Not Login";
            CrashReport.setUserId(userId);// 记录当前是谁上报的
        } catch (Exception e) {
        }
    }

    /**
     * 保存登录信息(登录成功后调用此方法)
     *
     * @param account       账号
     * @param password      密码
     * @param loginResponse 用户信息
     */
//    public void saveAccountInfo(String account, String password, LoginResponse loginResponse) {
//        spHelper.put(ACCOUNT, account);
//        spHelper.put(PASSWORD, password);
//        updateAccountInfo(loginResponse);
//    }

    /**
     * 更新登录信息(登录成功后调用此方法)
     *
     * @param loginResponse 用户信息
     */
//    public void updateAccountInfo(LoginResponse loginResponse) {
//        spHelper.put(TOKEN, loginResponse.getToken());
//        spHelper.put(USERID, loginResponse.getUserId());
//        spHelper.put(USER_NAME, loginResponse.getUserName());
//        spHelper.put(PHOTOURL, loginResponse.getAccountImage());
//        spHelper.put(RECYCLE_PHONE, loginResponse.getUserPhone());
//        spHelper.put(ORG_NAME, loginResponse.getOrgName());
//        spHelper.put(IS_PRINCIPAL, loginResponse.getIsPrincipal());
//
//        updateBugly();
//    }

    /**
     * 清除账号信息(手动点击退出登录后调用此方法)
     */
    public void clearAccountInfo() {
        spHelper.put(ACCOUNT, "");
        spHelper.put(PASSWORD, "");
        spHelper.put(TOKEN, "");
        spHelper.put(USER_NAME, "");
        spHelper.put(USERID, "");
        spHelper.put(PHOTOURL, "");
        spHelper.put(RECYCLE_PHONE, "");

        updateBugly();
    }

    /**
     * 设置Token
     *
     * @param token token
     */
    public void setToken(String token) {
        spHelper.put(TOKEN, token);
    }

    /**
     * 获取用户名称(昵称)
     *
     * @return 如果为空则返回账号
     */
    public String getUserName() {
        String username = spHelper.getPref(USER_NAME, "");
        if (TextUtils.isEmpty(username)) {
            username = spHelper.getPref(ACCOUNT, "");
        }
        return username;
    }

    /**
     * 获取账号
     *
     * @return 回调
     */
    public String getAccount() {
        return spHelper.getPref(ACCOUNT, "");
    }

    /**
     * 获取电话
     */
    public String getCurrPhone() {
        return spHelper.getPref(RECYCLE_PHONE, "");
    }

    /**
     * 获取密码
     *
     * @return 回调
     */
    public String getPassword() {
        return spHelper.getPref(PASSWORD, "");
    }

    /**
     * 获取Token
     *
     * @return 返回数据
     */
    public String getToken() {
        String str = spHelper.getPref(TOKEN, "");
        Timber.i("RetrofitFactoty：Token=" + str);
        return str;
    }

    /**
     * 获取头像URL
     *
     * @return 返回数据
     */
    public String getPhotoUrl() {
        return spHelper.getPref(PHOTOURL, "");
    }

    /**
     * 更新头像URL
     */
    public void setPhotoUrl(String url) {
        spHelper.put(PHOTOURL, url);
    }


    /**
     * 更新定位信息(经纬度)
     *
     * @param latitude  纬度
     * @param longitude 经度
     * @param radius    半径
     * @param address   详细地址
     */
    public void updateLocation(double latitude, double longitude, float radius, String address) {
        spHelper.put(LATITUDE, String.valueOf(latitude));
        spHelper.put(LONGITUDE, String.valueOf(longitude));
        spHelper.put(RADIUS, radius);
        spHelper.put(ADDRESS, address);
    }

    /**
     * 获取用户id
     *
     * @return 返回数据
     */
    public String getUserid() {
        return spHelper.getPref(USERID, "");
    }


    /**
     * 当前是否登录
     *
     * @return token存在则表示已登录(返回true)否则未登录(返回false)
     */
    public boolean isLogin() {
        String token = spHelper.getPref(TOKEN, "");
        if (TextUtils.isEmpty(token)) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 保存APP启动次数
     */
    public void setStartTime(int num) {
        spHelper.put(START_TIME, num);
    }


    /**
     * 获取APP启动次数，如果达到一定次数开始清理日志
     */
    public int getStartTime() {
        return spHelper.getPref(START_TIME, 0);
    }


    /**
     * 获取经度
     *
     * @return 回调
     */
    public double getLongitude() {
        return Double.parseDouble(spHelper.getPref(LONGITUDE, ""));
    }

    /**
     * 获取纬度
     *
     * @return 回调
     */
    public double getLatitude() {
        return Double.parseDouble(spHelper.getPref(LATITUDE, ""));
    }

    /**
     * 获取半径
     *
     * @return 回调
     */
    public float getRadius() {
        return spHelper.getPref(RADIUS, 0);
    }

    /**
     * 获取定位地址
     *
     * @return 回调
     */
    public String getAddress() {
        return spHelper.getPref(ADDRESS, "");
    }
}
