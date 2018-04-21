package com.sam.smartbutler.entity;

import cn.bmob.v3.BmobUser;

/**
 * 项目名：SmartButler
 * 包名：  com.sam.smartbutler.entity
 * 文件名：MyUser
 * 创建者：Sam
 * 创建时间：2017/11/15 18:12
 * 描述：用户属性
 */

public class MyUser extends BmobUser {
    private int age;
    private String desc;
    private boolean sex;
    private String nickName;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public boolean isSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
