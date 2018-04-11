package com.apitechnosoft.mrhelper.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apitechnosoft.mrhelper.R;
import com.apitechnosoft.mrhelper.models.Locationreportdata;

import java.util.ArrayList;

public class SlaServiceAdapter extends RecyclerView.Adapter<SlaServiceAdapter.MyViewHolder> {

    private ArrayList<Locationreportdata> locationList;
    Context mContext;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView slatext;
        public ImageView photo;
        public CardView card_view;
        public MyViewHolder(View view) {
            super(view);
            slatext = (TextView) view.findViewById(R.id.slatext);
            photo = (ImageView) view.findViewById(R.id.photo);
            card_view = (CardView) view.findViewById(R.id.card_view);
        }
    }


    public SlaServiceAdapter(Context mContext, ArrayList<Locationreportdata> list) {
        this.locationList = list;
        this.mContext = mContext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sla_service_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.slatext.setText(locationList.get(position).getPinCode());
        holder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ((Activity)mContext).finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return locationList.size();
    }


}
