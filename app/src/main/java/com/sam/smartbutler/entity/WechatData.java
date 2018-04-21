package com.sam.smartbutler.entity;

/**
 * 项目名：SmartButler
 * 包名：  com.sam.smartbutler.entity
 * 文件名：WechatData
 * 创建者：Sam
 * 创建时间：2017/11/23 22:34
 * 描述：微信精选实体类
 */

public class WechatData {
    //标题
    private String title;
    //新闻地址
    private String newsUrl;
    //出处
    private String source;
    //图片url
    private String imgUrl;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNewsUrl() {
        return newsUrl;
    }

    public void setNewsUrl(String newsUrl) {
        this.newsUrl = newsUrl;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
