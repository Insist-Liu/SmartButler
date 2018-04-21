package com.sam.smartbutler.ui;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.sam.smartbutler.R;
import com.sam.smartbutler.utils.L;

/**
 * 项目名：SmartButler
 * 包名：  com.sam.smartbutler.ui
 * 文件名：WebViewActivity
 * 创建者：Sam
 * 创建时间：2017/11/24 0:01
 * 描述：新闻详情
 */

public class WebViewActivity extends BaseActivity {
    private ProgressBar bar;
    private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        initView();
    }

    private void initView() {
        bar = (ProgressBar) findViewById(R.id.pro);
        webView = (WebView) findViewById(R.id.webView);
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        final String url = intent.getStringExtra("url");
        L.i("webView:"+url);

        //设置标题
        getSupportActionBar().setTitle(title);

        //支持JS
        webView.getSettings().setJavaScriptEnabled(true);
        //支持缩放
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        //接口回掉
        webView.setWebChromeClient(new WebViewClient());
        //加载网页
        webView.loadUrl(url);
        //本地显示
        webView.setWebViewClient(new android.webkit.WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                //判断是否是安卓5.0及以上系统
                if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
                    view.loadUrl(request.getUrl().toString());
                }else {
                    view.loadUrl(request.toString());
                }
                return true;
            }
        });
    }

    public class WebViewClient extends WebChromeClient{
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            if (newProgress == 100){
                bar.setVisibility(View.GONE);
            }
        }
    }
}
