package com.example.smartwateringpark.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ReportDao {
    @Query("SELECT * FROM report ORDER BY timestamp ASC")
    LiveData<List<Report>> getReports();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Report report);

    @Query("SELECT * FROM report WHERE timestamp > :fromDate and timestamp < :toDate ORDER BY timestamp ASC")
    LiveData<List<Report>> getFilterReports(long fromDate, long toDate);
}