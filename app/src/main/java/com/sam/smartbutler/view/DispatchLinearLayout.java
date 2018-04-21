package com.sam.smartbutler.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.LinearLayout;

/**
 * 项目名：SmartButler
 * 包名：  com.sam.smartbutler.view
 * 文件名：DispatchKeyEventListener
 * 创建者：Sam
 * 创建时间：2017/11/27 9:07
 * 描述：TODO
 */

public class DispatchLinearLayout extends LinearLayout {
    private DispatchKeyEventListener dispatchKeyEventListener;
    public DispatchLinearLayout(Context context) {
        super(context);
    }

    public DispatchLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DispatchLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public DispatchKeyEventListener getDispatchKeyEventListener() {
        return dispatchKeyEventListener;
    }

    public void setDispatchKeyEventListener(DispatchKeyEventListener dispatchKeyEventListener) {
        this.dispatchKeyEventListener = dispatchKeyEventListener;
    }

    //接口
    public static interface DispatchKeyEventListener{
        boolean dispatchKeyEvent(KeyEvent event);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        //如果不为空，说明被调用了，去获取事件
        if (dispatchKeyEventListener != null){
            return dispatchKeyEventListener.dispatchKeyEvent(event);
        }
        return super.dispatchKeyEvent(event);
    }
}
