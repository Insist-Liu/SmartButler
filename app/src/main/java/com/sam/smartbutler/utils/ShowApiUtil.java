package com.sam.smartbutler.utils;

import android.net.Uri;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Administrator on 2018/4/13.
 */

public class ShowApiUtil {
    private static final String SHOW_API_APPID = "50770";
    private static final String SHOW_API_SECRT = "9c5b2cb755f34196a18847f46431705b";

    public static final String SAYING = "1211-1";

    //需要解析的URL网址
    public static String getApiRequest(String address){
        String url = Uri.parse("http://route.showapi.com/"+address)
                .buildUpon()
                .appendQueryParameter("showapi_appid",SHOW_API_APPID)
                .appendQueryParameter("showapi_sign",SHOW_API_SECRT)
                .build().toString();
        return url;
    }

    //获取数据，使用HttpURLConnection实现
    public static String getData(String httpUrl){
        String jsonResult;
        BufferedReader reader = null;
        StringBuffer sb = new StringBuffer();

        try {
            URL url = new URL(getApiRequest(httpUrl));
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            InputStream is = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is,"UTF-8"));
            String strRead = null;

            while ((strRead = reader.readLine()) != null){
                sb.append(strRead);
                sb.append("\r\n");
            }
            is.close();
            reader.close();
            connection.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        jsonResult = sb.toString();
        return jsonResult;
    }

    //从返回的json数据解析出结果
    public static String parseJsonFromSaying(String jsonResult){
        String english = null;
        String chinese = null;

        try {
            JSONObject jsonObject = new JSONObject(jsonResult);
            JSONObject resBody = jsonObject.getJSONObject("showapi_res_body");
            JSONArray resDataArray = resBody.getJSONArray("data");
            JSONObject result = resDataArray.getJSONObject(0);

            english = result.getString("english");
            chinese = result.getString("chinese");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return chinese;
    }
}
