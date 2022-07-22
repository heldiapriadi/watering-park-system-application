package com.example.smartwateringpark.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartwateringpark.R;
import com.example.smartwateringpark.adapter.RecycleviewAdapter;
import com.example.smartwateringpark.model.ReportViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ListMonitoringFragment extends Fragment {
    final Calendar myCalendar = Calendar.getInstance();
    private RecyclerView recyclerview;
    private ReportViewModel mReportViewModel;
    private String millis;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_monitoring, container, false);
        recyclerview = (RecyclerView) view.findViewById(R.id.recyclerView);
        mReportViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())).get(ReportViewModel.class);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
           millis = bundle.getString("date", "01/07/21");
        }

        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        UpdateList(millis);

        return view;
    }

    public void UpdateList(String date) {
        try {

            String myFormat = "dd/MM/yy"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
            Date d = sdf.parse(date);
            long fromDate = d.getTime();
            long toDate = fromDate + (1000 * 60 * 60 * 24);
            String td = sdf.format( new Date(toDate));
            d = sdf.parse(td);
            toDate = d.getTime();
            Log.d("TESTTT",String.valueOf(fromDate));
            Log.d("TESTTT",String.valueOf(toDate));

            mReportViewModel.getAllFilterReports(fromDate,toDate).observe(getViewLifecycleOwner(), reports -> {
                // Update the cached copy of the words in the adapter.
                if (reports == null){
                    return;
                }
                    RecycleviewAdapter recyclerAdapter = new RecycleviewAdapter(getActivity(),reports);
                    recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
                    recyclerview.setAdapter(recyclerAdapter);

            });
        }catch (Exception e){
            e.printStackTrace();
            Log.d("TESTTT","GAMASOK");
        }

    }
}
