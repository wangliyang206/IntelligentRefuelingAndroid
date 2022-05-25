package com.axzl.mobile.refueling.mvp.model.entity;


import com.contrarywind.interfaces.IPickerViewData;

/**
 * Created by Sai on 15/11/22.
 */
public class HourBean implements IPickerViewData {
    private long id;
    private String name;

    public HourBean(long id, String name){
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    //这个用来显示在PickerView上面的字符串,PickerView会通过getPickerViewText方法获取字符串显示出来。
    @Override
    public String getPickerViewText() {
        return name;
    }
}
