package com.example.smartwateringpark.data;

public class Setting {
    private float penyiramanOtomatis;
    private float tinggiTandon;
    private float luasPermukaan;
    private float jarakSensor;

    public Setting(float penyiramanOtomatis, float tinggiTandon, float luasPermukaan, float jarakSensor) {
        this.penyiramanOtomatis = penyiramanOtomatis;
        this.tinggiTandon = tinggiTandon;
        this.luasPermukaan = luasPermukaan;
        this.jarakSensor = jarakSensor;
    }

    public Setting() {

    }


    public float getPenyiramanOtomatis() {
        return penyiramanOtomatis;
    }

    public void setPenyiramanOtomatis(float penyiramanOtomatis) {
        this.penyiramanOtomatis = penyiramanOtomatis;
    }

    public float getTinggiTandon() {
        return tinggiTandon;
    }

    public void setTinggiTandon(float tinggiTandon) {
        this.tinggiTandon = tinggiTandon;
    }

    public float getLuasPermukaan() {
        return luasPermukaan;
    }

    public void setLuasPermukaan(float luasPermukaan) {
        this.luasPermukaan = luasPermukaan;
    }

    public float getJarakSensor() {
        return jarakSensor;
    }

    public void setJarakSensor(float jarakSensor) {
        this.jarakSensor = jarakSensor;
    }
}
