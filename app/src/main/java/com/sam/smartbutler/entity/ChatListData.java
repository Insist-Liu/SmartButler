package com.sam.smartbutler.entity;

/**
 * 项目名：SmartButler
 * 包名：  com.sam.smartbutler.entity
 * 文件名：ChatListData
 * 创建者：Sam
 * 创建时间：2017/11/20 0:09
 * 描述：对话列表实体
 */

public class ChatListData {
    private int type;
    private String text;

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
}
