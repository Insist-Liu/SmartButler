package com.sam.smartbutler.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sam.smartbutler.R;
import com.sam.smartbutler.utils.ShareUtils;
import com.xys.libzxing.zxing.encoding.EncodingUtils;

import org.w3c.dom.Text;

import java.io.ByteArrayInputStream;

/**
 * 项目名：SmartButler
 * 包名：  com.sam.smartbutler.ui
 * 文件名：QrCodeActivity
 * 创建者：Sam
 * 创建时间：2017/11/28 16:28
 * 描述：生成二维码页面
 */

public class QrCodeActivity extends BaseActivity {
    //二维码
    private ImageView img;
    //总布局,内容布局
    private LinearLayout ll_qr_content, ll_img_size;
    //左边头像
    private ImageView qr_tit_img;
    //名称
    private TextView qr_name_tv;

    //获取屏幕宽高
    private int width;
    private int height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);
        initView();
    }

    private void initView() {
        width = getResources().getDisplayMetrics().widthPixels;
        height = getResources().getDisplayMetrics().heightPixels;
        img = (ImageView) findViewById(R.id.img_qr_img);
        ll_qr_content = (LinearLayout) findViewById(R.id.ll_qr_content);
        qr_tit_img = (ImageView) findViewById(R.id.qr_tit_img);
        qr_name_tv = (TextView) findViewById(R.id.qr_name_tv);
        String name = ShareUtils.getString(this, "nick_name", "");
        if (!TextUtils.isEmpty(name)) {
            qr_name_tv.setText(name);
        }
        String img_tit = ShareUtils.getString(this, "image_title", "");
        if (!TextUtils.isEmpty(img_tit)) {
            byte[] bytes = Base64.decode(img_tit, Base64.DEFAULT);
            ByteArrayInputStream is = new ByteArrayInputStream(bytes);
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            qr_tit_img.setImageBitmap(bitmap);
            Bitmap qrCodeBitmap = EncodingUtils.createQRCode("http://www.baidu.com", 600, 600, bitmap);
            img.setImageBitmap(qrCodeBitmap);
        }

    }
}
