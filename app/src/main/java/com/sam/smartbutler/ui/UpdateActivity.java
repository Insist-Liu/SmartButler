package com.sam.smartbutler.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.TextView;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.client.ProgressListener;
import com.kymjs.rxvolley.http.VolleyError;
import com.kymjs.rxvolley.toolbox.FileUtils;
import com.sam.smartbutler.R;
import com.sam.smartbutler.utils.L;

import java.io.File;

/**
 * 项目名：SmartButler
 * 包名：  com.sam.smartbutler.ui
 * 文件名：UpdateActivity
 * 创建者：Sam
 * 创建时间：2017/11/27 13:31
 * 描述：下载更新
 */

public class UpdateActivity extends BaseActivity {
    private TextView tv_size;
    private String url;
    private String path;
    private NumberProgressBar numBar;
    public static final int HANDLE_LOADING= 10001;
    public static final int HANDLE_OK= 10002;
    public static final int HANDLE_ON = 10003;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case HANDLE_LOADING:
                    Bundle bundle = msg.getData();
                    long transferredBytes = bundle.getLong("transferredBytes");
                    long totalSize = bundle.getLong("totalSize");
                    tv_size.setText(transferredBytes + "/" + totalSize);
                    //进度条更新
                    numBar.setProgress((int)(((float)transferredBytes/(float)totalSize)*100));
                    break;
                case HANDLE_OK:
                    tv_size.setText("下载完成");
                    startInstallApk();
                    break;
                case HANDLE_ON:
                    tv_size.setText("下载失败");
                    break;
            }
        }
    };

    private void startInstallApk() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setDataAndType(Uri.fromFile(new File(path)),"application/vnd.android.package-archive");
        L.i("intent"+intent);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        initView();
    }

    private void initView() {
        tv_size = (TextView) findViewById(R.id.tv_size);
        //下载地址
        url = getIntent().getStringExtra("url");
        //存储路径
        path = FileUtils.getSDCardPath() + "/" + System.currentTimeMillis() + ".apk";
        //进度条
        numBar = (NumberProgressBar) findViewById(R.id.number_bar);
        numBar.setMax(100);
        L.i("路径"+path);
        ProgressListener listener = new ProgressListener() {
            @Override
            public void onProgress(long transferredBytes, long totalSize) {
//                L.i("transferredBytes"+transferredBytes+",totalSize"+totalSize);
                Message m = new Message();
                m.what = HANDLE_LOADING;
                Bundle bundle = new Bundle();
                bundle.putLong("transferredBytes",transferredBytes);
                bundle.putLong("totalSize",totalSize);
                m.setData(bundle);
                handler.sendMessage(m);
            }
        };

        HttpCallback callback = new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                L.i("成功");
                handler.sendEmptyMessage(HANDLE_OK);
            }

            @Override
            public void onFailure(VolleyError error) {
                handler.sendEmptyMessage(HANDLE_ON);
            }
        };

        //下载文件
        if (!TextUtils.isEmpty(url)){
            RxVolley.download(path,url,listener,callback);
        }
    }
}
