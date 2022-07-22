package com.example.smartwateringpark.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "report")
public class Report {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "timestamp")
    private Date timeStamp;

    @ColumnInfo(name = "nilai_kelembaban_tanah")
    private int nilaiKelembabanTanah;

    @ColumnInfo(name = "volume_air_terpakai")
    private float volumeAirTerpakai;


    public Report(Date timeStamp, int nilaiKelembabanTanah, float volumeAirTerpakai){
        this.timeStamp = timeStamp;
        this.nilaiKelembabanTanah = nilaiKelembabanTanah;
        this.volumeAirTerpakai = volumeAirTerpakai;
    }

    public Report(){
    }

    @NonNull
    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(@NonNull Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public int getNilaiKelembabanTanah() {
        return nilaiKelembabanTanah;
    }

    public void setNilaiKelembabanTanah(int nilaiKelembabanTanah) {
        this.nilaiKelembabanTanah = nilaiKelembabanTanah;
    }

    public float getVolumeAirTerpakai() {
        return volumeAirTerpakai;
    }

    public void setVolumeAirTerpakai(float volumeAirTerpakai) {
        this.volumeAirTerpakai = volumeAirTerpakai;
    }
}
