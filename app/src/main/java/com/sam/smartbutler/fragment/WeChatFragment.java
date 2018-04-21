package com.sam.smartbutler.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.http.VolleyError;
import com.sam.smartbutler.R;
import com.sam.smartbutler.adapter.WeChatAdapter;
import com.sam.smartbutler.entity.WechatData;
import com.sam.smartbutler.ui.WebViewActivity;
import com.sam.smartbutler.utils.L;
import com.sam.smartbutler.utils.StaticClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名：SmartButler
 * 包名：  com.sam.smartbutler.fragment
 * 文件名：ButlerFragment
 * 创建者：Sam
 * 创建时间：2017/11/13 17:28
 * 描述：微信精选
 */

public class WeChatFragment extends Fragment implements AbsListView.OnScrollListener, View.OnClickListener {
    private ListView content;
    private List<WechatData> mList = new ArrayList<>();
    private List<String> mListTitle = new ArrayList<>();
    private List<String> mListUrl = new ArrayList<>();
    private WeChatAdapter adapter;

    private View view_more;
    private ProgressBar pb;
    private TextView tvLoad;
    private int lastVisibleIndex;
    String url;
    private ProgressBar proW;
    private Button btW;

    //数据总条数
    private int totalCount;
    //当前页
    private int currentPage;
    private int allNum;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    adapter = new WeChatAdapter(getActivity(),mList);
                    content.setAdapter(adapter);
                    content.addFooterView(view_more);
                    setListeners();
                    break;
                case 1:
                    adapter.notifyDataSetChanged();
                    break;
                case 2:
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    getData(url);
                    break;
            }
        }
    };
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wechat,null);
        view_more = inflater.inflate(R.layout.view_more,null);
        pb = view_more.findViewById(R.id.progress);
        tvLoad = view_more.findViewById(R.id.tv_load);
        findView(view);
        return view;
    }

    private void findView(View view) {
        content = (ListView) view.findViewById(R.id.lv_content);
        proW = view.findViewById(R.id.proW);
        btW = view.findViewById(R.id.btW);
        btW.setOnClickListener(this);
        //易源接口
        url = "http://route.showapi.com/582-2?showapi_appid=50770&showapi_sign=9c5b2cb755f34196a18847f46431705b&";
        getData(url);

        content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                L.i("item"+mListUrl.get(position));
                Intent intent = new Intent(getActivity(),WebViewActivity.class);
                intent.putExtra("title",mListTitle.get(position));
                intent.putExtra("url",mListUrl.get(position));
                startActivity(intent);
            }
        });
    }

    private void getData(String url) {
        proW.setVisibility(View.VISIBLE);
        RxVolley.get(url, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                proW.setVisibility(View.GONE);
                L.i("微信："+t);
                parsingJson(t);
            }

            @Override
            public void onFailure(VolleyError error) {
                L.i("请求失败："+error);
                Toast.makeText(getActivity(), "网络异常", Toast.LENGTH_SHORT).show();
                proW.setVisibility(View.GONE);
                btW.setVisibility(View.VISIBLE);
            }
        });
    }

    private void parsingJson(String t) {
        try {
            JSONObject jsonObject = new JSONObject(t);
            JSONObject jsonResult = jsonObject.getJSONObject("showapi_res_body");
            JSONObject jsonResult2 = jsonResult.getJSONObject("pagebean");
            totalCount = jsonResult2.getInt("allPages");
            allNum = jsonResult2.getInt("allNum");
            currentPage = jsonResult2.getInt("currentPage");
            JSONArray jsonArray = jsonResult2.getJSONArray("contentlist");
            for (int i = 0;i<jsonArray.length();i++){
                WechatData data = new WechatData();
                JSONObject json = jsonArray.getJSONObject(i);
                String title = json.getString("title");
                String source = json.getString("userName");
                String url = json.getString("url");
                data.setTitle(title);
                data.setSource(source);
                data.setImgUrl(json.getString("contentImg"));
                mList.add(data);
                mListTitle.add(title);
                mListUrl.add(url);
            }
            Message message = new Message();
            message.what=0;
            handler.sendMessage(message);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void parsingJson2(String t) {
        try {
            JSONObject jsonObject = new JSONObject(t);
            JSONObject jsonResult = jsonObject.getJSONObject("showapi_res_body");
            JSONObject jsonResult2 = jsonResult.getJSONObject("pagebean");
            totalCount = jsonResult2.getInt("allPages");
            allNum = jsonResult2.getInt("allNum");
            currentPage = jsonResult2.getInt("currentPage");
            JSONArray jsonArray = jsonResult2.getJSONArray("contentlist");
            for (int i = 1;i<jsonArray.length();i++){
                WechatData data = new WechatData();
                JSONObject json = jsonArray.getJSONObject(i);
                String title = json.getString("title");
                String source = json.getString("userName");
                String url = json.getString("url");
                data.setTitle(title);
                data.setSource(source);
                data.setImgUrl(json.getString("contentImg"));
                mList.add(data);
                mListTitle.add(title);
                mListUrl.add(url);
            }
            Message message = new Message();
            message.what=1;
            handler.sendMessage(message);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setListeners(){
        if (totalCount>currentPage){
            content.setOnScrollListener(this);
        }else {
            content.removeFooterView(view_more);
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {
        if (i == SCROLL_STATE_IDLE
                && lastVisibleIndex == adapter.getCount()) {
            /**
             * 这里也要设置为可见，是因为当你真正从网络获取数据且获取失败的时候。
             * 我在失败的方法里面，隐藏了底部的加载布局并提示用户加载失败。所以再次监听的时候需要
             * 继续显示隐藏的控件。因为我模拟的获取数据，失败的情况这里不给出。实际中简单的加上几句代码就行了。
             */
            pb.setVisibility(View.VISIBLE);
            tvLoad.setVisibility(View.VISIBLE);
            loadMoreData();// 加载更多数据
        }
    }

    private void loadMoreData() {
        ++currentPage;
        url = "http://route.showapi.com/582-2?showapi_appid=50770&showapi_sign=9c5b2cb755f34196a18847f46431705b&page="+currentPage+"&";
        RxVolley.get(url, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                L.i("微信："+t);
                parsingJson2(t);
            }

            @Override
            public void onFailure(VolleyError error) {
                L.i("请求失败："+error);
            }
        });
    }

    @Override
    public void onScroll(AbsListView absListView, int i, int i1, int i2) {
        lastVisibleIndex = i + i1 - 1;
        // 当adapter中的所有条目数已经和要加载的数据总条数相等时，则移除底部的View
        if (i2 == allNum) {
            // 移除底部的加载布局
            content.removeFooterView(view_more);
        }
    }

    @Override
    public void onClick(View view) {
        btW.setVisibility(View.GONE);
        Message msg = new Message();
        msg.what = 2;
        handler.sendMessage(msg);
    }
}
