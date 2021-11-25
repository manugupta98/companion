package com.sdpd.companion.viewModel;

import androidx.lifecycle.ViewModel;

import com.github.mikephil.charting.data.PieEntry;

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

        //input data and fit data into pie chart entry
        for(String type: typeAmountMap.keySet()){
            pieEntries.add(new PieEntry(typeAmountMap.get(type).floatValue(), type));
        }

        return pieEntries;
    }

}
