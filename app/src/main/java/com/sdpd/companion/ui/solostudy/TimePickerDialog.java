package com.sdpd.companion.ui.solostudy;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.sdpd.companion.R;
import com.sdpd.companion.viewmodels.GroupStudyViewModel;
import com.sdpd.companion.viewmodels.TimerViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class TimePickerDialog extends DialogFragment {

    private static final String TAG = "TimePickerDialog";

    TimerViewModel timerViewModel;

    View view;

    TextView timeTextView;

    TextView hourTextView;
    TextView minuteTextView;
    TextView secondsTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        hourTextView = view.findViewById(R.id.hours);
        minuteTextView = view.findViewById(R.id.minute);
        secondsTextView = view.findViewById(R.id.seconds);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        timerViewModel = new ViewModelProvider(getActivity()).get(TimerViewModel.class);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        view = getLayoutInflater().inflate(R.layout.dialog_time_picker, null);
        return new AlertDialog.Builder(getContext(), R.style.neu_dialog)
                .setTitle("Time")
//                .setIcon(R.drawable.ic_baseline_add_24)
                .setView(view)
                .setPositiveButton(R.string.alert_dialog_ok, null)
                .setNegativeButton(R.string.alert_dialog_cancel,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                return;
                            }
                        })
                .create();

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button confirmButton =  alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                confirmButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Integer hour = Integer.parseInt(hourTextView.getText().toString());
                        Integer minute = Integer.parseInt(minuteTextView.getText().toString());
                        Integer second = Integer.parseInt(secondsTextView.getText().toString());
                        Long time = (hour * 60 * 60 + minute * 60 + second) * 1000L;
                        Log.d(TAG, hour + ":" + minute + ":" + second);
                        if (time <= 0 || minute >= 60 || second >= 60 || hour < 0 || minute < 0 || second < 0) {
                            Toast.makeText(getContext(), "Invalid Value", Toast.LENGTH_SHORT);
                            return;
                        }
                        timerViewModel.setTimer(time);

                        dialog.dismiss();
                    }
                });
            }
        });
        return alertDialog;
    }
}
