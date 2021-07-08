package com.example.smartwateringpark.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface SettingDao {
    @Query("SELECT * FROM setting")
    LiveData<List<Setting>> getSettings();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Setting setting);

    @Update
    void update(Setting setting);
}