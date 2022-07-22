package com.example.smartwateringpark.data;

public class SettingsData {
    private static float penyiramanOtomatis = 1;

    private static float tinggiTandon = 5;

    private static  float luasPermukaan = (float) 153.8;

    private static float jarakSensor = 19;

    private static final Setting Instance = new Setting(1,5,(float)153,19);

    public static Setting getSetting() {
        return Instance;
    }

}
