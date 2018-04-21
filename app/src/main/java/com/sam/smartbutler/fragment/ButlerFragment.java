package com.sam.smartbutler.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.client.HttpParams;
import com.sam.smartbutler.R;
import com.sam.smartbutler.adapter.ChatListAdapter;
import com.sam.smartbutler.entity.ChatListData;
import com.sam.smartbutler.utils.L;
import com.sam.smartbutler.utils.ShareUtils;
import com.sam.smartbutler.utils.StaticClass;

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
 * 描述：服务管家
 */

public class ButlerFragment extends Fragment implements View.OnClickListener {

    private List<ChatListData> mList = new ArrayList<>();
    private ListView lv_content;
    private ChatListAdapter adapter;
    private EditText et_text;
    private Button btn_send;

    //TTS
    private SpeechSynthesizer mTts;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_butler, null);
        findView(view);
        return view;
    }

    private void findView(View view) {

        //1.创建SpeechSynthesizer对象，第二个参数：本地合成时传InitListener
        mTts = SpeechSynthesizer.createSynthesizer(getActivity(),null);
        //2.合成参数设置
        mTts.setParameter(SpeechConstant.VOICE_NAME,"xiaoyan");//设置发音人
        mTts.setParameter(SpeechConstant.SPEED,"50");//设置语速
        mTts.setParameter(SpeechConstant.VOLUME,"80");//设置音量
        mTts.setParameter(SpeechConstant.ENGINE_TYPE,SpeechConstant.TYPE_CLOUD);//设置云端

        et_text = (EditText) view.findViewById(R.id.et_text);
        btn_send = (Button) view.findViewById(R.id.btn_send);
        lv_content = (ListView) view.findViewById(R.id.lv_content);
        adapter = new ChatListAdapter(getActivity(), mList);
        lv_content.setAdapter(adapter);
        setLeftItemData("你好");

        btn_send.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            /**
             * 逻辑
             * 1.获取输入框内容
             * 2.判断输入框是否为空
             * 3.输入字符不能大于30
             * 4.清空输入框
             * 5.添加输入的内容到right_item
             * 6.发送给机器人请求返回内容
             * 7.处理机器人的返回值后添加到left_item
             */
            case R.id.btn_send:
                String text = et_text.getText().toString();
                if (!TextUtils.isEmpty(text)) {
                    if (text.length() > 30) {
                        Toast.makeText(getActivity(), "输入框内容不能大于30", Toast.LENGTH_SHORT).show();
                    } else {
                        et_text.setText("");
                        setRightItemData(text);
                        String url = "http://www.tuling123.com/openapi/api";
                        HttpParams params = new HttpParams();
                        params.put("key",StaticClass.CHAT_LIST_KEY);
                        params.put("info",text);
                        params.put("userid",123456);
                        L.i("text:"+text);
//                        RxVolley.get(url, new HttpCallback() {
//                            @Override
//                            public void onSuccess(String t) {
////                                Toast.makeText(getActivity(), t, Toast.LENGTH_SHORT).show();
//                                L.i(t);
//                                parsingJson(t);
//
//                            }
//                        });
                        RxVolley.post(url, params, new HttpCallback() {
                            @Override
                            public void onSuccess(String t) {
                                L.i("t="+t);
                                parsingJson(t);
                            }
                        });
                    }
                } else {
                    Toast.makeText(getActivity(), "输入框不能为空", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void parsingJson(String t) {
        try {
            JSONObject jsonObject = new JSONObject(t);
//            JSONObject jsonResult = jsonObject.getJSONObject("result");
//            String result = jsonResult.getString("text");
            String result = jsonObject.getString("text");
            setLeftItemData(result);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void setRightItemData(String text) {
        ChatListData data = new ChatListData();
        data.setText(text);
        data.setType(ChatListAdapter.VALUE_RIGHT_TEXT);
        mList.add(data);
        //通知adapter更新
        adapter.notifyDataSetChanged();
        //滚动到底部
        lv_content.setSelection(lv_content.getBottom());
    }

    private void setLeftItemData(String text) {
        boolean isSpeak = ShareUtils.getBoolean(getActivity(),"isSpeak",false);
        if (isSpeak){
            startSpeaking(text);
        }
        ChatListData data = new ChatListData();
        data.setText(text);
        data.setType(ChatListAdapter.VALUE_LEFT_TEXT);
        mList.add(data);
        //通知adapter更新
        adapter.notifyDataSetChanged();
        //滚动到底部
        lv_content.setSelection(lv_content.getBottom());
    }

    //开始说话
    private void startSpeaking(String text){
        mTts.startSpeaking(text, new SynthesizerListener() {

            //开始播放
            @Override
            public void onSpeakBegin() {

            }

            //缓冲进度回调
            //percent为缓冲进度0~100，beginPos为缓冲音频在文本中开始位置，endPos表示缓冲音频在文本中结束位置，info为附加信息
            @Override
            public void onBufferProgress(int i, int i1, int i2, String s) {

            }

            //暂停播放
            @Override
            public void onSpeakPaused() {

            }

            //恢复播放回调接口
            @Override
            public void onSpeakResumed() {

            }


            //播放进度回调
            //percent为播放进度0~100,beginPos为播放音频在文本中开始位置，endPos表示播放音频在文本中结束位置.
            @Override
            public void onSpeakProgress(int i, int i1, int i2) {

            }

            //会话结束回调接口，没有错误时，error为null
            @Override
            public void onCompleted(SpeechError speechError) {

            }

            //会话事件回调接口
            @Override
            public void onEvent(int i, int i1, int i2, Bundle bundle) {

            }
        });
    }
}
