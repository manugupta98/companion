package com.sdpd.companion.viewModel;

import androidx.lifecycle.ViewModel;

import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

import java.util.Calendar;
import java.util.Date;

public class StatsViewModel extends ViewModel {

    public BarGraphSeries<DataPoint> getDailySoloSeries() {
        return new BarGraphSeries<>(new DataPoint[] {
                new DataPoint(0.7, 5)
        });
    }

    public BarGraphSeries<DataPoint> getDailyGroupSeries() {
        return new BarGraphSeries<>(new DataPoint[] {
                new DataPoint(2.3, 4),
        });
    }

    public Double getTotalDailyTimeSpent() {
        return 7.0;
    }

    public BarGraphSeries<DataPoint> getWeeklySoloSeries() {
        Calendar calendar = Calendar.getInstance();
        Date d1 = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        Date d2 = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        Date d3 = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        Date d4 = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        Date d5 = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        Date d6 = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        Date d7 = calendar.getTime();

        return new BarGraphSeries<>(new DataPoint[] {
                new DataPoint(d1, 5),
                new DataPoint(d2, 7),
                new DataPoint(d3, 8),
                new DataPoint(d4, 10),
                new DataPoint(d5, 10),
                new DataPoint(d6, 11),
                new DataPoint(d7, 12)
        });
    }

    public BarGraphSeries<DataPoint> getWeeklyGroupSeries() {
        Calendar calendar = Calendar.getInstance();
        Date d1 = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        Date d2 = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        Date d3 = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        Date d4 = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        Date d5 = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        Date d6 = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        Date d7 = calendar.getTime();

        return new BarGraphSeries<>(new DataPoint[] {
                new DataPoint(d1, 5),
                new DataPoint(d2, 7),
                new DataPoint(d3, 8),
                new DataPoint(d4, 10),
                new DataPoint(d5, 10),
                new DataPoint(d6, 11),
                new DataPoint(d7, 12)
        });
    }
}
