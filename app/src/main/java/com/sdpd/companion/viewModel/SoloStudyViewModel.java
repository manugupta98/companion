package com.sdpd.companion.viewModel;

import android.content.Context;

import androidx.lifecycle.ViewModel;

import com.sdpd.companion.data.repository.SoloStudyRepository;

public class SoloStudyViewModel extends ViewModel {

    String searchedText;
    int stopWatchSeconds;
    boolean stopWatchRunning,stopWatchWasRunning,timerRunning;
    int timerSeconds,timerUserHours,timerUserMinutes,timerUserSeconds;
    private Context context;

    public void init(Context context) {
        this.context = context;
    }

    public void addAppSpentTime(int timeToAdd){
        SoloStudyRepository soloStudyRepository=new SoloStudyRepository(context);
        soloStudyRepository.updateTimeSolo(timeToAdd);
    }

    public String getSearchedText() {
        return searchedText;
    }

    public void setSearchedText(String searchedText) {
        this.searchedText = searchedText;
    }

    public int getStopWatchSeconds() {
        return stopWatchSeconds;
    }

    public void setStopWatchSeconds(int stopWatchSeconds) {
        this.stopWatchSeconds = stopWatchSeconds;
    }

    public boolean isStopWatchRunning() {
        return stopWatchRunning;
    }

    public void setStopWatchRunning(boolean stopWatchRunning) {
        this.stopWatchRunning = stopWatchRunning;
    }

    public boolean isStopWatchWasRunning() {
        return stopWatchWasRunning;
    }

    public void setStopWatchWasRunning(boolean stopWatchWasRunning) {
        this.stopWatchWasRunning = stopWatchWasRunning;
    }

    public int getTimerSeconds() {
        return timerSeconds;
    }

    public void setTimerSeconds(int timerSeconds) {
        this.timerSeconds = timerSeconds;
    }

    public int getTimerUserHours() {
        return timerUserHours;
    }

    public void setTimerUserHours(int timerUserHours) {
        this.timerUserHours = timerUserHours;
    }

    public int getTimerUserMinutes() {
        return timerUserMinutes;
    }

    public void setTimerUserMinutes(int timerUserMinutes) {
        this.timerUserMinutes = timerUserMinutes;
    }

    public int getTimerUserSeconds() {
        return timerUserSeconds;
    }

    public void setTimerUserSeconds(int timerUserSeconds) {
        this.timerUserSeconds = timerUserSeconds;
    }

    public boolean isTimerRunning() {
        return timerRunning;
    }

    public void setTimerRunning(boolean timerRunning) {
        this.timerRunning = timerRunning;
    }
}
