package com.example.smartwateringpark.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartwateringpark.data.Monitoring;
import com.example.smartwateringpark.R;
import com.example.smartwateringpark.adapter.RecycleviewAdapter;
import com.example.smartwateringpark.model.ReportViewModel;

import java.util.ArrayList;
import java.util.List;

public class ListMonitoringFragment extends Fragment {

    private RecyclerView recyclerview;
    private List<Monitoring> listMonitoring;
    private ReportViewModel mReportViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_monitoring, container, false);
        recyclerview = (RecyclerView) view.findViewById(R.id.recyclerView);
        mReportViewModel.getAllReports().observe(getViewLifecycleOwner(), reports -> {
            // Update the cached copy of the words in the adapter.
            RecycleviewAdapter recyclerAdapter = new RecycleviewAdapter(getActivity(),reports);
            recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerview.setAdapter(recyclerAdapter);
        });

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mReportViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())).get(ReportViewModel.class);

        listMonitoring = new ArrayList<>();
        listMonitoring.add(new Monitoring("06/29/21", "07.30", "Moist", (float) 0.5));
        listMonitoring.add(new Monitoring("06/29/21", "08.00", "Moist", (float) 0.0));
        listMonitoring.add(new Monitoring("06/29/21", "09.00", "Moist", (float) 0.1));
        listMonitoring.add(new Monitoring("06/29/21", "10.00", "Moist", (float) 0.01));
    }
}
