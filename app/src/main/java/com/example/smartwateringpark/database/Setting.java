package com.example.smartwateringpark.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "setting")
public class Setting {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "key")
    private String key;

    @ColumnInfo(name = "value")
    private float value;

    public Setting(String key, float value) {
        this.key = key;
        this.value = value;
    }

    @NonNull
    public String getKey() {
        return key;
    }

    public void setKey(@NonNull String key) {
        this.key = key;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }
}
