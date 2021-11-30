package com.sdpd.companion.ui.groupstudy;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.checkbox.MaterialCheckBox;
import com.sdpd.companion.R;
import com.sdpd.companion.viewmodels.GroupStudyViewModel;

import dagger.hilt.android.AndroidEntryPoint;


@AndroidEntryPoint
public class NewGroupDialog extends DialogFragment {

    private static final String TAG = "NewGroupDialog";

    GroupStudyViewModel groupStudyViewModel;

    View view;

    TextView groupNameTextView;
    TextView classCodeTextView;
    TextView descriptionTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        groupNameTextView = view.findViewById(R.id.group_study_name_input);
        classCodeTextView = view.findViewById(R.id.group_study_class_code_input);
        descriptionTextView = view.findViewById(R.id.group_study_description_input);
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
        view = getLayoutInflater().inflate(R.layout.dialog_new_group, null);
        return new AlertDialog.Builder(getContext())
                .setTitle("New Group")
                .setIcon(R.drawable.ic_baseline_add_24)
                .setView(view)
                .setPositiveButton(R.string.alert_dialog_ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String name = groupNameTextView.getText().toString();
                                String classCode = groupNameTextView.getText().toString();
                                String description = descriptionTextView.getText().toString();
                                groupStudyViewModel.createGroup(name, classCode, description);
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
