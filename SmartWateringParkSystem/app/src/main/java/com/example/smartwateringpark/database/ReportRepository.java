package com.example.smartwateringpark.database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.Set;

public class ReportRepository {
    private ReportDao reportDao;
    private LiveData<List<Report>> AllReports;

    // Note that in order to unit test the WordRepository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    public ReportRepository(Application application) {
        SwapasDatabase db = SwapasDatabase.getDatabase(application);
        reportDao = db.reportDao();
        AllReports = reportDao.getReports();
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    public LiveData<List<Report>> getAllReports() {
        return AllReports;
    }

    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.
    public void insert(Report report) {
        SwapasDatabase.databaseWriteExecutor.execute(() -> {
            reportDao.insert(report);
        });
    }
}