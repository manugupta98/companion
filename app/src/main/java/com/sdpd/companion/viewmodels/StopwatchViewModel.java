package com.sdpd.companion.viewmodels;

import android.os.SystemClock;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.sdpd.companion.ui.solostudy.StopwatchFragment;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class StopwatchViewModel extends ViewModel {
    private static final String TAG = "StopwatchViewModel";

    private MutableLiveData<String> topic = new MutableLiveData<>("");


    private MutableLiveData<Long> time = new MutableLiveData<Long>(0L);
    private MutableLiveData<Boolean> isChronometerRunning = new MutableLiveData<>(false);


    public MutableLiveData<Boolean> getIsChronometerRunning() {
        return isChronometerRunning;
    }

    @Inject
    public StopwatchViewModel(SavedStateHandle handle) {
    }

    public void setTopic(String topic) {
        this.topic.setValue(topic);
    }

    public void resetStopwatch() {
        time.setValue(0L);
        isChronometerRunning.setValue(false);
    }

    public void pauseStopwatch() {
        isChronometerRunning.setValue(false);
    }

    public void startStopwatch() {
        isChronometerRunning.setValue(true);
    }

    public void setTime(Long time) {
        this.time.setValue(time);
    }

    public void updateTime(long chronometerBase) {
        time.setValue(chronometerBase - SystemClock.elapsedRealtime());
    }

    public MutableLiveData<Long> getTime() {
        return time;
    }
}
