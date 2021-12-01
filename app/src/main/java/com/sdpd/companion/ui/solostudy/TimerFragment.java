package com.sdpd.companion.ui.solostudy;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TimePicker;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.sdpd.companion.R;
import com.sdpd.companion.viewmodels.SoloStudyViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class TimerFragment extends Fragment {
    private static final String TAG = "TimerFragment";

    SoloStudyViewModel soloStudyViewModel;

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

        soloStudyViewModel = new ViewModelProvider(getActivity()).get(SoloStudyViewModel.class);
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

//        circularProgressIndicator.setIndicatorSize(1000);
        circularProgressIndicator.setProgress(0);

        chronometer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog().show(getParentFragmentManager(), "dialog");
            }
        });

        soloStudyViewModel.initTimer(chronometer);

        initStartButton();
        initPauseButton();
        initResetButton();

        return view;
    }

    private void initResetButton() {
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soloStudyViewModel.resetTimer();
            }
        });
    }

    private void initPauseButton() {
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soloStudyViewModel.pauseTimer();
            }
        });
    }

    private void initStartButton() {
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soloStudyViewModel.startTimer();
            }
        });
    }
}
