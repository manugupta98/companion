package com.sdpd.companion.viewmodels;

import android.os.SystemClock;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.github.mikephil.charting.data.BarEntry;
import com.sdpd.companion.data.repository.AnalyticsRepository;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@HiltViewModel
public class AnalyticsViewModel extends ViewModel {
    private static final String TAG = "AnalyticsViewModel";

    AnalyticsRepository analyticsRepository;

    Disposable trackerDisposable;

    private String currentType;
    private String currentName;
    private Long lastUpdateTime;

    private MutableLiveData<ArrayList<String>> names = new MutableLiveData<>(new ArrayList<>());
    private Map<String, Object> data = new HashMap<>();

    private MutableLiveData<ArrayList<String>> xLabels = new MutableLiveData<>();
    private MutableLiveData<ArrayList<BarEntry>> barEntries = new MutableLiveData<>();

    @Inject
    public AnalyticsViewModel(SavedStateHandle handle, AnalyticsRepository analyticsRepository) {
        this.analyticsRepository = analyticsRepository;
    }

    public void setDetails(String type, String name) {
        this.currentType = type;
        this.currentName = name;
        this.lastUpdateTime = SystemClock.elapsedRealtime();
    }

    public void startTracking() {
        lastUpdateTime = SystemClock.elapsedRealtime();
        if (trackerDisposable != null && !trackerDisposable.isDisposed()) {
            trackerDisposable.dispose();
        }
        trackerDisposable = Flowable.interval(60, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                    analyticsRepository.updateAnalytics(currentType, currentName, 60000L);
                    lastUpdateTime = SystemClock.elapsedRealtime();
                });
    }

    public void stopTracking() {
        if (trackerDisposable != null && !trackerDisposable.isDisposed()) {
            analyticsRepository.updateAnalytics(currentType, currentName, SystemClock.elapsedRealtime() - lastUpdateTime);
            lastUpdateTime = SystemClock.elapsedRealtime();
            trackerDisposable.dispose();
        }
    }


    public void fetchGroups(String type) {
        analyticsRepository.fetchGroups(type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(data -> {
                    this.data = data;
                    Log.d(TAG, data.toString());
                    names.setValue(new ArrayList<>(data.keySet()));
                });
        ;
    }

    public void getBarChartData(String name) {
        ArrayList<BarEntry> entries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();
        if (name.equals("")){
            barEntries.setValue(entries);
            xLabels.setValue(labels);
            return;
        }
        int counter = 0;
        TreeMap<String, Long> sortedEntries;
        if (name.equals("All")) {
            sortedEntries = new TreeMap();
            for (String key : data.keySet()) {
                for (Map.Entry<String, Long> entry : ((Map<String, Long>) data.get(key)).entrySet()) {
                    if (sortedEntries.containsKey(entry.getKey())) {
                        sortedEntries.put(entry.getKey(), sortedEntries.get(entry.getKey()) + entry.getValue());
                    } else {
                        sortedEntries.put(entry.getKey(), entry.getValue());
                    }
                }
            }
        } else {
            sortedEntries = new TreeMap((Map<String, Long>) data.get(name));
        }
        for (Map.Entry<String, Long> entry : sortedEntries.entrySet()) {
            Log.d(TAG, entry.toString());
            entries.add(new BarEntry(counter++, entry.getValue()));
            labels.add(entry.getKey());
        }

        barEntries.setValue(entries);
        xLabels.setValue(labels);
    }

    public MutableLiveData<ArrayList<String>> getxLabels() {
        return xLabels;
    }

    public MutableLiveData<ArrayList<BarEntry>> getBarEntries() {
        return barEntries;
    }

    public MutableLiveData<ArrayList<String>> getNames() {
        return names;
    }
}
