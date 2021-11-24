package com.sdpd.companion.ui.login;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;
import com.sdpd.companion.R;
import com.sdpd.companion.viewModel.StatsViewModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class StudyStatsWeeklyFragment extends Fragment {

    private GraphView barGraph, groupBarGraph;
    private StatsViewModel mStatsViewModel;
    private BarGraphSeries<DataPoint> soloSeries, groupSeries;

    public StudyStatsWeeklyFragment() {
        super(R.layout.fragment_study_stats_weekly);
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
        View view = inflater.inflate(R.layout.fragment_study_stats_weekly, container, false);
        barGraph = view.findViewById(R.id.studyStatsWeeklyGraph);

        soloSeries = mStatsViewModel.getWeeklySoloSeries();
        graphConfigs(barGraph, soloSeries);

        groupBarGraph = view.findViewById(R.id.studyStatsWeeklyGroup);
        groupSeries = mStatsViewModel.getWeeklyGroupSeries();
        graphConfigs(groupBarGraph, groupSeries);

        return view;
    }

    private void graphConfigs(GraphView graph, BarGraphSeries<DataPoint> series) {
        graph.addSeries(series);
        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getActivity()));
        graph.getViewport().setMinY(0);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.setTitle("Time spent in hours(Group Study)");
        graph.getGridLabelRenderer().setHorizontalLabelsVisible(false);
        graph.getViewport().setScrollable(true);
        series.setAnimated(true);
        series.setSpacing(30);

        series.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
                Date date = new Date((long) dataPoint.getX());
                DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                Toast.makeText(getActivity(), "Date: " + df.format(date) + "\nTime: " + dataPoint.getY() + "hrs", Toast.LENGTH_SHORT).show();
            }
        });
    }
}