package com.sdpd.companion.ui.analytics;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputLayout;
import com.sdpd.companion.R;
import com.sdpd.companion.viewmodels.AnalyticsViewModel;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AnalyticsFragment extends Fragment {
    private static final String TAG = "AnalyticsFragment";

    AnalyticsViewModel analyticsViewModel;

    TextInputLayout typeDropdown;
    ArrayAdapter<String> typeAdapter;
    TextInputLayout nameDropdown;
    ArrayAdapter<String> nameAdapter;
    BarChart barChart;

    private final ArrayList<String> types = new ArrayList<>(Arrays.asList("Group Study", "Solo Study"));

    public AnalyticsFragment() {
        super(R.layout.fragment_analytics);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        analyticsViewModel = new ViewModelProvider(getActivity()).get(AnalyticsViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_analytics, container, false);
        typeDropdown = view.findViewById(R.id.type_dropdown);
        nameDropdown = view.findViewById(R.id.name_dropdown);
        barChart = view.findViewById(R.id.bar_chart);
        barChart.setData(null);
        barChart.invalidate();
        barChart.clear();
        typeAdapter = new ArrayAdapter<String>(getContext(), R.layout.list_item, types);
        ((MaterialAutoCompleteTextView) typeDropdown.getEditText()).setAdapter(typeAdapter);
        ((MaterialAutoCompleteTextView) typeDropdown.getEditText()).setText("", false);
        nameAdapter = new ArrayAdapter<String>(getContext(), R.layout.list_item);
        ((MaterialAutoCompleteTextView) nameDropdown.getEditText()).setAdapter(nameAdapter);
        ((MaterialAutoCompleteTextView) nameDropdown.getEditText()).setText("", false);
        analyticsViewModel.fetchGroups("");
        observeDropdown();
        return view;
    }

    private void observeDropdown() {
        ((AutoCompleteTextView) typeDropdown.getEditText()).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                ((MaterialAutoCompleteTextView) nameDropdown.getEditText()).setText("", false);
                String selectedType = typeAdapter.getItem(position);
                if (selectedType.equals("Group Study")) {
                    selectedType = "study";
                } else if (selectedType.equals("Solo Study")) {
                    selectedType = "solo";
                }
                barChart.setData(null);
                barChart.invalidate();
                barChart.clear();
                analyticsViewModel.fetchGroups(selectedType);
            }
        });

        analyticsViewModel.getNames().observeForever(namesList -> {
            Log.d(TAG, namesList.toString());
            nameAdapter.clear();
            nameAdapter.addAll(namesList);
        });

        ((AutoCompleteTextView) nameDropdown.getEditText()).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Log.d(TAG, position + "");
                String selectedName = nameAdapter.getItem(position);

                analyticsViewModel.getBarChartData(selectedName);
            }
        });
        analyticsViewModel.getxLabels().observe(getViewLifecycleOwner(), labels -> {
            ArrayList<BarEntry> entries = analyticsViewModel.getBarEntries().getValue();
            Log.d(TAG, entries.toString());
            Log.d(TAG, labels.toString());
            BarDataSet barDataSet = new BarDataSet(entries, "");
            barDataSet.setStackLabels(labels.toArray(new String[0]));
            barDataSet.setValueTextSize(15f);
//            barDataSet.setColors(Color.DKGRAY, Color.LTGRAY);
            BarData data = new BarData(barDataSet);
//            data.setBarWidth(0.1f);
            barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
            barChart.setDrawGridBackground(false);
            barChart.setDrawBarShadow(false);

            XAxis xAxis = barChart.getXAxis();
//            xAxis.setCenterAxisLabels(false);
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setDrawGridLines(false);
            xAxis.setDrawAxisLine(true);

            YAxis leftAxis = barChart.getAxisLeft();
            leftAxis.setLabelCount(5, false);
            leftAxis.setSpaceTop(20f);
            leftAxis.setAxisMinimum(0f);

            barChart.setData(data);
            barChart.getDescription().setEnabled(false);
            barChart.animateXY(1000, 1000);
            barChart.invalidate();

        });
    }


    @Override
    public void onStart() {
        super.onStart();
    }
}
