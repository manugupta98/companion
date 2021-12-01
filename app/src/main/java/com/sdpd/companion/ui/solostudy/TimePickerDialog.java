package com.sdpd.companion.ui.solostudy;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.sdpd.companion.R;
import com.sdpd.companion.viewmodels.GroupStudyViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class TimePickerDialog extends DialogFragment {

    private static final String TAG = "TimePickerDialog";

    GroupStudyViewModel groupStudyViewModel;

    View view;

    TextView timeTextView;

    TextView hourTextView;
    TextView minuteTextView;
    TextView secondsTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        timeTextView = view.findViewById(R.id);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        groupStudyViewModel = new ViewModelProvider(getActivity()).get(GroupStudyViewModel.class);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        view = getLayoutInflater().inflate(R.layout.dialog_time_picker, null);
        return new AlertDialog.Builder(getContext())
                .setTitle("Time")
//                .setIcon(R.drawable.ic_baseline_add_24)
                .setView(view)
                .setPositiveButton(R.string.alert_dialog_ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                .setNegativeButton(R.string.alert_dialog_cancel,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                return;
                            }
                        })
                .create();
    }
}
