package com.sam.smartbutler.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.sam.smartbutler.MainActivity;
import com.sam.smartbutler.R;
import com.sam.smartbutler.entity.MyUser;
import com.sam.smartbutler.utils.ShareUtils;
import com.sam.smartbutler.utils.ShowApiUtil;
import com.sam.smartbutler.utils.StaticClass;
import com.sam.smartbutler.utils.UtilTools;

import cn.bmob.v3.BmobUser;

/**
 * 项目名：SmartButler
 * 包名：  com.sam.smartbutler.ui
 * 文件名：SplashActivity
 * 创建者：Sam
 * 创建时间：2017/11/14 10:02
 * 描述：闪屏页
 */

public class SplashActivity extends AppCompatActivity {

    MyUser userInfo = BmobUser.getCurrentUser(MyUser.class);
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case StaticClass.HANDLER_SPLASH:
                    if (isFirst()) {
                        startActivity(new Intent(SplashActivity.this, Guide.class));
                    } else {
                        //缓存用户登录密码和账号

                        if (userInfo != null) {
                            startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        } else {
                            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                        }
                    }
                    finish();
                    break;
            }
        }
    };
    /**
     * 1.延时两秒
     * 2.判断是否第一次运行
     * 3.自定义字体
     * 4.全屏显示
     */

    private ImageView imageView;
    private TextView saying;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initView();

    }

    private void initView() {
        imageView = findViewById(R.id.iv_show_pic);
        saying = findViewById(R.id.tv_show_saying);

        //加载一句励志语句
        new ShowAsyncTask().execute(ShowApiUtil.SAYING);
        //splash动画
        startAnim();
    }

    private void startAnim() {
        ObjectAnimator animatorX = ObjectAnimator.ofFloat(imageView, "scaleX", 1f, 1.15f);
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(imageView, "scaleY", 1f, 1.15f);
        AnimatorSet set = new AnimatorSet();
        set.setDuration(2000).play(animatorX).with(animatorY);
        set.start();
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                handler.sendEmptyMessageDelayed(StaticClass.HANDLER_SPLASH,1000);
            }
        });
    }

    //判断程序是否第一次运行
    private boolean isFirst() {
        boolean isFirst = ShareUtils.getBoolean(this, StaticClass.SHARE_IS_FIRST, true);
        if (isFirst) {
            ShareUtils.putBoolean(SplashActivity.this, StaticClass.SHARE_IS_FIRST, false);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }

    //使用基本的AsyncTask处理网络请求
    class ShowAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            return ShowApiUtil.parseJsonFromSaying(ShowApiUtil.getData(strings[0]));
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s != null) {
                saying.setText(s);
            } else {
                saying.setText("消磨你的无聊时光");
            }
        }
    }
}
