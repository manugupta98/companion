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
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.sdpd.companion.R;
import com.sdpd.companion.viewmodels.AnalyticsViewModel;
import com.sdpd.companion.viewmodels.SoloStudyViewModel;
import com.sdpd.companion.viewmodels.StopwatchViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class StopwatchFragment extends Fragment {
    private static final String TAG = "StopwatchFragment";

    StopwatchViewModel stopwatchViewModel;
    AnalyticsViewModel analyticsViewModel;

    Button startButton;
    Button pauseButton;
    Button resetButton;
    Chronometer chronometer;
    CircularProgressIndicator circularProgressIndicator;

    public StopwatchFragment() {
        super(R.layout.fragment_stopwatch);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        stopwatchViewModel = new ViewModelProvider(getActivity()).get(StopwatchViewModel.class);
        analyticsViewModel = new ViewModelProvider(getActivity()).get(AnalyticsViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_stopwatch, container, false);

        startButton = view.findViewById(R.id.start_button);
        pauseButton = view.findViewById(R.id.pause_button);
        resetButton = view.findViewById(R.id.reset_button);
        chronometer = view.findViewById(R.id.chronometer_stopwatch);
        circularProgressIndicator = view.findViewById(R.id.progressBar);

        String topic = StopwatchFragmentArgs.fromBundle(getArguments()).getTopic();

        analyticsViewModel.setDetails("solo", topic);

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
                stopwatchViewModel.updateTime(chronometer.getBase());
            }
        });
        stopwatchViewModel.getTime().observeForever(time -> {
            Long seconds = (0 - time) % 60000;
            int progress = (int) ((seconds * 100) / 60000);
            circularProgressIndicator.setProgress(progress, true);
        });
        stopwatchViewModel.getIsChronometerRunning().observe(getViewLifecycleOwner(), value -> {
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
                chronometer.setBase(SystemClock.elapsedRealtime());
                chronometer.stop();
                stopwatchViewModel.resetStopwatch();
            }
        });
    }

    private void initPauseButton() {
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chronometer.stop();
                stopwatchViewModel.pauseStopwatch();
            }
        });
    }

    private void initStartButton() {
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Long time = stopwatchViewModel.getTime().getValue();
                chronometer.setBase(SystemClock.elapsedRealtime() + time);
                chronometer.start();
                stopwatchViewModel.startStopwatch();
            }
        });
    }
}
