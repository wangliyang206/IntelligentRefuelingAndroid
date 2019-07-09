package com.axzl.mobile.refueling.mvp.model.entity;

import java.util.ArrayList;

/**
 * Created by yrl on 17-9-1.
 */

public class MessageBean {
    /** 系统输出 */
    public static final int TYPE_OUTPUT = 0;
    /** 用户输入 */
    public static final int TYPE_INPUT = 1;
    public static final int TYPE_WIDGET_CONTENT = 2;
    /** 平台返回 列表数据 */
    public static final int TYPE_WIDGET_LIST = 3;
    /** 平台返回 网页地址 */
    public static final int TYPE_WIDGET_WEB = 4;
    /** 平台返回 网页媒体 */
    public static final int TYPE_WIDGET_MEDIA = 5;
    /** 二维码 */
    public static final int TYPE_QR_CODE = 6;

    private int type;
    private String text;
    private String title;
    private String subTitle;
    private String imgUrl;
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private ArrayList<MessageBean> mWidgetListItem = new ArrayList<>();
    private int mCurrentPage = 1;
    private boolean mFirstLayout = true;

    /** 每页的条数 */
    private int itemsPerPage;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public void addMessageBean(MessageBean bean) {
        mWidgetListItem.add(bean);
    }

    public int getCurrentPage() {
        return mCurrentPage;
    }

    public void setCurrentPage(int mCurrentPage) {
        this.mCurrentPage = mCurrentPage;
    }

    public boolean isFirstLayout() {
        return mFirstLayout;
    }

    public void setFirstLayout(boolean mFirstLayout) {
        this.mFirstLayout = mFirstLayout;
    }

    public ArrayList<MessageBean> getMessageBeanList() {
        return mWidgetListItem;
    }

    public int getItemsPerPage() {
        return itemsPerPage;
    }

    public void setItemsPerPage(int itemsPerPage) {
        this.itemsPerPage = itemsPerPage;
    }
}
