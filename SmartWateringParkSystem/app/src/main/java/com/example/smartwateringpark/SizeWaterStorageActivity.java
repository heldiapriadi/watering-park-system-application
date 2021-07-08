package com.example.smartwateringpark;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.smartwateringpark.data.SettingsData;
import com.example.smartwateringpark.database.Setting;
import com.example.smartwateringpark.model.AntaresSetting;
import com.example.smartwateringpark.model.SettingViewModel;

import java.util.List;
import java.util.Set;

import id.co.telkom.iot.AntaresHTTPAPI;
import id.co.telkom.iot.AntaresResponse;

public class SizeWaterStorageActivity extends AppCompatActivity implements AntaresHTTPAPI.OnResponseListener {
    private Button buttonSetSetting;
//    private TextView volume;
    private EditText highWaterStorage;
    private EditText baseArea;
    private EditText distance;
    private List<Setting> setting;
    private SettingViewModel settingViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_size_water_storage);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setTitle("Size Water Storage");
        }

        settingViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(SettingViewModel.class);

        buttonSetSetting = findViewById(R.id.button_add_size_water_storage);
//        volume = findViewById(R.id.volume);
        distance = findViewById(R.id.distance_ultrasonic_to_base);
        baseArea = findViewById(R.id.base_area);
        highWaterStorage = findViewById(R.id.high_water_storage);

        settingViewModel.getAllSettings().observe(this,settings -> {
            distance.setText(String.valueOf(settings.get(3).getValue()));
            baseArea.setText(String.valueOf(settings.get(2).getValue()));
            highWaterStorage.setText(String.valueOf(settings.get(1).getValue()));
//            volume.setText(String.valueOf(Float.valueOf(baseArea.getText().toString())*Float.valueOf(highWaterStorage.getText().toString())/1000));
        });



        buttonSetSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                settingViewModel.update(new Setting("jarakSensor",Float.valueOf(distance.getText().toString())));
                settingViewModel.update(new Setting("luasPermukaan",Float.valueOf(baseArea.getText().toString())));
                settingViewModel.update(new Setting("tinggiTandon",Float.valueOf(highWaterStorage.getText().toString())));

//                        setting = settingViewModel.getAllSettings().getValue();
                sendDataSetting(settingViewModel.getAllSettings().getValue().get(0).getValue(),Float.valueOf(highWaterStorage.getText().toString()),Float.valueOf(baseArea.getText().toString()),Float.valueOf(distance.getText().toString()));
//                    SettingsData.getSetting().setPenyiramanOtomatis(1);

//                Intent intent = getIntent();
//                finish();
//                startActivity(intent);
            }
        });
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
