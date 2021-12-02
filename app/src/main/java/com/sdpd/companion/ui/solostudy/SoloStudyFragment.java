package com.sdpd.companion.ui.solostudy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.sdpd.companion.R;
import com.sdpd.companion.viewmodels.SoloStudyViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SoloStudyFragment extends Fragment {
    private static final String TAG = "SoloStudyFragment";

    SoloStudyViewModel soloStudyViewModel;


    TextView topicTextView;
    Button timerButton;
    Button stopwatchButton;

    public SoloStudyFragment(){
        super(R.layout.fragment_solo_study);
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
        View view = inflater.inflate(R.layout.fragment_solo_study, container, false);

        topicTextView = view.findViewById(R.id.solo_study_topic_input);
        timerButton = view.findViewById(R.id.timer_button);
        stopwatchButton = view.findViewById(R.id.stopwatch_button);

        initTimerButton();
        initStopwatchButton();

        return view;
    }

    private void initStopwatchButton() {
        stopwatchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController navController = Navigation.findNavController(view);
                SoloStudyFragmentDirections.ActionSoloStudyFragmentToSoloStudyStopwatchFragment action = SoloStudyFragmentDirections.actionSoloStudyFragmentToSoloStudyStopwatchFragment(topicTextView.getText().toString());
                navController.navigate(action);
            }
        });
    }

    private void initTimerButton() {
        timerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController navController = Navigation.findNavController(view);
                SoloStudyFragmentDirections.ActionSoloStudyFragmentToSoloStudyTimerFragment action = SoloStudyFragmentDirections.actionSoloStudyFragmentToSoloStudyTimerFragment(topicTextView.getText().toString());
                navController.navigate(action);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
    }
}
