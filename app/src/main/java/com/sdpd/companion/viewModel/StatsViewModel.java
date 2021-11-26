package com.sdpd.companion.viewModel;

import androidx.lifecycle.ViewModel;

import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieEntry;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class StatsViewModel extends ViewModel {

    public ArrayList<PieEntry> getDailyStats() {
        ArrayList<PieEntry> pieEntries = new ArrayList<>();
        Map<String, Integer> typeAmountMap = new HashMap<>();
        typeAmountMap.put("Toys",200);
        typeAmountMap.put("Snacks",230);
        typeAmountMap.put("Clothes",100);
        typeAmountMap.put("Stationary",500);
        typeAmountMap.put("Phone",50);

        for(String type: typeAmountMap.keySet()){
            pieEntries.add(new PieEntry(typeAmountMap.get(type).floatValue(), type));
        }

        return pieEntries;
    }

    public ArrayList<BarEntry> getWeeklyStats() {
        ArrayList<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            BarEntry barEntry = new BarEntry(i, new float[] {10, i*100});
            entries.add(barEntry);
        }
        return entries;
    }

    public ArrayList<String> getAxisLabels(int n) {
        DateFormat df = new SimpleDateFormat("dd/MM/yy");
        ArrayList<String> labels = new ArrayList<>();
        for(int i = 0; i < n; i++) {
            labels.add(String.valueOf(df.format((new Date()).getTime())));
        }
        return labels;
    }

}
