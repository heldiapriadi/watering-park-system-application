package com.example.smartwateringpark.view;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.smartwateringpark.database.Report;
import com.example.smartwateringpark.database.ReportRepository;

import java.util.List;

public class ReportViewModel extends AndroidViewModel {

    private ReportRepository reportRepository;
    // Using LiveData and caching what getAlphabetizedWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    private final LiveData<List<Report>> AllReports;

    public ReportViewModel(Application application) {
        super(application);
        reportRepository= new ReportRepository(application);
        AllReports = reportRepository.getAllReports();
    }

    LiveData<List<Report>> getAllReports() {
        return AllReports;
    }

    void insert(Report report) {
        reportRepository.insert(report);
    }
}