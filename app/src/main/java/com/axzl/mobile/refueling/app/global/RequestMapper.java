package com.axzl.mobile.refueling.app.global;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.axzl.mobile.refueling.app.utils.CommonUtils;
import com.jess.arms.cj.ClientInfo;
import com.jess.arms.cj.GsonRequest;
import com.jess.arms.cj.IRequestMapper;
import com.jess.arms.utils.ArmsUtils;
import com.jess.arms.utils.DeviceUtils;

import javax.inject.Inject;

/**
 * 包名： com.zqw.mobile.recycling.api
 * 对象名： RequestMapper
 * 描述：请求映射
 * 作者： wly
 * 邮箱：wangliyang206@163.com
 * 创建日期： 2017/3/24 14:36
 */

public final class RequestMapper implements IRequestMapper {

    private Context context;
    private AccountManager accountManager;

    @Inject
    public RequestMapper(Context context, AccountManager accountManager) {
        this.context = context;
        this.accountManager = accountManager;
    }

    @Override
    public <T> GsonRequest<T> transform(T t) {
        GsonRequest<T> request = new GsonRequest<>();
        String token = accountManager.getToken();
        request.setUserid(accountManager.getUserid());
        request.setData(t);
        request.setToken(token);
        request.setVersion(Constant.version);

        request.setClient(getPhoneInfo(context));
        return request;
    }

    /**
     * 获取设备信息
     *
     * @param context 句柄
     * @return 返回 设备信息
     */
    private static ClientInfo getPhoneInfo(Context context) {
        ClientInfo loginBeanIn = new ClientInfo();
        loginBeanIn.setCell(CommonUtils.getPhoneNumber(context));
        loginBeanIn.setDeviceid(DeviceUtils.getIMEI(context));
        loginBeanIn.setSimid(CommonUtils.getSimSerialNumber(context));
        loginBeanIn.setOs("android");
        loginBeanIn.setOsver(android.os.Build.VERSION.SDK_INT + "");
        loginBeanIn.setPpiheight(String.valueOf(ArmsUtils.getScreenHeidth(context)));
        loginBeanIn.setPpiwidth(String.valueOf(ArmsUtils.getScreenWidth(context)));
        loginBeanIn.setOsver(android.os.Build.VERSION.SDK_INT + "");
        // loginBeanIn.setDeskey(StringUtil.getRand(8));

        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            loginBeanIn.setVercode(pi.versionCode + "");
            loginBeanIn.setVername(pi.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return loginBeanIn;
    }
}
