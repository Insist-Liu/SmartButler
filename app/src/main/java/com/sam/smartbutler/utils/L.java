package com.sam.smartbutler.utils;

import android.util.Log;

/**
 * 项目名：SmartButler
 * 包名：  com.sam.smartbutler.utils
 * 文件名：L
 * 创建者：Sam
 * 创建时间：2017/11/14 0:17
 * 描述：Log封装类
 */

public class L {

    //开关
    public static final boolean DEBUG = false;
    //TAG
    private static final String TAG = "SmartButler";

    //五个等级 DIWEF

    public static void d(String text) {
        if (DEBUG) {
            Log.d(TAG, text);
        }
    }

    public static void i(String text) {
        if (DEBUG) {
            Log.i(TAG, text);
        }
    }

    public static void w(String text) {
        if (DEBUG) {
            Log.w(TAG, text);
        }
    }

    public static void e(String text) {
        if (DEBUG) {
            Log.e(TAG, text);
        }
    }
}
