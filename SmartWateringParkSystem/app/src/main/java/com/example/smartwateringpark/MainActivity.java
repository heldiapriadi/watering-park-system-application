package com.example.smartwateringpark;

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
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResponse(AntaresResponse antaresResponse) {
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
                    float totalAirTandon = (float) obj.getDouble("totalAirTandon");
                    settingViewModel.update(new Setting("totalAirTandon",totalAirTandon));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
