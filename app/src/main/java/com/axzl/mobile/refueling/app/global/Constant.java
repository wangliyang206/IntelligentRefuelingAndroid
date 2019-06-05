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
     * 是否是消息通知打开的
     */
    String IS_MSG_OPEN = "isMsgOpen";

    /**
     * 接收自定义消息
     */
    String MESSAGE_RECEIVED_ACTION = "com.axzl.mobile.refueling.MESSAGE_RECEIVED_ACTION";

    /**
     * API版本号
     */
    int version = 1;

    /*** 默认展示20条 */
    int PAGESIZE = 20;
    /*----------------------------------------------跳转设定---------------------------------------------*/

    /**
     * 选择 营业执照
     */
    int SELECT_IMG_BUSINESS_REQUESTCODE = 1;

    /**
     * 选择 危险品经营许可证
     */
    int SELECT_IMG_LICENSE_REQUESTCODE = 2;

    /**
     * 裁剪图片
     */
    int CORP_CAMERA_IMAGE = 3;

    /**
     * 裁剪图片  危险品经营许可证
     */
    int CORP_CAMERA_IMAGE_LICENSE = 4;

    /**
     * 裁剪图片  营业执照
     */
    int CORP_CAMERA_IMAGE_BUSINESS = 5;

    /** 图片参数key */
    String IMAGE_URL = "IMAGE_URL";
}
