package com.sdpd.companion.ui.login;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.sdpd.companion.R;
import com.sdpd.companion.viewModel.SoloStudyViewModel;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class SoloStudyTimerFragment extends Fragment {

    EditText hours,minutes,seconds;
    SoloStudyViewModel soloStudyViewModel;
    MaterialButton endSession;

    TextView progressBarText;

    MaterialButton startTimerButton,playMusic;
    CircularProgressIndicator progress;

    public SoloStudyTimerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(getActivity(),"Ending current session",Toast.LENGTH_LONG).show();
        soloStudyViewModel.setTimerRunning(false);
        soloStudyViewModel.setTimerSeconds(0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_solo_study_timer, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        soloStudyViewModel=new ViewModelProvider(requireActivity()).get(SoloStudyViewModel.class);
        getActivity().setTitle(soloStudyViewModel.getSearchedText());

        hours=view.findViewById(R.id.hours);
        minutes=view.findViewById(R.id.minutes);
        seconds=view.findViewById(R.id.seconds);
        startTimerButton=view.findViewById(R.id.start);
        progress=view.findViewById(R.id.progressBar);
        progressBarText=view.findViewById(R.id.progressInidcatorText);

        hours.setEnabled(true);
        minutes.setEnabled(true);
        seconds.setEnabled(true);

        startTimerButton.setClickable(true);

        startTimerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // int h=Integer.parseInt(hours.toString());

                int milliSeconds=0;
                if(soloStudyViewModel.isTimerRunning()){
                    milliSeconds= soloStudyViewModel.getTimerSeconds();
                }
                else{
                    int hrs= hours.getText().toString().equals("") ?0:Integer.parseInt(hours.getText().toString());
                    int mins= minutes.getText().toString().equals("") ?0:Integer.parseInt(minutes.getText().toString());
                    int secs= seconds.getText().toString().equals("") ?0:Integer.parseInt(seconds.getText().toString());
                    milliSeconds=(hrs*3600000+mins*60000+secs*1000);
                    soloStudyViewModel.setTimerSeconds(milliSeconds);
                    soloStudyViewModel.setTimerRunning(true);
                }

                if(milliSeconds==0){
                    Toast.makeText(getActivity().getApplicationContext(),"Please enter  time",Toast.LENGTH_SHORT).show();
                    soloStudyViewModel.setTimerRunning(false);
                }
                else{
                    final int ms=milliSeconds;
                    startTimerButton.setEnabled(false);
                    hours.setEnabled(false);
                    minutes.setEnabled(false);
                    seconds.setEnabled(false);
                    new CountDownTimer(ms,1000){
                        public void onTick(long millisUntilFinished) {
                            // Used for formatting digit to be in 2 digits only
                            NumberFormat f = new DecimalFormat("00");
                            long hour = (millisUntilFinished / 3600000) % 24;
                            long min = (millisUntilFinished / 60000) % 60;
                            long sec = (millisUntilFinished / 1000) % 60;
                            hours.setText(f.format(hour));
                            minutes.setText(f.format(min));
                            seconds.setText(f.format(sec));

                            soloStudyViewModel.setTimerUserHours((int)hour);
                            soloStudyViewModel.setTimerUserMinutes((int)min);
                            soloStudyViewModel.setTimerUserSeconds((int)sec);

                            progress.setProgress((int)((double)millisUntilFinished/ms*100));
                            progressBarText.setText((int)((double)millisUntilFinished/ms*100)+"%");
                        }
                        // When the task is over it will print 00:00:00 there
                        public void onFinish() {
                            progress.setProgress(0);
                            progressBarText.setText("Session Completed");
                            soloStudyViewModel.addAppSpentTime(ms/1000);

                            startTimerButton.setClickable(false);
                        }
                    }.start();
                }

            }
        });

        playMusic=view.findViewById(R.id.playMusic);
        endSession=view.findViewById(R.id.endSession);

        endSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soloStudyViewModel.setSearchedText("");
                soloStudyViewModel.setTimerSeconds(0);
                soloStudyViewModel.setTimerRunning(false);

                FragmentManager fm=getActivity().getSupportFragmentManager();
                FragmentTransaction ft=fm.beginTransaction();
                fm.popBackStack();
                fm.popBackStack();
                ft.replace(R.id.fragment_container_view, SoloStudyMainFragment.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack(null)
                        .commit();
            }
        });

        playMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("spotify:album:"));
                    intent.putExtra(Intent.EXTRA_REFERRER, Uri.parse("android-app://" + getContext().getPackageName()));
                    startActivity(intent);
                }catch(Exception e){
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
}
