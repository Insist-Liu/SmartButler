package com.sam.smartbutler.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.http.VolleyError;
import com.sam.smartbutler.R;
import com.sam.smartbutler.adapter.CourierAdapter;
import com.sam.smartbutler.entity.CourierData;
import com.sam.smartbutler.utils.L;
import com.sam.smartbutler.utils.StaticClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 项目名：SmartButler
 * 包名：  com.sam.smartbutler.ui
 * 文件名：CourierActivity
 * 创建者：Sam
 * 创建时间：2017/11/18 19:32
 * 描述：快递查询
 */

public class CourierActivity extends BaseActivity implements View.OnClickListener {

    //快递公司名称，快递单号
    private EditText et_name,et_number;
    //查询按钮
    private Button bt_courier;
    //用来显示详细内容的时间轴
    private ListView mListView;
    //存放json数据
    private List<CourierData> list = new ArrayList<>();
    //加载详细内容的进度条
    private ProgressBar pro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courier);
        initView();
    }

    private void initView() {
        et_name = (EditText) findViewById(R.id.name_courier_et);
        et_number = (EditText) findViewById(R.id.number_courier_et);
        bt_courier = (Button) findViewById(R.id.get_courier_bt);
        mListView = (ListView) findViewById(R.id.listView_courier);
        pro = (ProgressBar) findViewById(R.id.pro);

        bt_courier.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            /**
             * 1.获取输入框的内容
             * 2.判断是否为空
             * 3.拿到数据去请求数据
             * 4.解析JSON
             * 5.ListView适配器
             * 6.实体类（item）
             * 7.设置数据/显示效果
             */
            case R.id.get_courier_bt:
                pro.setVisibility(View.VISIBLE);
                String name = et_name.getText().toString().trim();
                String number = et_number.getText().toString().trim();
                if (!TextUtils.isEmpty(name)&!TextUtils.isEmpty(number)){
                    if (name.equals("顺丰")||name.equals("顺丰快递")){
                        name = "sf";
                    }

                    if (name.equals("申通")||name.equals("申通快递")){
                        name = "sto";
                    }

                    if (name.equals("圆通")||name.equals("圆通快递")){
                        name = "yt";
                    }

                    if (name.equals("韵达")||name.equals("韵达快递")){
                        name = "yd";
                    }

                    if (name.equals("天天")||name.equals("天天快递")){
                        name = "tt";
                    }
                    final String url = "http://v.juhe.cn/exp/index?key="+ StaticClass.COURIER+"&com="+name+"&no="+number;
                    RxVolley.get(url, new HttpCallback() {
                        @Override
                        public void onSuccess(String t) {
                            pro.setVisibility(View.GONE);
                            //解析JSON
                            L.i(t);
                            parsingJson(t);
                        }
                    });
                }else {
                    Toast.makeText(this, "输入框不能为空", Toast.LENGTH_SHORT).show();
                }
        }

    }

    private void parsingJson(String t) {
        try {
            JSONObject jsonObject = new JSONObject(t);
            JSONObject jsonResult = jsonObject.getJSONObject("result");
            JSONArray jsonArray = jsonResult.getJSONArray("list");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = (JSONObject) jsonArray.get(i);
                CourierData data = new CourierData();
                data.setDateTime(json.getString("datetime"));
                data.setRemark(json.getString("remark"));
                list.add(data);
            }
            Collections.reverse(list);
            CourierAdapter adapter = new CourierAdapter(this,list);
            mListView.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
