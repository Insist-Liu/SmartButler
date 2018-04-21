package com.sam.smartbutler.ui;

import android.os.Bundle;
import android.widget.TextView;

import com.sam.smartbutler.R;

/**
 * 项目名：SmartButler
 * 包名：  com.sam.smartbutler.ui
 * 文件名：ScanResultStringActivity
 * 创建者：Sam
 * 创建时间：2017/11/28 20:03
 * 描述：TODO
 */

public class ScanResultStringActivity extends BaseActivity {
    private TextView result;
    private String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanresult_string);
        initView();
    }

    private void initView() {
        result = (TextView) findViewById(R.id.result_string_tv);
        url = getIntent().getStringExtra("scanResult");
        result.setText(url);
    }
}
