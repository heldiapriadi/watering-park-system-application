package com.example.smartwateringpark.database;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.smartwateringpark.database.Converters;
import com.example.smartwateringpark.database.Report;
import com.example.smartwateringpark.database.Setting;
import com.example.smartwateringpark.database.SwapasDatabase_Impl;
import com.example.smartwateringpark.model.AntaresSetting;
import com.example.smartwateringpark.model.ReportViewModel;
import com.example.smartwateringpark.model.SettingViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import id.co.telkom.iot.AntaresHTTPAPI;
import id.co.telkom.iot.AntaresResponse;

public class BackgroundTask extends Worker implements AntaresHTTPAPI.OnResponseListener {
    private AntaresHTTPAPI antaresAPIHTTP;
    private SettingViewModel settingViewModel;
    private ReportViewModel reportViewModel;
    private String dataDevice;
    private int TAG;
    private ReportDao reportDao;
    private SettingDao settingDao;


    public BackgroundTask(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        SwapasDatabase db = SwapasDatabase.getDatabase(context);
        reportDao = db.reportDao();
        settingDao = db.settingDao();
    }

    @NonNull
    @Override
    public Result doWork() {
        antaresAPIHTTP = new AntaresHTTPAPI();
        antaresAPIHTTP.addListener(this);
        TAG = 1;
        antaresAPIHTTP.getLatestDataofDevice(AntaresSetting.accessKey,AntaresSetting.porjectName,AntaresSetting.devineName[0]);
        antaresAPIHTTP.getLatestDataofDevice(AntaresSetting.accessKey,AntaresSetting.porjectName,AntaresSetting.devineName[1]);
        Log.d("TESTTT","MASOK");

        return Result.success();
    }

    @Override
    public void onResponse(AntaresResponse antaresResponse) {
        if(antaresResponse.getRequestCode()==0){
            try {
                JSONObject body = new JSONObject(antaresResponse.getBody());
                dataDevice = body.getJSONObject("m2m:cin").getString("con");
                JSONObject obj = new JSONObject(dataDevice);
                Log.d("TESTTT",dataDevice);

                try {
                    String timeStamp = obj.getString("timestamp") + "000";
                    int nilaiKelembapanTanah = obj.getInt("nilaiKelembabanTanah");
                    float volumeAirTerpakai = (float)obj.getDouble("volumeAirTerpakai");

                    Report report = new Report(Converters.toDate(Long.valueOf(timeStamp)),nilaiKelembapanTanah,volumeAirTerpakai);
                    reportDao.insert(report);
                    Log.d("TESTTT","DATA 1");

                }catch (Exception e){
                    float totalAirTandon = (float) obj.getDouble("totalAirTandon");
                    settingDao.update(new Setting("totalAirTandon",totalAirTandon));
                    Log.d("TESTTT","DATA 2");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}