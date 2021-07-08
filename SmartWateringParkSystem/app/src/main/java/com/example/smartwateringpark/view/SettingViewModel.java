package com.example.smartwateringpark.view;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.smartwateringpark.database.Setting;
import com.example.smartwateringpark.database.SettingRepository;

import java.util.List;
import java.util.Set;

public class SettingViewModel extends AndroidViewModel {

    private SettingRepository settingRepository;
    // Using LiveData and caching what getAlphabetizedWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    private final LiveData<List<Setting>> AllSettings;

    public SettingViewModel(Application application) {
        super(application);
        settingRepository= new SettingRepository(application);
        AllSettings = settingRepository.getAllSettings();
    }

    LiveData<List<Setting>> getAllSettings() {
        return AllSettings;
    }

    void insert(Setting setting) {
        settingRepository.insert(setting);
    }
}