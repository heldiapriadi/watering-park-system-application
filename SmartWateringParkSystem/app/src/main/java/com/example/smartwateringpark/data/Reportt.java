package com.example.smartwateringpark.data;

import java.util.Date;

public class Reportt {
    private Date timeStamp;
    private int nilaiKelembabanTanah;
    private float volumeAirTerpakai;

    public Reportt(){

    }

    public Reportt(Date timeStamp, int nilaiKelembabanTanah, float volumeAirTerpakai, float totalAirTandon){
        this.setTimeStamp(timeStamp);
        this.setNilaiKelembabanTanah(nilaiKelembabanTanah);
        this.setVolumeAirTerpakai(volumeAirTerpakai);
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
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
