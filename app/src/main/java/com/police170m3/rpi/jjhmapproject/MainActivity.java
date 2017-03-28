package com.police170m3.rpi.jjhmapproject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.police170m3.rpi.jjhmapproject.Util.JSONParsor;
import com.police170m3.rpi.jjhmapproject.Util.LogManager;
import com.skp.Tmap.TMapMarkerItem;
import com.skp.Tmap.TMapPOIItem;
import com.skp.Tmap.TMapPoint;
import com.skp.Tmap.TMapView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends BaseActivity {

    private TMapView tMapView;

    private Context mContext;

    ArrayList<String> mArrayMarkerID;
    private static int mMarkerID;
    ArrayList<HashMap<String, String>> dataInfo;
    HashMap<String, String> resultInfo = new HashMap<String, String>();

    ArrayList<String> getfloorArea;
    ArrayList<String> getName;
    ArrayList<String> getId;
    public static String Id = "id";
    public static String Name = "name";
    public static String Gugun = "gugun";
    public static String Sido = "sido";
    public static String Dong = "dong";
    public static String Bunji = "bunji";
    public static String Households="households";
    public static String BuildDate="buildDate";
    public static String Score="score";
    public static String Price="price";
    public static String FloorArea="floorArea";
    public static String FLAG = "flag";

    private ProgressDialog progressDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;


        mArrayMarkerID = new ArrayList<String>();
        mMarkerID = 0;

        RelativeLayout relativeLayout = new RelativeLayout(this);
        tMapView = new TMapView(this);

        tMapView.setSKPMapApiKey("a5c9afa4-c69a-37ab-8da5-d20a4a3176b5");

        tMapView.setLanguage(TMapView.LANGUAGE_KOREAN);
        tMapView.setCompassMode(true);
        tMapView.setIconVisibility(true);
        tMapView.setMapType(TMapView.MAPTYPE_STANDARD);
        tMapView.setCompassMode(true);
        tMapView.setTrackingMode(true);
        tMapView.setSightVisible(true);
        tMapView.setCenterPoint(128.5521682, 35.8570447, true);

        addView(tMapView);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        showDataTask taskData = new showDataTask();
        taskData.execute();

    }

    //json 파일에 담겨져 있는 데이터를 받아 해쉬맵에 저장한다
    //InfoActivity와 MainActivity에 사용될 데이터 저장
    class showDataTask extends AsyncTask<Void,Void,Void> {
        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("이미지를 불러오고 있습니다");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            //create an array
            dataInfo = new ArrayList<HashMap<String,String>>();
            arrLng = new ArrayList<Double>();
            arrLat = new ArrayList<Double>();
            getName = new ArrayList<String>();
            getfloorArea = new ArrayList<String>();
            getId = new ArrayList<String>();

            AssetManager assetManager = getResources().getAssets();

            try{
                AssetManager.AssetInputStream assetInputStream = (AssetManager.AssetInputStream)assetManager.open("mobiledangi.json");

                BufferedReader br = new BufferedReader(new InputStreamReader(assetInputStream));

                StringBuilder sb = new StringBuilder();

                int bufferSize = 1024 * 1024;

                char readBuf [] = new char[bufferSize];
                int resultSize = 0;

                while ((resultSize = br.read(readBuf)) != -1){
                    if(resultSize == bufferSize){
                        sb.append(readBuf);
                    }else{
                        for (int i= 0; i < resultSize; i++){
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
                String households[] = new String[arrLen];
                String builddate[] = new String[arrLen];
                String score[] = new String[arrLen];
                String price[] = new String[arrLen];
                Double lat[] = new Double[arrLen];
                Double lng[] = new Double[arrLen];
                String floorArea[] = new String[arrLen];

                for(int i = 0; i < jArr.length(); i++){
                    name[i] = jArr.getJSONObject(i).getString("name").toString();
                    id[i] = jArr.getJSONObject(i).getString("id").toString();
                    gugun[i] = jArr.getJSONObject(i).getString("gugun").toString();
                    sido[i] = jArr.getJSONObject(i).getString("sido").toString();
                    dong[i] = jArr.getJSONObject(i).getString("dong").toString();
                    bunji[i] = jArr.getJSONObject(i).getString("bunji").toString();
                    lat[i] = jArr.getJSONObject(i).getDouble("lat");
                    lng[i] = jArr.getJSONObject(i).getDouble("lng");
                    floorArea[i] = jArr.getJSONObject(i).getString("floorArea").toString();
                    households[i] = jArr.getJSONObject(i).getString("households").toString();
                    builddate[i] = jArr.getJSONObject(i).getString("buildDate").toString();
                    score[i] = jArr.getJSONObject(i).getString("score").toString();
                    price[i] = jArr.getJSONObject(i).getString("price").toString();

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

                    arrLat.add(lat[i]);
                    arrLng.add(lng[i]);
                    dataInfo.add(mapHash);
                    getfloorArea.add(floorArea[i]);
                    getName.add(name[i]);
                    getId.add(id[i]);
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
            showMarkerPoint();
            progressDialog.dismiss();
        }
    }

    //최초로 생성될 마커를 설정한다
    public void showMarkerPoint() {
        Bitmap bitmap_i = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.markersearch);
        ArrayList<String> IdList = new ArrayList<String>();

        for(int i = 0; i < arrLat.size(); i++) {
            Bitmap markerBitmap = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.end);
            Log.d("showMarkerPoint","markerBitmap: "+markerBitmap);
            double latitude = arrLat.get(i);
            double longitude = arrLng.get(i);
            String floorarea = getfloorArea.get(i);
            String getname = getName.get(i);
            TMapMarkerItem item = new TMapMarkerItem();

            TMapPoint point = new TMapPoint(latitude, longitude);
            String strID = String.format("%02d", i);

            item.setTMapPoint(point);
            item.setName(getname);
            item.setVisible(item.VISIBLE);

            item.setIcon(markerBitmap);

            item.setCalloutTitle(getname);
            item.setCalloutSubTitle(floorarea+" 평");
            item.setCalloutRightButtonImage(bitmap_i);
            item.setCanShowCallout(true);

            LogManager.printLog("MainActivity onPressUpEvent " + strID);
            IdList.add(strID);
            tMapView.addMarkerItem(strID, item);
        }
        initView(IdList);
    }

    private void initView(final ArrayList<String> getMarkerId) {
        final ArrayList<String> setmyMarkerId = getMarkerId;

        tMapView.setOnClickListenerCallBack(new TMapView.OnClickListenerCallback() {
            @Override
            public boolean onPressUpEvent(ArrayList<TMapMarkerItem> markerlist, ArrayList<TMapPOIItem> poilist, TMapPoint point, PointF pointf) {
                return false;
            }

            @Override
            public boolean onPressEvent(ArrayList<TMapMarkerItem> markerlist, ArrayList<TMapPOIItem> poilist, TMapPoint point, PointF pointf) {

                return false;
            }
        });

        tMapView.setOnDisableScrollWithZoomLevelListener(new TMapView.OnDisableScrollWithZoomLevelCallback() {
            @Override
            public void onDisableScrollWithZoomLevelEvent(float zoom, TMapPoint centerPoint) {
                LogManager.printLog("MainActivity zoom " + zoom);
            }
        });

        //마커의 오른족 이미지를 클릭하면 상세 정보(InfoActivity)로 넘어감
        tMapView.setOnCalloutRightButtonClickListener(new TMapView.OnCalloutRightButtonClickCallback() {
            @Override
            public void onCalloutRightButton(TMapMarkerItem markerItem) {

                String markerId = markerItem.getID();
                int getId = Integer.parseInt(markerId);
                resultInfo = dataInfo.get(getId);

                Intent intent = new Intent(MainActivity.this, InfoActivity.class);
                intent.putExtra("name",resultInfo.get(MainActivity.Name));
                intent.putExtra("gugun",resultInfo.get(MainActivity.Gugun));
                intent.putExtra("sido",resultInfo.get(MainActivity.Sido));
                intent.putExtra("dong",resultInfo.get(MainActivity.Dong));
                intent.putExtra("bunji",resultInfo.get(MainActivity.Bunji));
                intent.putExtra("flag",resultInfo.get(MainActivity.FLAG));
                intent.putExtra("id",resultInfo.get(MainActivity.Id));
                intent.putExtra("households",resultInfo.get(MainActivity.Households));
                intent.putExtra("buildDate",resultInfo.get(MainActivity.BuildDate));
                intent.putExtra("score",resultInfo.get(MainActivity.Score));
                intent.putExtra("price",resultInfo.get(MainActivity.Price));
                intent.putExtra("floorArea",resultInfo.get(MainActivity.FloorArea));

                startActivity(intent);

                Log.d("ClickEvent","count: "+markerId);
            }
        });
    }
}
