package com.example.smartwateringpark.model;

import android.app.Application;
import android.util.Log;

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

    public LiveData<List<Report>> getAllFilterReports(long fromDate, long toDate) {
        Log.d("TESTTT","BEBAS APA AJA");
        Log.d("TESTTT","fromDate: "+String.valueOf(fromDate)+" ToDate: "+String.valueOf(toDate));
        return reportRepository.getAllFilterReports(fromDate,toDate);
    }

    public void insert(Report report){
        reportRepository.insert(report);
    }
}
