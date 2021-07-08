package com.example.smartwateringpark.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.smartwateringpark.R;
import com.example.smartwateringpark.database.Report;
import com.example.smartwateringpark.model.ReportViewModel;
import com.example.smartwateringpark.model.SettingViewModel;

import java.util.ArrayList;

public class SoilMonitoringFragment extends Fragment {
    private TextView soilStatus;
    private ReportViewModel reportViewModel;
//    private ArrayList<Reportt> listReport = new ArrayList<>();
    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        reportViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())).get(ReportViewModel.class);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_soil_monitoring, container, false);
        soilStatus = view.findViewById(R.id.soil_status);

        Log.d("TESTTT",String.valueOf("soil mosture"));
        reportViewModel.getAllReports().observe(getViewLifecycleOwner(),reports -> {
//            setting = settings;
            try {
                Report report = reports.get(reports.size() - 1);
                Log.d("TESTTT",String.valueOf(reports.size()));
                if(report.getNilaiKelembabanTanah() < 300){
                    soilStatus.setText("Basah");
                }else if(report.getNilaiKelembabanTanah() > 900){
                    soilStatus.setText("Kering");
                }else{
                    soilStatus.setText("Lembab");
                }
            }catch (ArrayIndexOutOfBoundsException e){
                e.printStackTrace();
            }

        });
        return view;
    }


}
