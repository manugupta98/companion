package com.sdpd.companion.viewmodels;

import android.os.SystemClock;
import android.util.Log;
import android.widget.Chronometer;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class TimerViewModel extends ViewModel {
    private static final String TAG = "TimerViewModel";

    private MutableLiveData<String> topic = new MutableLiveData<>("");


    private MutableLiveData<Long> totalTime = new MutableLiveData<Long>(0L);
    private MutableLiveData<Long> timeLeft = new MutableLiveData<Long>(0L);
    private MutableLiveData<Boolean> isChronometerRunning = new MutableLiveData<>(false);

    public MutableLiveData<Long> getTotalTime() {
        return totalTime;
    }
    public MutableLiveData<Long> getTimeLeft() {
        return timeLeft;
    }

    public MutableLiveData<Boolean> getIsChronometerRunning() {
        return isChronometerRunning;
    }

    @Inject
    public TimerViewModel(SavedStateHandle handle) {
    }

    public void setTopic(String topic) {
        this.topic.setValue(topic);
    }

    public void resetTimer() {
        timeLeft.setValue(0L);
        isChronometerRunning.setValue(false);
    }

    public void pauseTimer() {
        isChronometerRunning.setValue(false);
    }

    public void startTimer() {
        isChronometerRunning.setValue(true);
    }

    public void setTimer(Long time) {
        timeLeft.setValue(time);
        totalTime.setValue(time);
    }

    public void updateTimeLeft(long chronometerBase) {
        timeLeft.setValue(chronometerBase - SystemClock.elapsedRealtime());
    }
}
