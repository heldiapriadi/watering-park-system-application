package com.example.smartwateringpark.database;

import android.app.Application;
import androidx.lifecycle.LiveData;
import java.util.List;

public class SettingRepository {
    private SettingDao settingDao;
    private LiveData<List<Setting>> AllSetting;

    // Note that in order to unit test the WordRepository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    public SettingRepository(Application application) {
        SwapasDatabase db = SwapasDatabase.getDatabase(application);
        settingDao = db.settingDao();
        AllSetting = settingDao.getSettings();
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    public LiveData<List<Setting>> getAllSettings() {
        return AllSetting;
    }

    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.
    public void insert(Setting setting) {
        SwapasDatabase.databaseWriteExecutor.execute(() -> {
            settingDao.insert(setting);
        });
    }

    public void update(Setting setting) {
        SwapasDatabase.databaseWriteExecutor.execute(() -> {
            settingDao.update(setting);
        });
    }
}