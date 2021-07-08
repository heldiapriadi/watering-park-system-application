package com.example.smartwateringpark.model;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.smartwateringpark.database.Report;
import com.example.smartwateringpark.database.ReportRepository;

import java.util.List;

public class ReportViewModel extends AndroidViewModel {

    private ReportRepository reportRepository;
    private final LiveData<List<Report>> AllReports;

    public ReportViewModel(Application application) {
        super(application);
        reportRepository = new ReportRepository(application);
        AllReports =reportRepository.getAllReports();
    }

    public LiveData<List<Report>> getAllReports(){
        return AllReports;
    }

    void insert(Report report){
        reportRepository.insert(report);
    }
}
