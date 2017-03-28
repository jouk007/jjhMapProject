package com.police170m3.rpi.jjhmapproject;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.police170m3.rpi.jjhmapproject.Util.JSONParsor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

public class BaseActivity extends AppCompatActivity {

    //카드뷰를 표시하여 메인 액티비티 하단에 볼수 있게 만든다

    JSONObject jsonobject;

    ArrayList<HashMap<String,String>> arrayList;
    ArrayList<Double> arrLat;
    ArrayList<Double> arrLng;
    RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    static String Id = "id";
    static String Name = "name";
    static String Gugun = "gugun";
    static String Sido = "sido";
    static String Dong = "dong";
    static String Bunji = "bunji";
    static String FLAG = "flag";
    static String Price = "price";
    static String Households="households";
    static String BuildDate="buildDate";
    static String Score="score";
    static String FloorArea="floorArea";
    static String Lat="lat";
    static String Lng="lng";

    private RecyclerView.LayoutManager mLayoutManager;
    private RelativeLayout contentView = null;
    private static Context mCtx = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.setContentView(R.layout.activity_base);
        mCtx= this;
        recyclerView = (RecyclerView) findViewById(R.id.map_list);
        contentView = (RelativeLayout)findViewById(R.id.contentView);

        showImageTask taskImage = new showImageTask();
        taskImage.execute();
        super.onCreate(savedInstanceState);
    }

    class showImageTask extends AsyncTask<Void,Void,Void> {

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Void doInBackground(Void... unused) {
            //create an array
            arrayList = new ArrayList<HashMap<String,String>>();
            arrLng = new ArrayList<Double>();
            arrLat = new ArrayList<Double>();

            //json 파일에 담겨져 있는 데이터를 받아 해쉬맵에 저장한다
            AssetManager assetManager = getResources().getAssets();

            try{
                AssetManager.AssetInputStream assetInputStream = (AssetManager.AssetInputStream)assetManager.open("mobiledangi.json");

                BufferedReader br = new BufferedReader(new InputStreamReader(assetInputStream));

                StringBuilder sb = new StringBuilder();

                int bufferSize = 1024 * 1024;

                char readBuf [] = new char[bufferSize];
                int resultSize = 0;

                //파일 전체내용 읽어오기
                while ((resultSize = br.read(readBuf)) != -1){
                    if(resultSize == bufferSize){
                        sb.append(readBuf);
                    }else{
                        for (int i= 0; i < resultSize; i++){
                            //StringBuilder에 append
                            sb.append(readBuf[i]);
                        }
                    }
                }

                String jString = sb.toString();

                JSONObject jsonObject = new JSONObject(jString);

                JSONArray jArr = new JSONArray(jsonObject.getString("filtered"));

                jsonobject = JSONParsor.getJSONfromURL("");

                int arrLen =jArr.length();

                String id[] = new String[arrLen];
                String name[] = new String[arrLen];
                String gugun[] = new String[arrLen];
                String sido[] = new String[arrLen];
                String dong[] = new String[arrLen];
                String bunji[] = new String[arrLen];
                String photoURL_thumbnail[] = new String[arrLen];
                String households[] = new String[arrLen];
                String builddate[] = new String[arrLen];
                String score[] = new String[arrLen];
                String price[] = new String[arrLen];
                String floorArea[] = new String[arrLen];

                String lat[] = new String[arrLen];
                String lng[] = new String[arrLen];

                for(int i = 0; i < jArr.length(); i++){
                    name[i] = jArr.getJSONObject(i).getString("name").toString();
                    id[i] = jArr.getJSONObject(i).getString("id").toString();
                    gugun[i] = jArr.getJSONObject(i).getString("gugun").toString();
                    sido[i] = jArr.getJSONObject(i).getString("sido").toString();
                    dong[i] = jArr.getJSONObject(i).getString("dong").toString();
                    bunji[i] = jArr.getJSONObject(i).getString("bunji").toString();
                    price[i] = jArr.getJSONObject(i).getString("price").toString();
                    floorArea[i] = jArr.getJSONObject(i).getString("floorArea").toString();
                    households[i] = jArr.getJSONObject(i).getString("households").toString();
                    builddate[i] = jArr.getJSONObject(i).getString("buildDate").toString();
                    score[i] = jArr.getJSONObject(i).getString("score").toString();

                    lat[i] = jArr.getJSONObject(i).getString("lat").toString();
                    lng[i] = jArr.getJSONObject(i).getString("lng").toString();

                    photoURL_thumbnail[i] = jArr.getJSONObject(i).getString("image").toString();

                    HashMap<String, String> mapHash = new HashMap<String, String>();
                    jsonobject = jArr.getJSONObject(i);
                    mapHash.put("id",id[i]);
                    mapHash.put("name",name[i]);
                    mapHash.put("gugun",gugun[i]);
                    mapHash.put("sido",sido[i]);
                    mapHash.put("dong",dong[i]);
                    mapHash.put("bunji",bunji[i]);
                    mapHash.put("flag",jsonobject.getString("image"));
                    mapHash.put("floorArea",floorArea[i]);
                    mapHash.put("households",households[i]);
                    mapHash.put("buildDate",builddate[i]);
                    mapHash.put("score",score[i]);
                    mapHash.put("price",price[i]);
                    mapHash.put("lat",lat[i]);
                    mapHash.put("lng",lng[i]);

                    arrayList.add(mapHash);
                }
            }catch(IOException e){
                Log.e("execption", "파일이 없습니다", e);
            }catch (JSONException je){
                Log.e("jsonErr", "JSON 오류가 발생했습니다", je);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            // 리스트뷰 참조 및 Adapter달기

            recyclerView.setHasFixedSize(true);

            // StaggeredGrid 레이아웃 사용
            mLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL);

            adapter = new MapRecyclerAdapter(BaseActivity.this, arrayList);
            recyclerView.setAdapter(adapter);

            // ListView
            recyclerView.setLayoutManager(mLayoutManager);

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void setContentView(int res)  {

        contentView.removeAllViews();

        LayoutInflater inflater;
        inflater = LayoutInflater.from(this);

        View item = inflater.inflate(res, null);
        contentView.addView(item, new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));

    }

    @Override
    public void setContentView(View view) {

        contentView.removeAllViews();

        contentView.addView(view, new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));

    }

    public void addView(View v)
    {
        contentView.removeAllViews();
        contentView.addView(v, new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
    }

}
