package com.example.smartwateringpark;

import android.app.DatePickerDialog;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;


import com.example.smartwateringpark.database.Converters;
import com.example.smartwateringpark.database.Report;
import com.example.smartwateringpark.database.Setting;
import com.example.smartwateringpark.model.AntaresSetting;
import com.example.smartwateringpark.model.ReportViewModel;
import com.example.smartwateringpark.model.SettingViewModel;
import com.example.smartwateringpark.ui.ListMonitoringFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import id.co.telkom.iot.AntaresHTTPAPI;
import id.co.telkom.iot.AntaresResponse;


public class MonitoringHistoryActivity extends AppCompatActivity implements AntaresHTTPAPI.OnResponseListener  {
    private AntaresHTTPAPI antaresAPIHTTP;
    private String dataDevice;
    private SettingViewModel settingViewModel;
    private ReportViewModel reportViewModel;
    final Calendar myCalendar = Calendar.getInstance();
    TextView date_picker;
//    FragmentContainerView fragmentListMonitoring;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitoring_history);
        date_picker = findViewById(R.id.date_picker);
        reportViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(ReportViewModel.class);
        settingViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(SettingViewModel.class);


        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setTitle("Monitoring History");
        }
        updateLabel();

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };
        date_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(MonitoringHistoryActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void updateLabel() {
        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        date_picker.setText(sdf.format(myCalendar.getTime()));

//        Bundle bundle = new Bundle();
//        bundle.putString("date", sdf.format(myCalendar.getTime()));
//        ListMonitoringFragment fragobj = new ListMonitoringFragment();
//        fragobj.setArguments(bundle);
//        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_list_monitoring,fragobj).addToBackStack(null).commit();
            ListMonitoringFragment fragobj = new ListMonitoringFragment();
            loadFragment(fragobj);

    }

    //BUTTON REFRESH DATA
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
                    antaresAPIHTTP = new AntaresHTTPAPI();
                    antaresAPIHTTP.addListener(this);
                    antaresAPIHTTP.getLatestDataofDevice(AntaresSetting.accessKey,AntaresSetting.porjectName,AntaresSetting.devineName[0]);
                    antaresAPIHTTP.getLatestDataofDevice(AntaresSetting.accessKey,AntaresSetting.porjectName,AntaresSetting.devineName[1]);
//                Toast.makeText(this, "Refresh Selected",Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void loadFragment(Fragment fragment){
        Bundle arguments = new Bundle();
        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        arguments.putString("date",sdf.format(myCalendar.getTime()) );
        fragment.setArguments(arguments);
        // create a FragmentManager
        FragmentManager fm = getSupportFragmentManager();
// create a FragmentTransaction to begin the transaction and replace the Fragment
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
// replace the FrameLayout with new Fragment
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit(); // save the changes


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

                    finish();
                    overridePendingTransition( 0, 0);
                    startActivity(getIntent());
                    overridePendingTransition( 0, 0);
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
