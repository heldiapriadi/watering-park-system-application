package com.example.smartwateringpark.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartwateringpark.data.Monitoring;
import com.example.smartwateringpark.R;
import com.example.smartwateringpark.database.Report;

import java.text.SimpleDateFormat;
import java.util.List;

public class RecycleviewAdapter extends RecyclerView.Adapter<RecycleviewAdapter.MyViewHolder>  {
    Context mContext;
    List<Report> mData;

    public RecycleviewAdapter(Context mContext, List<Report> mData) {
        this.mContext   = mContext;
        this.mData      = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.list_monitoring,parent,false);
        MyViewHolder vHolder = new MyViewHolder(v);

        return vHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        String strDate = formatter.format(mData.get(position).getTimeStamp());
        holder.tvJam.setText(strDate);
        holder.tvSoilStatus.setText(nilaiKelembabanToString(mData.get(position).getNilaiKelembabanTanah()));
        holder.tvWaterUsage.setText(String.valueOf(mData.get(position).getVolumeAirTerpakai()));
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }

    public  static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvJam;
        private TextView tvSoilStatus;
        private TextView tvWaterUsage;

        public MyViewHolder(View itemView) {
            super(itemView);

            tvJam = (TextView) itemView.findViewById(R.id.tvJam);
            tvSoilStatus = (TextView) itemView.findViewById(R.id.tvSoilStatus);
            tvWaterUsage = (TextView)  itemView.findViewById(R.id.tvWaterUsage);
        }
    }

    private String nilaiKelembabanToString(int nilaiKelembaban){
        if (nilaiKelembaban < 300){
            return "Basah";
        }else if(nilaiKelembaban > 600){
            return "Kering";
        }else{
            return "Lembab";
        }
    }
}
