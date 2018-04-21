package com.sam.smartbutler.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sam.smartbutler.MainActivity;
import com.sam.smartbutler.R;
import com.sam.smartbutler.entity.MyUser;
import com.sam.smartbutler.utils.ShareUtils;
import com.sam.smartbutler.view.CustomDialog;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * 项目名：SmartButler
 * 包名：  com.sam.smartbutler.ui
 * 文件名：LoginActivity
 * 创建者：Sam
 * 创建时间：2017/11/15 14:14
 * 描述：登陆页面
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    //登陆按钮
    private Button mLogin;
    //注册按钮
    private Button mResiger;
    //复选框
    private CheckBox keep_pass;
    //密码，用户名
    private EditText et_name,et_pass;
    //找回密码
    private TextView tv_forget;
    //登陆弹窗
    private CustomDialog dialog;

    private long exitTime;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView() {
        keep_pass = (CheckBox) findViewById(R.id.keep_password);
        mResiger = (Button) findViewById(R.id.register_login_bt);
        et_name = (EditText) findViewById(R.id.name_login_et);
        et_pass = (EditText) findViewById(R.id.pass_login_et);
        mLogin = (Button) findViewById(R.id.login_login_bt);
        tv_forget = (TextView) findViewById(R.id.forget_login_tv);
        mResiger.setOnClickListener(this);
        mLogin.setOnClickListener(this);
        //获取复选框状态
        boolean isCheck = ShareUtils.getBoolean(this,"keep_pass",false);
        //默认复选框不选中
        keep_pass.setChecked(isCheck);
        if (isCheck){
            String name = ShareUtils.getString(this,"name","");
            et_name.setText(name);
            et_pass.setText(ShareUtils.getString(this,"pass",""));
        }
        //但是如果预先内容为空，然后设置好内容，这种情况下光标自然会在文字的开头
        // ，所以这种情况下可以这样做让光标位于末尾
        et_name.requestFocus();
        tv_forget.setOnClickListener(this);
        dialog = new CustomDialog(this,100,100,R.layout.dialog_loding,R.style.Theme_dialog, Gravity.CENTER,R.style.pop_anim_style);
        dialog.setCancelable(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.register_login_bt:
                startActivity(new Intent(LoginActivity.this,RegisteredActivity.class));
                break;
            case R.id.login_login_bt:
                login();
                break;
            case R.id.forget_login_tv:
                startActivity(new Intent(LoginActivity.this, ForgetPasswordActivity.class));
                break;
        }
    }

    private void login() {
        String name = et_name.getText().toString().trim();
        String pass = et_pass.getText().toString().trim();
        //判断密码用户名不为NULL
        if (!TextUtils.isEmpty(name)&!TextUtils.isEmpty(pass)){
            dialog.show();
            final MyUser user = new MyUser();
            user.setUsername(name);
            user.setPassword(pass);
            user.login(new SaveListener<MyUser>() {
                @Override
                public void done(MyUser myUser, BmobException e) {
                    dialog.dismiss();
                    if (e == null){
                        //判断邮箱是否激活
                        if (user.getEmailVerified()){
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            Toast.makeText(LoginActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
                            finish();
                        }else {
                            Toast.makeText(LoginActivity.this, "登陆失败，请激活邮箱", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(LoginActivity.this, "登陆失败", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else {
            Toast.makeText(this, "输入框不能为空", Toast.LENGTH_SHORT).show();
        }
    }

    //保存复选框状态


    @Override
    protected void onPause() {
        super.onPause();
        //设置复选框状态
        ShareUtils.putBoolean(this,"keep_pass",keep_pass.isChecked());
        //是否选中复选框
        if (keep_pass.isChecked()){
            //保存信息
            ShareUtils.putString(this,"name",et_name.getText().toString().trim());
            ShareUtils.putString(this,"pass",et_pass.getText().toString().trim());
        }else {
            ShareUtils.deleShare(this,"name");
            ShareUtils.deleShare(this,"pass");
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            if ((System.currentTimeMillis()-exitTime)>2000){
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            }else {
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
