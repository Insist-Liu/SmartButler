package com.sam.smartbutler.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.sam.smartbutler.R;
import com.sam.smartbutler.entity.ChatListData;

import java.util.List;

/**
 * 项目名：SmartButler
 * 包名：  com.sam.smartbutler.adapter
 * 文件名：ChatListAdapter
 * 创建者：Sam
 * 创建时间：2017/11/20 0:22
 * 描述：管家机器人适配器
 */

public class ChatListAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private Context mContext;
    private List<ChatListData> mList;
    private ChatListData data;
    public static final int VALUE_LEFT_TEXT = 1;
    public static final int VALUE_RIGHT_TEXT = 2;
    public ChatListAdapter(Context context,List<ChatListData> list){
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
        ViewHolderLeft viewHolderLeft = null;
        ViewHolderRight viewHolderRight = null;
        int type = getItemViewType(position);
        if (convertView == null){
            switch (type){
                case VALUE_LEFT_TEXT:
                    viewHolderLeft = new ViewHolderLeft();
                    convertView = inflater.inflate(R.layout.left_item,null);
                    viewHolderLeft.tv_left_text = (TextView) convertView.findViewById(R.id.tv_left_text);
                    convertView.setTag(viewHolderLeft);
                    break;
                case VALUE_RIGHT_TEXT:
                    viewHolderRight = new ViewHolderRight();
                    convertView = inflater.inflate(R.layout.right_item,null);
                    viewHolderRight.tv_right_text = (TextView) convertView.findViewById(R.id.tv_right_text);
                    convertView.setTag(viewHolderRight);
                    break;
            }
        }else {
            switch (type){
                case VALUE_LEFT_TEXT:
                    viewHolderLeft = (ViewHolderLeft) convertView.getTag();
                    break;
                case VALUE_RIGHT_TEXT:
                    viewHolderRight = (ViewHolderRight) convertView.getTag();
                    break;
            }
        }
        ChatListData data = mList.get(position);
        switch (type){
            case VALUE_LEFT_TEXT:
                viewHolderLeft.tv_left_text.setText(data.getText());
                break;
            case VALUE_RIGHT_TEXT:
                viewHolderRight.tv_right_text.setText(data.getText());
                break;
        }
        return convertView;
    }

    //根据数据源的position来返回要显示的item
    @Override
    public int getItemViewType(int position) {
        ChatListData data = mList.get(position);
        int type = data.getType();
        return type;
    }

    //返回所有layout的数据
    @Override
    public int getViewTypeCount() {
        return 3;
    }

    class ViewHolderLeft{
        private TextView tv_left_text;
    }

    class ViewHolderRight{
        private TextView tv_right_text;
    }
}
