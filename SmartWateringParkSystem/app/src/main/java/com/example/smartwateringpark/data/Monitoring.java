package com.example.smartwateringpark.data;

public class Monitoring {
    private String tanggal;
    private String jam;
    private String soilStatus;
    private float waterUsage;

    public Monitoring(String tanggal, String jam, String soilStatus, float waterUsage) {
        this.setTanggal(tanggal);
        this.setJam(jam);
        this.setSoilStatus(soilStatus);
        this.setWaterUsage(waterUsage);
    }


    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getJam() {
        return jam;
    }

    public void setJam(String jam) {
        this.jam = jam;
    }

    public String getSoilStatus() {
        return soilStatus;
    }

    public void setSoilStatus(String soilStatus) {
        this.soilStatus = soilStatus;
    }

    public float getWaterUsage() {
        return waterUsage;
    }

    public void setWaterUsage(float waterUsage) {
        this.waterUsage = waterUsage;
    }
}
