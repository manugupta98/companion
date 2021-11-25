package com.sdpd.companion.ui.login;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.sdpd.companion.R;
import com.sdpd.companion.viewModel.StatsViewModel;

import java.util.ArrayList;

public class StatsFragment extends Fragment implements OnChartValueSelectedListener {

    private PieChart dailyPieChart;
    private BarChart weeklyBarchart;
    private StatsViewModel mStatsViewModel;
    private ArrayList<PieEntry> pieEntries;

    public StatsFragment() {
        // Required empty public constructor
        super(R.layout.fragment_stats);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mStatsViewModel = new ViewModelProvider(this).get(StatsViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_stats, container, false);
        dailyPieChart = view.findViewById(R.id.dailyPieChart);
        dailyPieChart.setOnChartValueSelectedListener(this);
        weeklyBarchart = view.findViewById(R.id.weeklyBarChart);
        showDailyStats();
        showBarChart();
        return view;
    }

    private void showDailyStats(){
        pieEntries = mStatsViewModel.getDailyStats();
        PieDataSet pieDataSet = new PieDataSet(pieEntries,"");
        pieDataSet.setValueTextSize(20f);
        pieDataSet.setColors(ColorTemplate.PASTEL_COLORS);
        PieData pieData = new PieData(pieDataSet);
        pieData.setDrawValues(true);
        pieData.setValueFormatter(new PercentFormatter(dailyPieChart));
        dailyPieChart.setUsePercentValues(true);
        dailyPieChart.setCenterTextSize(15f);
        dailyPieChart.getDescription().setEnabled(false);
        dailyPieChart.setData(pieData);
        dailyPieChart.setDrawEntryLabels(false);
        dailyPieChart.setCenterText("Today's stats");
        dailyPieChart.animateXY(1000, 1000);
        dailyPieChart.invalidate();
    }

    private void showBarChart(){
        ArrayList<Double> valueList = new ArrayList<Double>();
        ArrayList<BarEntry> entries = new ArrayList<>();

        //input data
        for(int i = 1; i < 6; i++){
            valueList.add(i * 100.0);
        }

        //fit the data into a bar
        for (int i = 0; i < valueList.size(); i++) {
            BarEntry barEntry = new BarEntry(i, valueList.get(i).floatValue());
            entries.add(barEntry);
        }

        BarDataSet barDataSet = new BarDataSet(entries, "");
        barDataSet.setValueTextSize(20f);

        BarData data = new BarData(barDataSet);
        weeklyBarchart.setData(data);
        weeklyBarchart.getDescription().setEnabled(false);
        weeklyBarchart.getLegend().setEnabled(false);
        weeklyBarchart.animateXY(1000, 1000);
        weeklyBarchart.invalidate();
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        int index = (int) h.getX();
        dailyPieChart.setCenterText("Time spent on " + pieEntries.get(index).getLabel() + "\n" + pieEntries.get(index).getValue() + "hrs");
        dailyPieChart.invalidate();
    }

    @Override
    public void onNothingSelected() {
        dailyPieChart.setCenterText("Today's stats");
        dailyPieChart.invalidate();
    }
}