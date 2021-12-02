package com.sdpd.companion.ui.solostudy;

import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.sdpd.companion.R;
import com.sdpd.companion.viewmodels.AnalyticsViewModel;
import com.sdpd.companion.viewmodels.SoloStudyViewModel;
import com.sdpd.companion.viewmodels.TimerViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class TimerFragment extends Fragment {
    private static final String TAG = "TimerFragment";

    TimerViewModel timerViewModel;
    AnalyticsViewModel analyticsViewModel;

    ImageView startButton;
    ImageView pauseButton;
    ImageView resetButton;
    Chronometer chronometer;
    CircularProgressIndicator circularProgressIndicator;

    public TimerFragment(){
        super(R.layout.fragment_timer);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        timerViewModel = new ViewModelProvider(getActivity()).get(TimerViewModel.class);
        analyticsViewModel = new ViewModelProvider(getActivity()).get(AnalyticsViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_timer, container, false);

        startButton = view.findViewById(R.id.start_button);
        pauseButton = view.findViewById(R.id.pause_button);
        resetButton = view.findViewById(R.id.reset_button);
        chronometer = view.findViewById(R.id.chronometer_timer);
        circularProgressIndicator = view.findViewById(R.id.progressBar);
        String topic = TimerFragmentArgs.fromBundle(getArguments()).getTopic();

        analyticsViewModel.setDetails("solo", topic);

//        circularProgressIndicator.setIndicatorSize(1000);
        chronometer.setCountDown(true);
        chronometer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog().show(getParentFragmentManager(), "dialog");
            }
        });

        initStartButton();
        initPauseButton();
        initResetButton();
        observeProgress();

        return view;
    }

    private void observeProgress() {
        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                if (!timerViewModel.getIsChronometerRunning().getValue())
                    return;
                if ("00:00".equals(chronometer.getText())){
                    resetButton.callOnClick();
                    Toast.makeText(getContext(), "Timer finished", Toast.LENGTH_SHORT);
                    ToneGenerator toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
                    toneGen1.startTone(ToneGenerator.TONE_CDMA_EMERGENCY_RINGBACK,1000);
                }else {
                    timerViewModel.updateTimeLeft(chronometer.getBase());
                }
            }
        });
        timerViewModel.getTotalTime().observeForever(totalTime -> {
            if (totalTime != 0){
                chronometer.setBase(SystemClock.elapsedRealtime() + totalTime);
                startButton.callOnClick();
            }
        });
        timerViewModel.getTimeLeft().observeForever(timeLeft -> {
            Long totalTime = timerViewModel.getTotalTime().getValue();
            if (totalTime == 0){
                circularProgressIndicator.setProgress(100);
            }else {

                circularProgressIndicator.setProgress((int) ((100 * timeLeft) / totalTime), true);
            }
        });
        timerViewModel.getIsChronometerRunning().observe(getViewLifecycleOwner(), value -> {
            if (value){
                analyticsViewModel.startTracking();
            } else {
                analyticsViewModel.stopTracking();
            }

        });
    }

    private void initResetButton() {
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Long totalTime = timerViewModel.getTotalTime().getValue();
                if (totalTime == 0){
                    return;
                }
                timerViewModel.resetTimer();
                timerViewModel.setTimer(0L);
                chronometer.setBase(SystemClock.elapsedRealtime());
                chronometer.stop();
            }
        });
    }

    private void initPauseButton() {
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timerViewModel.getIsChronometerRunning().getValue()) {
                    timerViewModel.pauseTimer();
                    chronometer.stop();
                }
            }
        });
    }

    private void initStartButton() {
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!timerViewModel.getIsChronometerRunning().getValue()) {
                    Long timeLeft = timerViewModel.getTimeLeft().getValue();
                    if (timeLeft != 0) {
                        chronometer.setBase(SystemClock.elapsedRealtime() + timeLeft);
                        chronometer.start();
                        timerViewModel.startTimer();
                    } else {
                        new TimePickerDialog().show(getParentFragmentManager(), "dialog");
                    }
                }
            }
        });
    }
}
