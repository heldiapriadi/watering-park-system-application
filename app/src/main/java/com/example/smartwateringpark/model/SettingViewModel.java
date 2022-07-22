package com.example.smartwateringpark.model;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.smartwateringpark.database.Report;
import com.example.smartwateringpark.database.ReportRepository;
import com.example.smartwateringpark.database.Setting;
import com.example.smartwateringpark.database.SettingRepository;

import java.util.List;
import java.util.Set;

public class SettingViewModel extends AndroidViewModel {

    private SettingRepository settingRepository;
    private final LiveData<List<Setting>> AllSettings;

    public SettingViewModel(Application application) {
        super(application);
        settingRepository = new SettingRepository(application);
        AllSettings = settingRepository.getAllSettings();
    }

    public LiveData<List<Setting>> getAllSettings(){
        return AllSettings;
    }

    public void insert(Setting setting){
        settingRepository.insert(setting);
    }

    public void update(Setting setting){ settingRepository.update(setting);}
}
