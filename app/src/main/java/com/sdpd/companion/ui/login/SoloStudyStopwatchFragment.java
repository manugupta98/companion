package com.sdpd.companion.ui.login;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.sdpd.companion.R;
import com.sdpd.companion.data.repository.SoloStudyRepository;
import com.sdpd.companion.viewModel.SoloStudyViewModel;

import java.util.Locale;


public class SoloStudyStopwatchFragment extends Fragment {

    private static SoloStudyStopwatchFragment soloStudyStopwatchFragment;
    SoloStudyViewModel soloStudyViewModel;
    Handler handler;
    View view;

    public SoloStudyStopwatchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onPause(){
        super.onPause();
        soloStudyViewModel.setStopWatchWasRunning(soloStudyViewModel.isStopWatchRunning());
        soloStudyViewModel.setStopWatchRunning(false);
        handler.removeCallbacks(my_runnable);
    }

    @Override
    public void onResume(){
        super.onResume();
        if(soloStudyViewModel.isStopWatchWasRunning()){
            soloStudyViewModel.setStopWatchRunning(true);
        }
    }

    public static SoloStudyStopwatchFragment newInstance() {
        SoloStudyStopwatchFragment fragment = new SoloStudyStopwatchFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_solo_study_stopwatch, container, false);
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        handler=new Handler();

        soloStudyViewModel = new ViewModelProvider(requireActivity()).get(SoloStudyViewModel.class);
        this.view=view;
        getActivity().setTitle(soloStudyViewModel.getSearchedText());

        MaterialButton playMusic, endSession, start, stop, reset;

        start = view.findViewById(R.id.start);
        stop = view.findViewById(R.id.stop);
        reset = view.findViewById(R.id.reset);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soloStudyViewModel.setStopWatchRunning(true);
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soloStudyViewModel.setStopWatchRunning(false);
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soloStudyViewModel.setStopWatchRunning(false);
                soloStudyViewModel.setStopWatchSeconds(0);
            }
        });


        runTimer(view, soloStudyViewModel);

        endSession = view.findViewById(R.id.endSession);
        endSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soloStudyViewModel.addAppSpentTime(soloStudyViewModel.getStopWatchSeconds());
                soloStudyViewModel.setSearchedText("");
                soloStudyViewModel.setStopWatchSeconds(0);
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.fragment_container_view, SoloStudyMainFragment.class, null)
                        .setReorderingAllowed(true)
                        .commit();
            }
        });


        playMusic = view.findViewById(R.id.playMusic2);
        playMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("spotify:album:"));
                    intent.putExtra(Intent.EXTRA_REFERRER, Uri.parse("android-app://" + getContext().getPackageName()));
                    startActivity(intent);
                } catch (Exception e) {
                    final String appPackageName = "com.spotify.music";
                    final String referrer = "adjust_campaign=PACKAGE_NAME&adjust_tracker=ndjczk&utm_source=adjust_preinstall";

                    try {
                        Uri uri = Uri.parse("market://details")
                                .buildUpon()
                                .appendQueryParameter("id", appPackageName)
                                .appendQueryParameter("referrer", referrer)
                                .build();
                        startActivity(new Intent(Intent.ACTION_VIEW, uri));
                    } catch (android.content.ActivityNotFoundException ignored) {
                        Uri uri = Uri.parse("https://play.google.com/store/apps/details")
                                .buildUpon()
                                .appendQueryParameter("id", appPackageName)
                                .appendQueryParameter("referrer", referrer)
                                .build();
                        startActivity(new Intent(Intent.ACTION_VIEW, uri));
                    }
                }
            }
        });
    }

    Runnable my_runnable = new Runnable() {
        @Override
        public void run() {
            int hours = soloStudyViewModel.getStopWatchSeconds() / 3600;
            int minutes = (soloStudyViewModel.getStopWatchSeconds() % 3600) / 60;
            int secs = soloStudyViewModel.getStopWatchSeconds() % 60;
            final TextView timeView = (TextView) view.findViewById(R.id.timeDisplay);
            String time = String.format(Locale.getDefault(), "%d:%02d:%02d", hours, minutes, secs);
            timeView.setText(time);

            if (soloStudyViewModel.isStopWatchRunning()) {
                soloStudyViewModel.setStopWatchSeconds(soloStudyViewModel.getStopWatchSeconds() + 1);
            }

            handler.postDelayed(this, 1000);
        }
    };

    private void runTimer(View view, SoloStudyViewModel soloStudyViewModel) {
        handler.post(my_runnable);
    }

}