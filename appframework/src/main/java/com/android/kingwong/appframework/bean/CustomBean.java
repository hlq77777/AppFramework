package com.android.kingwong.appframework.bean;

import com.contrarywind.interfaces.IPickerViewData;

/**
 * Created by KingWong on 2017/9/5.
 * 基础选项
 */

public class CustomBean implements IPickerViewData {
    private int id;
    private String customNo;
    private String customContent;

    public CustomBean(int id, String customNo, String customContent) {
        this.id = id;
        this.customNo = customNo;
        this.customContent = customContent;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getCustomNo() {
        return customNo;
    }
    public void setCustomNo(String customNo) {
        this.customNo = customNo;
    }

    public String getCustomContent() {
        return customContent;
    }
    public void setCustomContent(String customContent) {
        this.customContent = customContent;
    }

    @Override
    public String getPickerViewText() {
        return customContent;
    }

}
