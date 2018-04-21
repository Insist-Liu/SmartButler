package com.sam.smartbutler.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sam.smartbutler.R;
import com.sam.smartbutler.entity.CourierData;

import java.util.List;

/**
 * 项目名：SmartButler
 * 包名：  com.sam.smartbutler.adapter
 * 文件名：CourierAdapter
 * 创建者：Sam
 * 创建时间：2017/11/18 22:55
 * 描述：快递查询适配器
 */

public class CourierAdapter extends BaseAdapter {
    private Context mContext;
    private List<CourierData> mList;
    private LayoutInflater inflater;
    private CourierData data;

    public CourierAdapter(Context context,List<CourierData> list){
        this.mContext = context;
        this.mList = list;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        //第一次加载
        if (convertView ==null){
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.layout_courier_item,null);
            viewHolder.tv_remark = (TextView) convertView.findViewById(R.id.tv_remark);
            viewHolder.tv_datetime = (TextView) convertView.findViewById(R.id.tv_datetime);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        data = mList.get(position);
        viewHolder.tv_datetime.setText(data.getDateTime());
        viewHolder.tv_remark.setText(data.getRemark());
        return convertView;
    }

    class ViewHolder{
        private TextView tv_remark,tv_datetime;
    }
}
