package com.police170m3.rpi.jjhmapproject.Util;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Jaehun on 2017-03-22.
 */

public class JSONParsor {

    //JSON 파싱은 이미지 URL에 저장된 비트맵을 불러오기 위해 사용
    //URL코드는 JSON파일 내부에 있으므로 URL코드는 비움

    public static JSONObject getJSONfromURL(String urlImage){
        String result = "";
        JSONObject jArray = null;

        try{
            URL url = new URL(urlImage);
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            Log.d("JSONParsor","urlImage: "+urlImage);
            if(httpURLConnection.getResponseCode() == 200)
            {
                BufferedReader r = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line = null;
                while((line = r.readLine())!=null)
                    sb.append(line + "\n");

                httpURLConnection.disconnect();
                result = sb.toString();
            }
        }catch (Exception e){
        }
        try {
            jArray = new JSONObject(result);
        } catch (JSONException e) {
        }

        return jArray;
    }
}
