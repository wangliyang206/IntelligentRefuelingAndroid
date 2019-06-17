package com.axzl.mobile.refueling.mvp.model.entity;

import android.graphics.drawable.Drawable;

/** 应用程序 */
public class AppInfo {
    private Drawable icon;
    private String name;
    private String pageName;

    /**
     * true是在内部 false在sdcard
     */
    private boolean isRow;
    /**
     * true 是用户程序 false 是系统程序
     */
    private boolean isUser;

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    public boolean isRow() {
        return isRow;
    }

    public void setRow(boolean isRow) {
        this.isRow = isRow;
    }

    public boolean isUser() {
        return isUser;
    }

    public void setUser(boolean isUser) {
        this.isUser = isUser;
    }

    @Override
    public String toString() {
        return "AppInfo [name=" + name + ", pageName=" + pageName + ", isRow="
                + isRow + ", isUser=" + isUser + "]";
    }

}
