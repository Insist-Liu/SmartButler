package com.sam.smartbutler.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.sam.smartbutler.R;

/**
 * 项目名：SmartButler
 * 包名：  com.sam.smartbutler.ui
 * 文件名：ScanResultActivity
 * 创建者：Sam
 * 创建时间：2017/11/28 16:56
 * 描述：TODO
 */

public class ScanResultActivity extends BaseActivity {
    private ProgressBar bar;
    private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanresult);
        initView();
    }

    private void initView() {
        bar = (ProgressBar) findViewById(R.id.bar_scan_result);
        webView = (WebView) findViewById(R.id.webView_scan_result);
        Intent intent = getIntent();
        final String url = intent.getStringExtra("scanResult");
        //设置支持js
        webView.getSettings().setJavaScriptEnabled(true);
        //设置支持缩放
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        //设置接口回调
        webView.setWebChromeClient(new WebViewClient());
        //加载url
        webView.loadUrl(url);
        //本地加载
        webView.setWebViewClient(new android.webkit.WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, request);
            }
        });

    }

    class WebViewClient extends WebChromeClient{
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            if (newProgress ==100){
                bar.setVisibility(View.GONE);
            }
        }
    }
}
