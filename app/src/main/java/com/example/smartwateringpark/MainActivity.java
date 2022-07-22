package com.example.smartwateringpark;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.smartwateringpark.database.BackgroundTask;
import com.example.smartwateringpark.database.Converters;
import com.example.smartwateringpark.database.Report;
import com.example.smartwateringpark.database.ReportDao;
import com.example.smartwateringpark.database.Setting;
import com.example.smartwateringpark.database.SettingDao;
import com.example.smartwateringpark.model.AntaresSetting;
import com.example.smartwateringpark.model.ReportViewModel;
import com.example.smartwateringpark.model.SettingViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import id.co.telkom.iot.AntaresHTTPAPI;
import id.co.telkom.iot.AntaresResponse;

public class MainActivity extends AppCompatActivity implements AntaresHTTPAPI.OnResponseListener {
    private AntaresHTTPAPI antaresAPIHTTP;
    private String dataDevice;
    private SettingViewModel settingViewModel;
    private ReportViewModel reportViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        reportViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(ReportViewModel.class);
        settingViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(SettingViewModel.class);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setTitle("Main");
        }

        //Initialize and run Worker
        Constraints.Builder constraints = new Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED);
        final PeriodicWorkRequest periodicWorkRequest1 = new PeriodicWorkRequest.Builder(BackgroundTask.class,15, TimeUnit.MINUTES)
                .setConstraints(constraints.build())
                .build();
        WorkManager workManager = WorkManager.getInstance(getApplicationContext());
        workManager.enqueueUniquePeriodicWork("Counter", ExistingPeriodicWorkPolicy.KEEP,periodicWorkRequest1);
        Log.d("TESTTT","TAG");

        //Initialize Navigation
        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_soil_monitoring, R.id.navigation_water_storage, R.id.navigation_settings)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
        

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.refresh_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh_menu:
                //Method untuk refresh data disimpan disini
//                Toast.makeText(this, "Refresh Selected",Toast.LENGTH_SHORT).show();
                antaresAPIHTTP = new AntaresHTTPAPI();
                antaresAPIHTTP.addListener(this);
                antaresAPIHTTP.getLatestDataofDevice(AntaresSetting.accessKey,AntaresSetting.porjectName,AntaresSetting.devineName[0]);
                antaresAPIHTTP.getLatestDataofDevice(AntaresSetting.accessKey,AntaresSetting.porjectName,AntaresSetting.devineName[1]);
                antaresAPIHTTP.getLatestDataofDevice(AntaresSetting.accessKey,AntaresSetting.porjectName,AntaresSetting.devineName[2]);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResponse(AntaresResponse antaresResponse) {
        float totalAirTandon = 0;
        float luasPermukaan = 0;
        float tinggiTandon = 0;
        if(antaresResponse.getRequestCode()==0){
            try {
                JSONObject body = new JSONObject(antaresResponse.getBody());
                dataDevice = body.getJSONObject("m2m:cin").getString("con");
                JSONObject obj = new JSONObject(dataDevice);

                try {
                    String timeStamp = obj.getString("timestamp") + "000";
                    int nilaiKelembapanTanah = obj.getInt("nilaiKelembabanTanah");
                    float volumeAirTerpakai = (float)obj.getDouble("volumeAirTerpakai");

                    Report report = new Report(Converters.toDate(Long.valueOf(timeStamp)),nilaiKelembapanTanah,volumeAirTerpakai);
                    reportViewModel.insert(report);

//                    finish();
//                    overridePendingTransition( 0, 0);
//                    startActivity(getIntent());
//                    overridePendingTransition( 0, 0);
                }catch (Exception e){
                    try {
                        totalAirTandon = (float) obj.getDouble("totalAirTandon");
                        settingViewModel.update(new Setting("totalAirTandon",totalAirTandon));
                    }catch (Exception ep){
                        try {
                            luasPermukaan = (float) obj.getDouble("luasPermukaan");
                            tinggiTandon = (float) obj.getDouble("tinggiTandon");
                            float jarakSensor = (float) obj.getDouble("jarakSensor");
                        }catch (Exception ex){
                            ex.printStackTrace();
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }finally {
                float volumeAir = (totalAirTandon / ((luasPermukaan * tinggiTandon) / 1000)) * 100;

                if(volumeAir <= 20){
                    sendNotification("Alert!", "Total air tandon hampir habis!");
                }
            }
        }
    }

    void sendNotification(String title, String message) {
        NotificationManager mNotificationManager =
                (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("1",
                    "android",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("WorkManger");
            mNotificationManager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "1")
                .setSmallIcon(R.mipmap.ic_launcher) // notification icon
                .setContentTitle(title) // title for notification
                .setContentText(message)// message for notification
                .setAutoCancel(true); // clear notification after click
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pi);
        mNotificationManager.notify(0, mBuilder.build());
    }
}
