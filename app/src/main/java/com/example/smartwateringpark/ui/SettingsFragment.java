package com.example.smartwateringpark.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.smartwateringpark.MonitoringHistoryActivity;
import com.example.smartwateringpark.R;
import com.example.smartwateringpark.SizeWaterStorageActivity;
import com.example.smartwateringpark.database.Setting;
import com.example.smartwateringpark.model.AntaresSetting;
import com.example.smartwateringpark.model.ReportViewModel;
import com.example.smartwateringpark.model.SettingViewModel;

import java.util.ArrayList;
import java.util.List;

import id.co.telkom.iot.AntaresHTTPAPI;
import id.co.telkom.iot.AntaresResponse;

public class SettingsFragment extends Fragment implements AntaresHTTPAPI.OnResponseListener {
    private SettingViewModel settingViewModel;
    private Button sizeWaterStorageButton;
    private Button monitoringHistoryButton;
    private Switch switchWaterSpringkle;
    private List<Setting> setting;

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settingViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())).get(SettingViewModel.class);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        sizeWaterStorageButton = view.findViewById(R.id.size_water_storage);
        switchWaterSpringkle = view.findViewById(R.id.switch_automatically_sprinkle);

//        setting = settingViewModel.getAllSettings().getValue();
//


        settingViewModel.getAllSettings().observe(getViewLifecycleOwner(),settings -> {
//            setting = settings;
            if (settings.get(0).getValue() == 1){
                switchWaterSpringkle.setChecked(true);
            }else{
                switchWaterSpringkle.setChecked(false);
            }

            switchWaterSpringkle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        settingViewModel.update(new Setting("penyiramanOtomatis",(float)1));
//                        setting = settingViewModel.getAllSettings().getValue();
                        sendDataSetting(1,settings.get(1).getValue(),settings.get(2).getValue(),settings.get(3).getValue());
//                    SettingsData.getSetting().setPenyiramanOtomatis(1);
                    }else{
                        settingViewModel.update(new Setting("penyiramanOtomatis",(float)0));
//                        setting = settingViewModel.getAllSettings().getValue();
                        sendDataSetting(0,settings.get(1).getValue(),settings.get(2).getValue(),settings.get(3).getValue());
//                    SettingsData.getSetting().setPenyiramanOtomatis(1);
                    }
                }
            });
        });

        sizeWaterStorageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivitySizeWaterStorage();
            }
        });

        monitoringHistoryButton = view.findViewById(R.id.monitoring_history);
        monitoringHistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivityMonitoringHistory();
            }
        });




        return view;
    }

    public void openActivitySizeWaterStorage() {
        Intent intent = new Intent(getActivity(), SizeWaterStorageActivity.class);
        startActivity(intent);
    }

    public void openActivityMonitoringHistory() {
        Intent intent = new Intent(getActivity(), MonitoringHistoryActivity.class);
        startActivity(intent);
    }

    @Override
    public void onResponse(AntaresResponse antaresResponse) {

    }
    private void sendDataSetting(float penyiramanOtomatis, float tinggiTandon, float luasPermukaan, float jarakSensor){
        AntaresHTTPAPI antaresAPIHTTP = new AntaresHTTPAPI();
        antaresAPIHTTP.addListener(this);
        String textData = "{\\\"penyiramanOtomatis\\\":" +penyiramanOtomatis+",\\\"tinggiTandon\\\":"+tinggiTandon+",\\\"luasPermukaan\\\":"+luasPermukaan+",\\\"jarakSensor\\\":"+jarakSensor+"}";
        antaresAPIHTTP.storeDataofDevice(1, AntaresSetting.accessKey,AntaresSetting.porjectName,AntaresSetting.devineName[2], textData);
    }

}
