package com.sam.smartbutler.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.http.VolleyError;
import com.sam.smartbutler.R;
import com.sam.smartbutler.adapter.GirlAdapter;
import com.sam.smartbutler.entity.GirlData;
import com.sam.smartbutler.utils.L;
import com.sam.smartbutler.utils.PicassoUtils;
import com.sam.smartbutler.utils.StaticClass;
import com.sam.smartbutler.view.CustomDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * 项目名：SmartButler
 * 包名：  com.sam.smartbutler.fragment
 * 文件名：ButlerFragment
 * 创建者：Sam
 * 创建时间：2017/11/13 17:28
 * 描述：美女社区
 */

public class GrilFragment extends Fragment implements AbsListView.OnScrollListener, View.OnClickListener {
    private GridView gridView;
    private List<GirlData> mList = new ArrayList<>();
    private List<String> mListUrl = new ArrayList<>();
    private PhotoViewAttacher mAttacher;
    private CustomDialog dialog;
    private ImageView imageView;
    private int width,height;
    private GirlAdapter adapter;

    private View view_more;
    private ProgressBar pb;
    private TextView tvLoad;
    private int lastVisibleIndex;
    private int currentPage = 1;
    private ProgressBar proG;
    private Button btG;
    private String url = StaticClass.GIRL_PICK;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    adapter = new GirlAdapter(getActivity(),mList);
                    gridView.setAdapter(adapter);
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

    private void setListeners() {
        gridView.setOnScrollListener(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gril,null);
        view_more = inflater.inflate(R.layout.view_more,null);
        pb = view_more.findViewById(R.id.progress);
        tvLoad = view_more.findViewById(R.id.tv_load);
        findView(view);
        return view;
    }

    private void findView(View view) {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        width = displayMetrics.widthPixels;
        height = displayMetrics.heightPixels;
        gridView = (GridView) view.findViewById(R.id.gridView);
        proG = view.findViewById(R.id.proG);
        btG = view.findViewById(R.id.btG);
        btG.setOnClickListener(this);
        dialog = new CustomDialog(getActivity(), width,height,R.layout.dialog_girl,R.style.Theme_dialog, Gravity.CENTER,R.style.pop_anim_style);
        imageView = (ImageView) dialog.findViewById(R.id.girl_image);
        getData(url);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PicassoUtils.loadImageView(getActivity(),mListUrl.get(position),imageView);
                mAttacher = new PhotoViewAttacher(imageView);
                mAttacher.update();
                dialog.show();
            }
        });
    }

    private void getData(String url) {
        proG.setVisibility(View.VISIBLE);
        //易源接口
        RxVolley.get(url, new HttpCallback() {
            @Override
            public void onSuccess(String t) {

                proG.setVisibility(View.GONE);
                parsingJson(t);
            }

            @Override
            public void onFailure(VolleyError error) {
                Toast.makeText(getActivity(), "网络异常", Toast.LENGTH_SHORT).show();
                proG.setVisibility(View.GONE);
                btG.setVisibility(View.VISIBLE);
            }
        });
    }

    private void parsingJson(String t) {
        try {
            JSONObject jsonObject = new JSONObject(t);
            JSONObject jsonResult = jsonObject.getJSONObject("showapi_res_body");
            JSONArray jsonArray = jsonResult.getJSONArray("newslist");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = (JSONObject) jsonArray.get(i);
                String url = json.getString("picUrl");
                mListUrl.add(url);
                GirlData data = new GirlData();
                data.setImageUrl(url);
                mList.add(data);
            }
            Message message = new Message();
            message.what = 0;
            handler.sendMessage(message);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void parsingJson2(String t) {
        try {
            JSONObject jsonObject = new JSONObject(t);
            JSONObject jsonResult = jsonObject.getJSONObject("showapi_res_body");
            JSONArray jsonArray = jsonResult.getJSONArray("newslist");
            for (int i = 1; i < jsonArray.length(); i++) {
                JSONObject json = (JSONObject) jsonArray.get(i);
                String url = json.getString("picUrl");
                mListUrl.add(url);
                GirlData data = new GirlData();
                data.setImageUrl(url);
                mList.add(data);
            }
            Message message = new Message();
            message.what = 1;
            handler.sendMessage(message);

        } catch (JSONException e) {
            e.printStackTrace();
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
            loadMoreData();// 加载更多数据
        }
    }

    private void loadMoreData() {
        ++currentPage;
        String url = "http://route.showapi.com/197-1?showapi_appid=50768&showapi_sign=27109ec958c34d59a873d896bb331c6b&num=10&page="+currentPage+"&";
        RxVolley.get(url, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                parsingJson2(t);
            }
        });
    }

    @Override
    public void onScroll(AbsListView absListView, int i, int i1, int i2) {
        lastVisibleIndex = i+i1;
    }

    @Override
    public void onClick(View view) {
        btG.setVisibility(View.GONE);
        Message msg = new Message();
        msg.what = 2;
        handler.sendMessage(msg);
    }
}
