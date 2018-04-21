package com.sam.smartbutler.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * 项目名：SmartButler
 * 包名：  com.sam.smartbutler.utils
 * 文件名：UtilTools
 * 创建者：Sam
 * 创建时间：2017/11/14 10:01
 * 描述：工具统一类
 */

public class UtilTools {
    //设置字体
    public static void setFont(Context context, TextView textView){
        Typeface typeface = Typeface.createFromAsset(context.getAssets(),"fonts/FONT.TTF");
        textView.setTypeface(typeface);
    }

    //保存图片到shareutils
    public static void putImageToShare(Context context, ImageView imageView){
        //保存
        BitmapDrawable bitmapDrawable = (BitmapDrawable) imageView.getDrawable();
        Bitmap bitmap = bitmapDrawable.getBitmap();
        //1.将Bitmap压缩成字节数组输出流
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,80,out);
        //2.利用Base64,将字节数组输出流转换成String
        byte[] byteArray = out.toByteArray();
        String imageFile = new String(Base64.encode(byteArray,Base64.DEFAULT));
        //将Sting保存到sharedPreferences
        ShareUtils.putString(context,"image_title",imageFile);
    }

    //从shareutils读取图片
    public static void getImageToShare(Context context,ImageView imageView){
        //1.拿到String
        String imgString = ShareUtils.getString(context,"image_title","");
        if (!imgString.equals("")){
            //2.利用Base64将String转换成byte
            byte[] byteArray = Base64.decode(imgString,Base64.DEFAULT);
            ByteArrayInputStream input = new ByteArrayInputStream(byteArray);
            //3.生成bitmap
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            imageView.setImageBitmap(bitmap);
        }
    }
}
