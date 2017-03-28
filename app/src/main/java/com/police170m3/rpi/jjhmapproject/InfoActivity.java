package com.police170m3.rpi.jjhmapproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Jaehun on 2017-03-25.
 */

public class InfoActivity extends AppCompatActivity {

    private TextView txtTitle;
    public ImageView flag;
    public TextView textView_Name;
    private TextView textView_Id;
    private TextView textView_Sido;
    private TextView textView_Gugun;
    private TextView textView_Dong;
    private TextView textView_Bunji;
    private TextView textView_Households;
    private TextView textView_BuildDate;
    private TextView textView_Score;
    private TextView textView_Price;
    private TextView textView_FloorArea;
    String Name;
    String Gugun;
    String Sido;
    String Dong;
    String Bunji;
    String Households;
    String BuildDate;
    String Score;
    String Price;
    String FloorArea;
    String FLAG;
    String Id;
    Context context;
    LayoutInflater inflater;
    ArrayList<HashMap<String, String>> data;
    InfoImageLoader infoImageLoader = new InfoImageLoader(this);
    HashMap<String, String> resultp = new HashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_info);

        flag =(ImageView)findViewById(R.id.mapImage);
        textView_Name =(TextView)findViewById(R.id.textView_Name);
        textView_Sido =(TextView)findViewById(R.id.textView_Sido);
        textView_Gugun =(TextView)findViewById(R.id.textView_Gugun);
        textView_Dong =(TextView)findViewById(R.id.textView_Dong);
        textView_Bunji =(TextView)findViewById(R.id.textView_Bungi);
        textView_Id = (TextView)findViewById(R.id.textView_id);
        textView_Households =(TextView)findViewById(R.id.textView_Households);
        textView_BuildDate =(TextView)findViewById(R.id.textView_BuildDate);
        textView_Score =(TextView)findViewById(R.id.textView_Score);
        textView_Price =(TextView)findViewById(R.id.textView_Price);
        textView_FloorArea =(TextView)findViewById(R.id.textView_FloorArea);

        Intent i = getIntent();
        Name = i.getStringExtra("name");
        Gugun = i.getStringExtra("gugun");
        Sido = i.getStringExtra("sido");
        Dong = i.getStringExtra("dong");
        Bunji = i.getStringExtra("bunji");
        FLAG = i.getStringExtra("flag");
        Households = i.getStringExtra("households");
        Id = i.getStringExtra("id");
        BuildDate = i.getStringExtra("buildDate");
        Score = i.getStringExtra("score");
        Price = i.getStringExtra("price");
        FloorArea = i.getStringExtra("floorArea");

        setLoader();
    }

    public void setLoader(){
        textView_Name.setText(Name);
        textView_Sido.setText(Sido);
        textView_Gugun.setText(Gugun);
        textView_Dong.setText(Dong);
        textView_Bunji.setText(Bunji);
        infoImageLoader.DisplayImage(FLAG, flag);
        textView_Id.setText(Id);
        textView_Households.setText(Households);
        textView_BuildDate.setText(BuildDate);
        textView_Score.setText(Score);
        textView_Price.setText(Price);
        textView_FloorArea.setText(FloorArea);
    }
}
