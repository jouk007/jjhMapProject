package com.police170m3.rpi.jjhmapproject;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Jaehun on 2017-03-24.
 */

public class MapRecyclerAdapter extends RecyclerView.Adapter<MapRecyclerAdapter.ViewHolder>{

    Context context;
    LayoutInflater inflater;
    ArrayList<HashMap<String, String>> data;
    ImageLoader imageLoader;
    HashMap<String, String> resultp = new HashMap<String, String>();

    public MapRecyclerAdapter(Context context, ArrayList<HashMap<String, String>> arraylist){
        this.context = context;
        data = arraylist;
        imageLoader = new ImageLoader(context);
    }

    @Override
    public long getItemId(int position) {
        return data.size();
    }

    @Override
    public void onBindViewHolder(MapRecyclerAdapter.ViewHolder viewHolder, int position) {
        resultp = data.get(position);

        viewHolder.textView_Name.setText(resultp.get(BaseActivity.Name));
        viewHolder.textView_Sido.setText(resultp.get(BaseActivity.Sido));
        viewHolder.textView_Gugun.setText(resultp.get(BaseActivity.Gugun));
        viewHolder.textView_Dong.setText(resultp.get(BaseActivity.Dong));
        viewHolder.textView_Bunji.setText(resultp.get(BaseActivity.Bunji));
        viewHolder.textView_Price.setText(resultp.get(BaseActivity.Price));
        imageLoader.DisplayImage(resultp.get(BaseActivity.FLAG), viewHolder.flag);
    }

    @Override
    public MapRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View itemView = inflater.inflate(R.layout.card_view, parent, false);
        final ViewHolder viewHolder = new ViewHolder(itemView);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int position = viewHolder.getAdapterPosition();
                resultp = data.get(position);

                Log.d("onCreateViewHolder","id: "+position);

                Intent intent = new Intent(context, InfoActivity.class);
                intent.putExtra("name",resultp.get(MainActivity.Name));
                intent.putExtra("gugun",resultp.get(MainActivity.Gugun));
                intent.putExtra("sido",resultp.get(MainActivity.Sido));
                intent.putExtra("dong",resultp.get(MainActivity.Dong));
                intent.putExtra("bunji",resultp.get(MainActivity.Bunji));
                intent.putExtra("flag",resultp.get(MainActivity.FLAG));
                intent.putExtra("id",resultp.get(MainActivity.Id));
                intent.putExtra("households",resultp.get(MainActivity.Households));
                intent.putExtra("buildDate",resultp.get(MainActivity.BuildDate));
                intent.putExtra("score",resultp.get(MainActivity.Score));
                intent.putExtra("price",resultp.get(MainActivity.Price));
                intent.putExtra("floorArea",resultp.get(MainActivity.FloorArea));

                context.startActivity(intent);

            }
        });
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView flag;
        public TextView textView_Name;
        TextView textView_Sido;
        TextView textView_Gugun;
        TextView textView_Dong;
        TextView textView_Bunji;
        TextView textView_Price;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);

            flag =(ImageView)itemView.findViewById(R.id.mapImage);
            textView_Name =(TextView)itemView.findViewById(R.id.textView_Name);
            textView_Sido =(TextView)itemView.findViewById(R.id.textView_Sido);
            textView_Gugun =(TextView)itemView.findViewById(R.id.textView_Gugun);
            textView_Dong =(TextView)itemView.findViewById(R.id.textView_Dong);
            textView_Bunji =(TextView)itemView.findViewById(R.id.textView_Bungi);
            textView_Price =(TextView)itemView.findViewById(R.id.textView_Price);
        }

    }
}
