package com.sdpd.companion.ui.login;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
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
import java.util.Date;

public class StudyStatsFragment extends Fragment implements View.OnClickListener {

    private GraphView barGraph;
    private Button weeklyStats;
    private StatsViewModel mStatsViewModel;
    private BarGraphSeries<DataPoint> soloSeries, groupSeries;

    public StudyStatsFragment() {
        // Required empty public constructor
        super(R.layout.fragment_study_stats);
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
        View view = inflater.inflate(R.layout.fragment_study_stats, container, false);
        barGraph = view.findViewById(R.id.dailyStudyStats);
        soloSeries = mStatsViewModel.getDailySoloSeries();
        groupSeries = mStatsViewModel.getDailyGroupSeries();
        graphConfigs(barGraph, soloSeries, groupSeries);

        TextView finalStats = view.findViewById(R.id.studyStatsFinal);
        finalStats.setText("Total time spent: " + mStatsViewModel.getTotalDailyTimeSpent() + " hours");

        weeklyStats = view.findViewById(R.id.weeklyReport);
        weeklyStats.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        final FragmentTransaction transaction = getFragmentManager().beginTransaction().setReorderingAllowed(true);
        transaction.replace(R.id.fragment_container_view, StudyStatsWeeklyFragment.class, null);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void graphConfigs(GraphView barGraph, BarGraphSeries<DataPoint> soloSeries, BarGraphSeries<DataPoint> groupSeries) {
        barGraph.addSeries(groupSeries);
        barGraph.addSeries(soloSeries);
        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(barGraph);
        staticLabelsFormatter.setHorizontalLabels(new String[] {"", "Solo", "Group", ""});
        barGraph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
        barGraph.getViewport().setMinX(0);
        barGraph.getViewport().setMaxX(3);
        barGraph.getViewport().setMinY(0);
        barGraph.getViewport().setXAxisBoundsManual(true);
        barGraph.getViewport().setYAxisBoundsManual(true);
        barGraph.setTitle("Time spent in hours");
        soloSeries.setAnimated(true);
        groupSeries.setAnimated(true);
        soloSeries.setSpacing(30);
        groupSeries.setSpacing(30);

        groupSeries.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
                Date date = new Date((long) dataPoint.getX());
                DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                Toast.makeText(getActivity(), "Time: " + dataPoint.getY() + "hrs", Toast.LENGTH_SHORT).show();
            }
        });

        soloSeries.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
                Date date = new Date((long) dataPoint.getX());
                DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                Toast.makeText(getActivity(), "Time: " + dataPoint.getY() + "hrs", Toast.LENGTH_SHORT).show();
            }
        });
    }
}