package com.sam.smartbutler.application;

import android.app.Application;
import android.content.Intent;
import android.os.StrictMode;

import com.baidu.mapapi.SDKInitializer;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.sam.smartbutler.MainActivity;
import com.sam.smartbutler.entity.MyUser;
import com.sam.smartbutler.ui.LoginActivity;
import com.sam.smartbutler.utils.StaticClass;
import com.tencent.bugly.crashreport.CrashReport;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;

/**
 * 项目名：SmartButler
 * 包名：  com.sam.smartbutler.application
 * 文件名：BaseApplication
 * 创建者：Sam
 * 创建时间：2017/11/13 16:16
 * 描述：BaseApplication
 */

public class BaseApplication extends Application {

    //创建
    @Override
    public void onCreate() {
        super.onCreate();
        //初始化Bugly
        CrashReport.initCrashReport(getApplicationContext(), StaticClass.BUGLY_APP_ID, true);

        //初始化Bmob
        Bmob.initialize(this, StaticClass.BMOB_APP_ID);

        //初始化tts
        SpeechUtility.createUtility(getApplicationContext(), SpeechConstant.APPID +"=5a192de1");

        //初始化百度地图
        SDKInitializer.initialize(getApplicationContext());

//        android 7.0系统解决拍照的问题,报android.os.FileUriExposedException:file:///
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
    }
}
