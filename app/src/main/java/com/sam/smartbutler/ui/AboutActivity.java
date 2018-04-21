package com.sam.smartbutler.ui;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.sam.smartbutler.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.sam.smartbutler.R.id.apk_pick;

/**
 * 项目名：SmartButler
 * 包名：  com.sam.smartbutler.ui
 * 文件名：AboutActivity
 * 创建者：Sam
 * 创建时间：2017/12/1 19:34
 * 描述：关于软件
 */

public class AboutActivity extends BaseActivity {
    private CircleImageView pick;
    private ListView mListView;
    private List<String> mList = new ArrayList<>();
    private ArrayAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        initView();
    }

    private void initView() {
        pick = (CircleImageView) findViewById(apk_pick);
        mListView = (ListView) findViewById(R.id.listView);
        mList.add("应用名:智能管家");
        mList.add("版本号"+getVersion());
        adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,mList);
        mListView.setAdapter(adapter);
    }

    private String getVersion(){
        PackageManager pm = getPackageManager();
        try {
            PackageInfo info = pm.getPackageInfo(getPackageName(),0);
            return info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "获取版本号失败";
        }
    }
}
