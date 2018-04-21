package com.sam.smartbutler.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

/**
 * 项目名：SmartButler
 * 包名：  com.sam.smartbutler.utils
 * 文件名：PicassoUtils
 * 创建者：Sam
 * 创建时间：2017/11/24 17:28
 * 描述：picasso封装
 */

public class PicassoUtils {
    //默认加载图片
    public static void loadImageView(Context context, String url, ImageView imageView){
        Picasso.with(context).load(url).into(imageView);
    }

    //默认加载图片（可以设置宽高）
    public static void loadImageViewSize(Context context, String url,int width,int height,ImageView imageView){
        Picasso.with(context).load(url).resize(width,height).centerCrop().into(imageView);
    }

    //加载图片默认有图片
    public static void loadImageViewHolder(Context context, String url,int load,int error,ImageView imageView){
        Picasso.with(context).load(url).placeholder(load).error(error).into(imageView);
    }

    //裁剪图片

    public static void loadImageViewCrop(Context context, String url,ImageView imageView){
        Picasso.with(context).load(url).transform(new CropSquareTransformation()).into(imageView);
    }

    //按比例裁剪
    public static class CropSquareTransformation implements Transformation {
        @Override public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());
            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;
            Bitmap result = Bitmap.createBitmap(source, x, y, size, size);
            if (result != source) {
                source.recycle();
            }
            return result;
        }

        @Override public String key() { return "square()"; }
    }
}
