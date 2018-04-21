package com.sam.smartbutler.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.sam.smartbutler.MainActivity;
import com.sam.smartbutler.R;
import com.sam.smartbutler.utils.UtilTools;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名：SmartButler
 * 包名：  com.sam.smartbutler.ui
 * 文件名：Guide
 * 创建者：Sam
 * 创建时间：2017/11/14 10:30
 * 描述：引导页
 */

public class Guide extends AppCompatActivity implements View.OnClickListener {

    private ViewPager mViewPager;
    private List<View> mData = new ArrayList<>();
    private View view1,view2,view3;
    private ImageView mPoint1,mPoint2,mPoint3;
    private ImageView mBack;
    private Button mButton;
    private TextView tv_pager_1,tv_pager_2,tv_pager_3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        initView();
        setPointImg(true,false,false);
    }

    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.guide_viewPager);
        view1 = View.inflate(this,R.layout.guide_item_one,null);
        view2 = View.inflate(this,R.layout.guide_item_two,null);
        view3 = View.inflate(this,R.layout.guide_item_three,null);
        mPoint1 = (ImageView) findViewById(R.id.point1);
        mPoint2 = (ImageView) findViewById(R.id.point2);
        mPoint3 = (ImageView) findViewById(R.id.point3);
        mBack = (ImageView) findViewById(R.id.guide_back);
        mButton = (Button) view3.findViewById(R.id.tree_btn);
        tv_pager_1 = (TextView) view1.findViewById(R.id.tv_pager_1);
        tv_pager_2 = (TextView) view2.findViewById(R.id.tv_pager_2);
        tv_pager_3 = (TextView) view3.findViewById(R.id.tv_pager_3);
        UtilTools.setFont(this,tv_pager_1);
        UtilTools.setFont(this,tv_pager_2);
        UtilTools.setFont(this,tv_pager_3);
        mButton.setOnClickListener(this);

        mData.add(view1);
        mData.add(view2);
        mData.add(view3);

        //帮ImageView添加监听事件
        mBack.setOnClickListener(this);

        //添加适配器
        mViewPager.setAdapter(new MyPagerAdapter());

        //添加监听
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        setPointImg(true,false,false);
                        break;
                    case 1:
                        setPointImg(false,true,false);
                        break;
                    case 2:
                        setPointImg(false,false,true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.guide_back:
            case R.id.tree_btn:
                startActivity(new Intent(Guide.this, LoginActivity.class));
                finish();
                break;
        }
    }

    //适配器
    class MyPagerAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mData.get(position));
            return mData.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mData.get(position));
        }
    }

    //底部指示器
    private void setPointImg(boolean isCheck1,boolean isCheck2,boolean isCheck3){
        if(isCheck1){
            mPoint1.setBackgroundResource(R.mipmap.point_on);
            mBack.setVisibility(View.VISIBLE);
        }else {
            mPoint1.setBackgroundResource(R.mipmap.point_off);
        }

        if(isCheck2){
            mPoint2.setBackgroundResource(R.mipmap.point_on);
            mBack.setVisibility(View.VISIBLE);
        }else {
            mPoint2.setBackgroundResource(R.mipmap.point_off);
        }

        if(isCheck3){
            mPoint3.setBackgroundResource(R.mipmap.point_on);
            mBack.setVisibility(View.GONE);
        }else {
            mPoint3.setBackgroundResource(R.mipmap.point_off);
        }
    }
}
