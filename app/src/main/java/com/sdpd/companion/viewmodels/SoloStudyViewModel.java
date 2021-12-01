package com.sdpd.companion.viewmodels;

import android.os.SystemClock;
import android.util.Log;
import android.widget.Chronometer;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.sdpd.companion.data.repository.GroupRepository;
import com.sdpd.companion.data.repository.UserRepository;
import com.sdpd.companion.ui.solostudy.SoloStudyFragment;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class SoloStudyViewModel extends ViewModel {

    private static final String TAG = "SoloStudyViewModel";

    private MutableLiveData<String> topic = new MutableLiveData<>("");

    private Chronometer chronometer;
    private boolean isChronometerRunning = false;
    private long timeWhenStopped = 0;

    private long timeLeft = 3 * 60 * 1000;

    public MutableLiveData<String> getTopic() {
        return topic;
    }

    GroupRepository groupRepository;
    UserRepository userRepository;

    @Inject
    public SoloStudyViewModel(SavedStateHandle handle, GroupRepository groupRepository, UserRepository userRepository) {
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
    }

    public void initStopwatch(Chronometer chronometer){
        this.chronometer = chronometer;
    }

    public void startStopwatch(){
        if (!isChronometerRunning) {
            chronometer.setBase(SystemClock.elapsedRealtime() + timeWhenStopped);
            chronometer.start();
            isChronometerRunning = true;
            Log.d(TAG, chronometer.getText().toString());
        }
    }

    public void pauseStopwatch(){
        if (isChronometerRunning) {
            chronometer.stop();
            timeWhenStopped = chronometer.getBase() - SystemClock.elapsedRealtime();
            isChronometerRunning = false;
            Log.d(TAG, chronometer.getText().toString());
        }
    }

    public void resetStopwatch(){
        chronometer.setBase(SystemClock.elapsedRealtime());
        timeWhenStopped = 0;
        chronometer.stop();
        isChronometerRunning = false;
    }

    public void setTopic(String topic){
        this.topic.setValue(topic);
    }

    public void initTimer(Chronometer chronometer) {
        this.chronometer = chronometer;
        chronometer.setCountDown(true);
        chronometer.setBase(SystemClock.elapsedRealtime() + timeLeft);
    }

    public void resetTimer() {
        chronometer.setBase(SystemClock.elapsedRealtime());
        timeLeft = 0;
        chronometer.stop();
        isChronometerRunning = false;
    }

    public void pauseTimer() {
        if (isChronometerRunning) {
            chronometer.stop();
            timeLeft = chronometer.getBase() - SystemClock.elapsedRealtime();
            isChronometerRunning = false;
            Log.d(TAG, chronometer.getText().toString());
        }
    }

    public void startTimer() {
        if (!isChronometerRunning) {
            chronometer.setBase(SystemClock.elapsedRealtime() + timeLeft);
            chronometer.start();
            isChronometerRunning = true;
            Log.d(TAG, chronometer.getText().toString());
        }
    }
}
