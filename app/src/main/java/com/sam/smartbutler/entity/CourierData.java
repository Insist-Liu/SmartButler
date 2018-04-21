package com.sam.smartbutler.entity;

/**
 * 项目名：SmartButler
 * 包名：  com.sam.smartbutler.entity
 * 文件名：CourierData
 * 创建者：Sam
 * 创建时间：2017/11/18 22:51
 * 描述：快递查询实体
 */

public class CourierData {
    //时间
    private String dateTime;
    //状态
    private String remark;
    //城市
    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

}
