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
import com.example.smartwateringpark.model.SettingViewModel;

import java.util.ArrayList;

public class WaterStorageFragment extends Fragment {

    private TextView waterStatus;
    private SettingViewModel settingViewModel;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_water_storage, container, false);
        waterStatus = view.findViewById(R.id.water_status);

        settingViewModel.getAllSettings().observe(getViewLifecycleOwner(),settings ->{
            float sisaTotalAirTandon = settings.get(4).getValue();
            float volumeAir = (sisaTotalAirTandon / ((settings.get(1).getValue() * settings.get(2).getValue()) / 1000)) * 100;

            if (volumeAir < 0){
                waterStatus.setText("0%");
            }else if(volumeAir > 100){
                waterStatus.setText("100%");
            }
            else{
                waterStatus.setText(String.valueOf(volumeAir).substring(0,4)+"%");
            }


        });

//        float volumeTandon = setting.getTinggiTandon() * setting.getLuasPermukaan();
//        waterStatus.setText(String.valueOf((listReport.get(listReport.size() - 1).getTotalAirTandon()/volumeTandon) * 100).substring(0,4) + "%");
        return view;
    }

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settingViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())).get(SettingViewModel.class);
//        listReport.addAll(ReportsData.getListReports());
//        setting = SettingsData.getSetting();

    }
}
