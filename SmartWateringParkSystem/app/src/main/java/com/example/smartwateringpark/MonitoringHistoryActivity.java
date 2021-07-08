package com.example.smartwateringpark;

import android.app.DatePickerDialog;
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
import androidx.fragment.app.FragmentContainerView;

import com.example.smartwateringpark.ui.ListMonitoringFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class MonitoringHistoryActivity extends AppCompatActivity {

    final Calendar myCalendar = Calendar.getInstance();
    TextView date_picker;
    FragmentContainerView fragmentListMonitoring;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitoring_history);
        date_picker = findViewById(R.id.date_picker);

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

            fragobj.UpdateList(sdf.format(myCalendar.getTime()));

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
                Toast.makeText(this, "Refresh Selected",Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
