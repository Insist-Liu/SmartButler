package com.sam.smartbutler.adapter;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sam.smartbutler.R;
import com.sam.smartbutler.entity.WechatData;
import com.sam.smartbutler.utils.L;
import com.sam.smartbutler.utils.PicassoUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * 项目名：SmartButler
 * 包名：  com.sam.smartbutler.adapter
 * 文件名：WeChatAdapter
 * 创建者：Sam
 * 创建时间：2017/11/23 22:34
 * 描述：微信精选适配器
 */

public class WeChatAdapter extends BaseAdapter {
    private Context mContext;
    private List<WechatData> mList;
    private WechatData data;
    private int width,height;
    private LayoutInflater inflater;
    public WeChatAdapter(Context context,List<WechatData> list){
        this.mContext = context;
        this.mList = list;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
        width = metrics.widthPixels;
        height = metrics.heightPixels;
        L.i("width"+width+",height"+height);
    }
    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null){
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.wechat_item,null);
            viewHolder.img_pick = (ImageView) convertView.findViewById(R.id.img_pick);
            viewHolder.tv_source = (TextView) convertView.findViewById(R.id.tv_source);
            viewHolder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        data = mList.get(position);
        viewHolder.tv_title.setText(data.getTitle());
        viewHolder.tv_source.setText(data.getSource());
        PicassoUtils.loadImageViewSize(mContext,data.getImgUrl(),width/3,200,viewHolder.img_pick);
        return convertView;
    }

    class ViewHolder{
        private TextView tv_title,tv_source;
        private ImageView img_pick;
    }
}
