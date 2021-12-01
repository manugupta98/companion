package com.sdpd.companion.ui.solostudy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.sdpd.companion.R;
import com.sdpd.companion.viewmodels.SoloStudyViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class StopwatchFragment extends Fragment {
    private static final String TAG = "StopwatchFragment";

    SoloStudyViewModel soloStudyViewModel;

    Button startButton;
    Button pauseButton;
    Button resetButton;
    Chronometer chronometer;
    CircularProgressIndicator circularProgressIndicator;

    public StopwatchFragment(){
        super(R.layout.fragment_stopwatch);
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
        View view = inflater.inflate(R.layout.fragment_stopwatch, container, false);

        startButton = view.findViewById(R.id.start_button);
        pauseButton = view.findViewById(R.id.pause_button);
        resetButton = view.findViewById(R.id.reset_button);
        chronometer = view.findViewById(R.id.chronometer_stopwatch);
        circularProgressIndicator = view.findViewById(R.id.progressBar);

//        circularProgressIndicator.setIndicatorSize(1000);
        circularProgressIndicator.setProgress(20);


        soloStudyViewModel.initStopwatch(chronometer);

        initStartButton();
        initPauseButton();
        initResetButton();

        return view;
    }

    private void initResetButton() {
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soloStudyViewModel.resetStopwatch();
            }
        });
    }

    private void initPauseButton() {
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soloStudyViewModel.pauseStopwatch();
            }
        });
    }

    private void initStartButton() {
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soloStudyViewModel.startStopwatch();
            }
        });
    }
}
