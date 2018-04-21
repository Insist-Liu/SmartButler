package com.sam.smartbutler.ui;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.sam.smartbutler.R;
import com.sam.smartbutler.service.SmsService;
import com.sam.smartbutler.utils.L;
import com.sam.smartbutler.utils.ShareUtils;
import com.sam.smartbutler.utils.StaticClass;
import com.xys.libzxing.zxing.activity.CaptureActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名：SmartButler
 * 包名：  com.sam.smartbutler.ui
 * 文件名：SettingActivity
 * 创建者：Sam
 * 创建时间：2017/11/13 20:45
 * 描述：设置
 */

public class SettingActivity extends BaseActivity implements View.OnClickListener {
    private Switch sw_speak;
    private Switch sw_sms;
    private LinearLayout ll_update;
    private TextView tv_version;

    private String versionName;
    private int versionCode;
    private String url;

    private LinearLayout ll_scan, ll_qr;
    private TextView tv_scan_result;

    //我的位置
    private LinearLayout ll_my_local;

    //关于软件
    private LinearLayout ll_about;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initView();
    }

    private void initView() {
        sw_speak = (Switch) findViewById(R.id.sw_speak);
        sw_speak.setOnClickListener(this);
        boolean isSpeak = ShareUtils.getBoolean(this, "isSpeak", false);
        sw_speak.setChecked(isSpeak);

        sw_sms = (Switch) findViewById(R.id.sw_sms);
        sw_sms.setOnClickListener(this);
        boolean isSms = ShareUtils.getBoolean(this, "isSms", false);
        sw_sms.setChecked(isSms);

        ll_update = (LinearLayout) findViewById(R.id.ll_update);
        ll_update.setOnClickListener(this);
        tv_version = (TextView) findViewById(R.id.tv_version);

        ll_scan = (LinearLayout) findViewById(R.id.ll_scan);
        ll_scan.setOnClickListener(this);

        ll_qr = (LinearLayout) findViewById(R.id.ll_qr);
        ll_qr.setOnClickListener(this);

        ll_my_local = (LinearLayout) findViewById(R.id.ll_my_local);
        ll_my_local.setOnClickListener(this);

        ll_about = (LinearLayout) findViewById(R.id.ll_about);
        ll_about.setOnClickListener(this);

        tv_scan_result = (TextView) findViewById(R.id.tv_scan_result);

        try {
            getVersionNameCode();
            tv_version.setText("当前版本:" + versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sw_speak:
                //切换相反,设置语音功能开关
                sw_speak.setSelected(!sw_speak.isSelected());
                ShareUtils.putBoolean(this, "isSpeak", sw_speak.isChecked());
                break;
            case R.id.sw_sms:
                sw_sms.setSelected(!sw_sms.isSelected());
                ShareUtils.putBoolean(this, "isSms", sw_sms.isChecked());
                if (sw_sms.isChecked()) {
                    startService(new Intent(this, SmsService.class));
                } else {
                    stopService(new Intent(this, SmsService.class));
                }
                break;
            case R.id.ll_update:
                L.i("ll_update");
                RxVolley.get(StaticClass.CHECK_UPDATE_URL, new HttpCallback() {
                    @Override
                    public void onSuccess(String t) {
                        L.i(t);
                        parsing(t);
                    }
                });
                break;
            case R.id.ll_scan:
                startScan();
                break;
            case R.id.ll_qr:
                startGenerateQr();
                break;
            case R.id.ll_my_local:
                //获取定位所需权限
                getJurisdiction();
                break;
            case R.id.ll_about:
                startActivity(new Intent(this, AboutActivity.class));
        }
    }

    //动态获取权限
    private void getJurisdiction() {
        if (Build.VERSION.SDK_INT>22){
            String[] permissions = new String[]{
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE};
            List<String> mPermissionList = new ArrayList<>();
            mPermissionList.clear();
            for (int i = 0; i <permissions.length;i++){
                if (ContextCompat.checkSelfPermission(this,permissions[i]) != PackageManager.PERMISSION_GRANTED){
                    mPermissionList.add(permissions[i]);
                }
            }
            if (!mPermissionList.isEmpty()){
                String[] permission2 = mPermissionList.toArray(new String[mPermissionList.size()]);
                ActivityCompat.requestPermissions(this, permission2, 1011);
            }else {
                startActivity(new Intent(this, LocationActivity.class));
            }
        }else {
            startActivity(new Intent(this, LocationActivity.class));
        }
    }

    //跳转到自己的二维码页面
    private void startGenerateQr() {
        Intent intent = new Intent(SettingActivity.this, QrCodeActivity.class);
        startActivity(intent);
    }

    //开始扫描二维码
    private void startScan() {
        if (Build.VERSION.SDK_INT > 22) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},1010);
            }else {
                Intent openCameraIntent = new Intent(SettingActivity.this, CaptureActivity.class);
                startActivityForResult(openCameraIntent, 0);
            }
        } else {
            Intent openCameraIntent = new Intent(SettingActivity.this, CaptureActivity.class);
            startActivityForResult(openCameraIntent, 0);
        }
    }

    //获取权限回调


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 1010:
                    Intent openCameraIntent = new Intent(SettingActivity.this, CaptureActivity.class);
                    startActivityForResult(openCameraIntent, 0);
                break;
            case 1011:
                startActivity(new Intent(this, LocationActivity.class));
                break;
        }
    }

    //获得扫描结果
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString("result");
                Intent intent = new Intent(this, ScanResultStringActivity.class);
                intent.putExtra("scanResult", scanResult);
                startActivity(intent);
        }
    }

    private void parsing(String t) {
        L.i("parsing");
        try {
            JSONObject jsonObject = new JSONObject(t);
            String content = jsonObject.getString("content");
            int code = jsonObject.getInt("versionCode");
            String versionName = jsonObject.getString("versionName");
            url = jsonObject.getString("url");
            L.i("code" + code);
            if (code > versionCode) {
                L.i("成功");
                showUpdateDialog(content, versionName);
            } else {
                Toast.makeText(this, "无版本更新", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            L.i("异常:" + e);
            e.printStackTrace();
        }
    }

    //获取版本号，code
    private void getVersionNameCode() throws PackageManager.NameNotFoundException {
        PackageManager pm = getPackageManager();
        PackageInfo info = pm.getPackageInfo(getPackageName(), 0);
        versionName = info.versionName;
        versionCode = info.versionCode;
    }

    //版本更新窗口
    private void showUpdateDialog(String content, String versionName) {
        L.i("showUpdate");
        new AlertDialog.Builder(this)
                .setTitle("新版本:" + versionName)
                .setMessage(content)
                .setPositiveButton("更新", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(SettingActivity.this, UpdateActivity.class);
                        intent.putExtra("url", url);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
    }
}
