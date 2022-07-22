package com.example.smartwateringpark.data;

import com.example.smartwateringpark.database.Report;

import java.util.ArrayList;

import static com.example.smartwateringpark.data.Converter.toDate;

public class ReportsData {
    private static String[] timeStamp = {
            "1625122800000",
            "1625126400000",
            "1625130000000",
            "1625133600000",
            "1625137200000",
            "1625140800000",
            "1625148000000",
            "1625151600000",
            "1625155200000",
            "1625158800000",
            "1625162400000",
            "1625166000000",
            "1625169600000",
            "1625173200000",
            "1625176800000",
            "1625180400000",
            "1625184000000",
            "1625187600000",
            "1625191200000",
            "1625194800000",
            "1625198400000",
            "1625202000000",
            "1625742361000",
    };

    private static int[] nilaiKelembaban = {
            144,
            168,
            203,
            405,
            504,
            607,
            94,
            150,
            205,
            325,
            465,
            599,
            690,
            84,
            142,
            213,
            342,
            414,
            550,
            610,
            55,
            106,
            212
    };

    private static float[] volumeAirTerpakai = {
            0,
            0,
            0,
            0,
            0,
            0,
            (float) 0.03,
            0,
            0,
            0,
            0,
            0,
            0,
            (float) 0.14,
            0,
            0,
            0,
            0,
            0,
            0,
            (float) 0.01,
            0,
            0
    };


    public static ArrayList<Report> getListReports() {
        ArrayList<Report> list = new ArrayList<>();
        for (int position = 0; position < timeStamp.length; position++) {
            Report report = new Report();
            report.setTimeStamp(toDate(Long.valueOf(timeStamp[position])));
            report.setNilaiKelembabanTanah(nilaiKelembaban[position]);
            report.setVolumeAirTerpakai(volumeAirTerpakai[position]);
            list.add(report);
        }
        return list;
    }


}