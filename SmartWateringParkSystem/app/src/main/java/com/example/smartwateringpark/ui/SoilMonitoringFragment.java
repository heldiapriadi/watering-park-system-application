package com.example.smartwateringpark.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.smartwateringpark.R;
import com.example.smartwateringpark.data.Reportt;
import com.example.smartwateringpark.data.ReportsData;

import java.util.ArrayList;

public class SoilMonitoringFragment extends Fragment {
    private TextView soilStatus;
//    private ArrayList<Reportt> listReport = new ArrayList<>();
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_soil_monitoring, container, false);
        soilStatus = view.findViewById(R.id.soil_status);

//        if(listReport.get(listReport.size() - 1).getNilaiKelembabanTanah() < 300){
//            soilStatus.setText("Basah");
//        }else if(listReport.get(listReport.size() - 1).getNilaiKelembabanTanah() > 900){
//            soilStatus.setText("Kering");
//        }else{
//            soilStatus.setText("Lembab");
//        }

        return view;
    }

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        listReport = ReportsData.getListReports();
    }
}
