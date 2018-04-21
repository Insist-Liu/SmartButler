package com.sam.smartbutler.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.SmsMessage;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.sam.smartbutler.R;
import com.sam.smartbutler.utils.L;
import com.sam.smartbutler.utils.StaticClass;
import com.sam.smartbutler.view.DispatchLinearLayout;

/**
 * 项目名：SmartButler
 * 包名：  com.sam.smartbutler.service
 * 文件名：SmsService
 * 创建者：Sam
 * 创建时间：2017/11/26 15:56
 * 描述：TODO
 */

public class SmsService extends Service implements View.OnClickListener {
    private SmsReceiver smsReceiver;
    //发送人号码
    private String smsPhone;
    //短信内容
    private String smsContent;
    //窗口管理
    private WindowManager wm;
    //窗口布局参数
    private WindowManager.LayoutParams layoutParams;
    //自定义view
    private DispatchLinearLayout view;
    private TextView tv_phone,tv_content;
    private Button btn_send_sms;
    private HomeWatchReceiver home;

    public static final String SYSTEM_DIALOGS_REASON_KEY = "reason";
    public static final String SYSTEM_DIALOGS_HOME_KEY = "homekey";
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        L.i("onCreate");
        init();
    }

    private void init() {
        //动态注册广播
        smsReceiver = new SmsReceiver();
        IntentFilter intentFilter = new IntentFilter();
        //添加action
        intentFilter.addAction(StaticClass.SMS_ACTION);
        //设置优先级
        intentFilter.setPriority(Integer.MAX_VALUE);
        //动态注册
        registerReceiver(smsReceiver,intentFilter);

        home = new HomeWatchReceiver();
        IntentFilter filter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        registerReceiver(home,filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        L.i("onDestroy");
        unregisterReceiver(smsReceiver);
        unregisterReceiver(home);
    }

    public class SmsReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            //获取action
            String action = intent.getAction();
            if (StaticClass.SMS_ACTION.equals(action)){
                //获取短信内容，返回的是一个Object数组
                Object[] objs = (Object[]) intent.getExtras().get("pdus");
                //遍历数组，得到相关的内容
                for(Object obj:objs){
                    SmsMessage sms = SmsMessage.createFromPdu((byte[]) obj);
                    smsPhone = sms.getOriginatingAddress();
                    smsContent = sms.getMessageBody();
                    showWindow();
                }
            }else {
                L.i("不匹配");
            }
        }
    }

    private void showWindow() {
        //获取系统服务，初始化windowManager
        wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        //获取布局参数
        layoutParams = new WindowManager.LayoutParams();
        //定义宽高
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        //定义标识
        layoutParams.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON|
                WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
        //定义格式
        layoutParams.format = PixelFormat.TRANSPARENT;
        //定义类型
        layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        view = (DispatchLinearLayout) View.inflate(getApplicationContext(), R.layout.sms_item,null);
        tv_phone = (TextView) view.findViewById(R.id.tv_phone);
        tv_content= (TextView) view.findViewById(R.id.tv_content);
        btn_send_sms = (Button) view.findViewById(R.id.btn_send_sms);
        tv_phone.setText("发件人："+smsPhone);
        tv_content.setText(smsContent);
        btn_send_sms.setOnClickListener(this);
        wm.addView(view,layoutParams);

        view.setDispatchKeyEventListener(listener);
    }
    private DispatchLinearLayout.DispatchKeyEventListener listener = new DispatchLinearLayout.DispatchKeyEventListener() {
        @Override
        public boolean dispatchKeyEvent(KeyEvent event) {
            if (event.getKeyCode() == KeyEvent.KEYCODE_BACK){
                L.i("我点击了back");
                if (view.getParent() != null){
                    wm.removeView(view);
                }
                return true;
            }
            return false;
        }
    };


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_send_sms:
                sendSms();
                if (view.getParent() != null){
                    wm.removeView(view);
                }
                break;
        }
    }

    private void sendSms() {
        Uri uri = Uri.parse("smsto:"+smsPhone);
        Intent intent = new Intent(Intent.ACTION_SENDTO,uri);
        //service里面要设置启动模式
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("sms_body","");
        startActivity(intent);
    }

    //监听Home键的广播
    class HomeWatchReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)){
                String reason = intent.getStringExtra(SYSTEM_DIALOGS_REASON_KEY);
                if (SYSTEM_DIALOGS_HOME_KEY.equals(reason)){
                    L.i("我点击了home");
                    if (view.getParent() != null){
                        wm.removeView(view);
                    }
                }
            }
        }
    }
}
