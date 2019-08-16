package com.axzl.mobile.refueling.app.global;

import com.blankj.utilcode.util.SDCardUtils;

/**
 * 包名： com.zqw.mobile.recyclingandroid.app.global
 * 对象名： Constant
 * 描述：公共设置
 * 作者： wly
 * 邮箱：wangliyang206@163.com
 * 创建日期： 2017/10/18 14:47
 */

public interface Constant {
    /*----------------------------------------------APP SdCard目录地址-------------------------------------------------*/

    /**
     * 图片路径
     */
    String IMAGE_PATH = SDCardUtils.getSDCardPathByEnvironment() + "/Refueling/Image/";

    /**
     * 业务缓存目录
     */
    String CACHE_PATH = SDCardUtils.getSDCardPathByEnvironment() + "/Refueling/Cache/";

    /**
     * APP升级路径
     */
    String APP_UPDATE_PATH = SDCardUtils.getSDCardPathByEnvironment() + "/Refueling/AppUpdate/";

    /**
     * log路径
     */
    String LOG_PATH = SDCardUtils.getSDCardPathByEnvironment() + "/Refueling/Log/";

    /**
     * 视频缓存路径
     */
    String VIDEO_PATH = SDCardUtils.getSDCardPathByEnvironment() + "/Refueling/Video/";

    /*----------------------------------------------------业务变量-------------------------------------------------------*/
    /**
     * API版本号
     */
    int version = 1;

    /**
     * 默认展示20条
     */
    int PAGESIZE = 20;

    /**
     * 周一至周五使用的图标
     */
    String ICON_SINGLE_DAY = "com.axzl.mobile.refueling.IconNormal";

    /**
     * 周六、周日使用的图标
     */
    String ICON_DOUBLE_DAY = "com.axzl.mobile.refueling.IconFestival";

    /**
     * Bmob后端云
     */
    String BMOB_APPLICATION_KEY = "e7db8235cfd89bf48fda19ebab15e146";

    /*----------------------------------------------------首页业务变量-------------------------------------------------------*/
    /** 首页 */
    int MAIN_HOME = 1;

    /** 抽奖之幸运转盘 */
    int MAIN_LOTTERY_WHEELFORTUNE = 2;
    /** 抽奖之QQ积分 */
    int MAIN_LOTTERY_QQ = 3;
    /** 抽奖之原生转盘抽奖 */
    int MAIN_LOTTERY_WHEELSURF = 4;

    /** Icon视图 之 FontAwesome */
    int MAIN_ICON_FONTAWESOME = 5;
    /** Icon视图 之 GoogleMaterial */
    int MAIN_ICON_GOOGLEMATERIAL = 6;
    /** Icon视图 之 Octicons */
    int MAIN_ICON_OCTICONS = 7;

    /** 关于 */
    int MAIN_ABOUT = 9;

    /** 设置 */
    int MAIN_SETTING = 10;
}
