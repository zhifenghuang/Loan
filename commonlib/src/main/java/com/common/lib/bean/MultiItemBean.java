package com.common.lib.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

public class MultiItemBean implements MultiItemEntity {

    private int itemType;
    private Object bean;

    public MultiItemBean(int itemType, Object bean) {
        this.itemType = itemType;
        this.bean = bean;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public Object getBean() {
        return bean;
    }

    public void setBean(Object bean) {
        this.bean = bean;
    }

    @Override
    public int getItemType() {
        return itemType;
    }
}
