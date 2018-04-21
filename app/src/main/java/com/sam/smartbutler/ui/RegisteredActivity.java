package com.sam.smartbutler.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.sam.smartbutler.R;
import com.sam.smartbutler.entity.MyUser;
import com.sam.smartbutler.utils.L;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * 项目名：SmartButler
 * 包名：  com.sam.smartbutler.ui
 * 文件名：RegisteredActivity
 * 创建者：Sam
 * 创建时间：2017/11/15 17:08
 * 描述：注册页面
 */

public class RegisteredActivity extends BaseActivity implements View.OnClickListener {

    private EditText et_nickName;
    private EditText et_user;
    private EditText et_pass;
    private EditText et_password;
    private EditText et_email;
    private EditText et_age;
    private EditText et_desc;
    private Button bt_regiser;
    private RadioGroup rd_radio;
    private boolean isGender = true;
    private long exitTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
    }

    private void initView() {
        et_user = (EditText) findViewById(R.id.user_register_et);
        et_pass = (EditText) findViewById(R.id.pass_register_et);
        et_password = (EditText) findViewById(R.id.password_register_et);
        et_email = (EditText) findViewById(R.id.email_register_et);
        et_age = (EditText) findViewById(R.id.age_register_et);
        et_desc = (EditText) findViewById(R.id.desc_register_et);
        et_nickName = (EditText) findViewById(R.id.nickName_register_et);

        rd_radio = (RadioGroup) findViewById(R.id.radio_register);

        bt_regiser = (Button) findViewById(R.id.register_register_bt);
        bt_regiser.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.register_register_bt:
                registering();
                break;
        }
    }

    //注册
    private void registering() {
        String nickName = et_nickName.getText().toString().trim();
        String user = et_user.getText().toString().trim();
        String pass = et_pass.getText().toString().trim();
        String password = et_password.getText().toString().trim();
        String email = et_email.getText().toString().trim();
        String age = et_age.getText().toString().trim();
        String desc = et_desc.getText().toString().trim();

        //判断男女
        rd_radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.boy_register_rb){
                    isGender = true;
                }else if (checkedId == R.id.girl_register_rb){
                    isGender = false;
                }
            }
        });

        //判断输入框是否为空
        if (!TextUtils.isEmpty(user)&!TextUtils.isEmpty(pass)&!TextUtils.isEmpty(password)&
                !TextUtils.isEmpty(email)&!TextUtils.isEmpty(age)&!TextUtils.isEmpty(nickName)){

            if(Integer.parseInt(age)>200){
                Toast.makeText(this, "年龄不能大于200岁", Toast.LENGTH_SHORT).show();
            }

            //判断两次的密码是否相同
            if (pass.equals(password)){
                //判断简介是否为空
                if (TextUtils.isEmpty(desc)){
                    desc = "这个人很懒，什么都没有留下";
                }

                //注册
                MyUser bu = new MyUser();
                bu.setUsername(user);
                bu.setPassword(password);
                bu.setEmail(email);
                bu.setAge(Integer.parseInt(age));
                bu.setDesc(desc);
                bu.setSex(isGender);
                bu.setNickName(nickName);
                bu.signUp(new SaveListener<MyUser>() {
                    @Override
                    public void done(MyUser myUser, BmobException e) {
                        if (e == null){
                            Toast.makeText(RegisteredActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                            finish();
                        }else {
                            L.e(e.toString());
                            Toast.makeText(RegisteredActivity.this, "注册失败"+e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }else {
                Toast.makeText(RegisteredActivity.this, "两次输入的密码不一致", Toast.LENGTH_SHORT).show();
            }

        }else {
            Toast.makeText(RegisteredActivity.this, "输入框不能为空", Toast.LENGTH_SHORT).show();
        }
    }

}
