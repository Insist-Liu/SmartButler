package com.sam.smartbutler.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.sam.smartbutler.R;
import com.sam.smartbutler.utils.StaticClass;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 项目名：SmartButler
 * 包名：  com.sam.smartbutler.ui
 * 文件名：PhoneActivity
 * 创建者：Sam
 * 创建时间：2017/11/19 17:47
 * 描述：归属地查询
 */

public class PhoneActivity extends BaseActivity implements View.OnClickListener {

    //输入号码文本框
    private EditText et_number;
    //网络请求回来的图片
    private ImageView iv_company;
    //网络请求回来的内容
    private TextView tv_result;
//    private Button btn_1,btn_2,btn_3,btn_4,btn_5,btn_6,btn_7,btn_8,btn_9,
//            btn_0,btn_del,btn_query;
    private Button btn_query;
    private boolean flag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);
        initView();
    }

    private void initView() {
        et_number = (EditText) findViewById(R.id.et_number);
        iv_company = (ImageView) findViewById(R.id.iv_company);
        tv_result = (TextView) findViewById(R.id.tv_result);
        btn_query = (Button) findViewById(R.id.btn_query);
        btn_query.setOnClickListener(this);
//        btn_0 = (Button) findViewById(R.id.btn_0);
//        btn_1 = (Button) findViewById(R.id.btn_1);
//        btn_2 = (Button) findViewById(R.id.btn_2);
//        btn_3 = (Button) findViewById(R.id.btn_3);
//        btn_4 = (Button) findViewById(R.id.btn_4);
//        btn_5 = (Button) findViewById(R.id.btn_5);
//        btn_6 = (Button) findViewById(R.id.btn_6);
//        btn_7 = (Button) findViewById(R.id.btn_7);
//        btn_8 = (Button) findViewById(R.id.btn_8);
//        btn_9 = (Button) findViewById(R.id.btn_9);
//        btn_del = (Button) findViewById(R.id.btn_del);
//        btn_query = (Button) findViewById(R.id.btn_query);
//
//        btn_0.setOnClickListener(this);
//        btn_1.setOnClickListener(this);
//        btn_2.setOnClickListener(this);
//        btn_3.setOnClickListener(this);
//        btn_4.setOnClickListener(this);
//        btn_5.setOnClickListener(this);
//        btn_6.setOnClickListener(this);
//        btn_7.setOnClickListener(this);
//        btn_8.setOnClickListener(this);
//        btn_9.setOnClickListener(this);
//        btn_del.setOnClickListener(this);
//        btn_query.setOnClickListener(this);
//        btn_del.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                et_number.setText("");
//                return true;
//            }
//        });
    }

    @Override
    public void onClick(View v) {
        String str = et_number.getText().toString().trim();
        switch (v.getId()){
            case R.id.btn_query:
                if (!TextUtils.isEmpty(str)){
                    if (str.length()==11){
                        getPhone(str);
                    }else {
                        Toast.makeText(this, "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(this, "输入框不能为空", Toast.LENGTH_SHORT).show();
                }
                break;
//            case R.id.btn_0:
//            case R.id.btn_1:
//            case R.id.btn_2:
//            case R.id.btn_3:
//            case R.id.btn_4:
//            case R.id.btn_5:
//            case R.id.btn_6:
//            case R.id.btn_7:
//            case R.id.btn_8:
//            case R.id.btn_9:
//                if (flag){
//                    flag = false;
//                    str = "";
//                    et_number.setText("");
//                }
//                //每次结尾添加1
//                et_number.setText(str+((Button)v).getText());
//                //移动光标
//                et_number.setSelection(str.length()+1);
//                break;
//            case R.id.btn_del:
//                if (!TextUtils.isEmpty(str)){
//                    et_number.setText(str.substring(0,str.length()-1));
//                    et_number.setSelection(str.length()-1);
//                }
//                break;
//            case R.id.btn_query:
//                tv_result.setText("");
//                if (!TextUtils.isEmpty(str)){
//                    getPhone(str);
//                }
//                break;
        }
    }

    private void getPhone(String str) {
        String url = "http://apis.juhe.cn/mobile/get?phone="+str+"&key="+ StaticClass.PHONE_KEY;
        RxVolley.get(url, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                parsingJson(t);
            }
        });
    }

    private void parsingJson(String t) {
        try {
            StringBuffer sb = new StringBuffer();
            JSONObject jsonObject = new JSONObject(t);
            JSONObject jsonResult = jsonObject.getJSONObject("result");
            String province = jsonResult.getString("province");
            String city = jsonResult.getString("city");
            String areacode = jsonResult.getString("areacode");
            String zip = jsonResult.getString("zip");
            String company = jsonResult.getString("company");
            String card = jsonResult.getString("card");

            sb.append("归属地:"+ province + city + "\n");
            sb.append("区号:"+ areacode + "\n");
            sb.append("邮编:"+ zip + "\n");
            sb.append("运营商:"+ company + "\n");
            sb.append("类型:"+ card+"1"+"\n");
            tv_result.setText(sb);

            switch (company){
                case "移动":
                    iv_company.setImageResource(R.mipmap.china_mobile);
                    break;
                case "联通":
                    iv_company.setImageResource(R.mipmap.china_unicom);
                    break;
                case "电信":
                    iv_company.setImageResource(R.mipmap.china_telecom);
                    break;
            }
            flag = true;

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
