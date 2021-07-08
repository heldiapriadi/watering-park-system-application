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
import com.example.smartwateringpark.data.Setting;
import com.example.smartwateringpark.data.SettingsData;

import java.util.ArrayList;

public class WaterStorageFragment extends Fragment {

    private ArrayList<Reportt> listReport = new ArrayList<>();
    private Setting setting;
    private TextView waterStatus;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_water_storage, container, false);
        waterStatus = view.findViewById(R.id.water_status);

//        float volumeTandon = setting.getTinggiTandon() * setting.getLuasPermukaan();
//        waterStatus.setText(String.valueOf((listReport.get(listReport.size() - 1).getTotalAirTandon()/volumeTandon) * 100).substring(0,4) + "%");
        return view;
    }

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//
//        listReport.addAll(ReportsData.getListReports());
//        setting = SettingsData.getSetting();

    }
}
