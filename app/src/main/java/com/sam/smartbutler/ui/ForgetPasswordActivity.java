package com.sam.smartbutler.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sam.smartbutler.R;
import com.sam.smartbutler.entity.MyUser;
import com.sam.smartbutler.utils.L;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;


/**
 * 项目名：SmartButler
 * 包名：  com.sam.smartbutler.ui
 * 文件名：ForgetPasswordActivity
 * 创建者：Sam
 * 创建时间：2017/11/16 1:18
 * 描述：忘记/重置密码
 */

public class ForgetPasswordActivity extends BaseActivity implements View.OnClickListener {
    //原密码
    private EditText et_pass;
    //新密码
    private EditText et_newPass;
    //确认新密码
    private EditText et_again;
    //邮箱
    private EditText et_email;
    //修改密码
    private Button bt_modify;
    //忘记密码
    private Button bt_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        initView();
    }

    private void initView() {
        et_pass = (EditText) findViewById(R.id.pass_forget_ed);
        et_newPass = (EditText) findViewById(R.id.newPass_forget_ed);
        et_again = (EditText) findViewById(R.id.againPass_forget_ed);
        et_email = (EditText) findViewById(R.id.email_forget_et);
        bt_modify = (Button) findViewById(R.id.modifyPass_forget_ed);
        bt_back = (Button) findViewById(R.id.back_forget_bt);

        bt_modify.setOnClickListener(this);
        bt_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.modifyPass_forget_ed:
                modifyPassword();
                break;
            case R.id.back_forget_bt:
                resetPassword();
                break;
        }
    }

    //找回密码
    private void resetPassword() {
        final String email = et_email.getText().toString().trim();
        if (!TextUtils.isEmpty(email)){
            MyUser.resetPasswordByEmail(email, new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null){
                        Toast.makeText(ForgetPasswordActivity.this, "邮件已发送到"+email, Toast.LENGTH_SHORT).show();
                        finish();
                }else {
                        Toast.makeText(ForgetPasswordActivity.this, "邮件发送失败", Toast.LENGTH_SHORT).show();
                    }
            }});
        }else {
            Toast.makeText(this, "输入框不能为空", Toast.LENGTH_SHORT).show();
        }
    }

    //修改密码
    private void modifyPassword() {
         String password = et_pass.getText().toString().trim();
         String newPassword = et_newPass.getText().toString().trim();
         String again = et_again.getText().toString().trim();
        //判断输入框是否为空
        if (!TextUtils.isEmpty(password)&!TextUtils.isEmpty(newPassword)
                &!TextUtils.isEmpty(again)){
            if (again.equals(newPassword)){
                MyUser.updateCurrentUserPassword(password, newPassword, new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null){
                            Toast.makeText(ForgetPasswordActivity.this, "修改密码成功", Toast.LENGTH_SHORT).show();
                            finish();
                        }else {
                            L.i(e.getMessage());
                            Toast.makeText(ForgetPasswordActivity.this, "修改密码失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }else {
                Toast.makeText(this, "两次输入的密码不一致", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(this, "输入框不能为空", Toast.LENGTH_SHORT).show();
        }
    }
}
