package com.sam.smartbutler.ui;

import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.Toast;

/**
 * 项目名：SmartButler
 * 包名：  com.sam.smartbutler.ui
 * 文件名：BaseActivity
 * 创建者：Sam
 * 创建时间：2017/11/13 16:19
 * 描述：Activity基类
 */

/**
 * 主要做的一些事情：
 * 1.统一属性
 * 2.统一方法
 * 3.统一接口
 */
public class BaseActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //显示返回键
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //android5.0以上去除阴影
        if (Build.VERSION.SDK_INT>=21){
            getSupportActionBar().setElevation(0);
        }

    }

    //菜单栏操作
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

}
