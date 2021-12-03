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
public class GroupStudyFilterDialog extends DialogFragment {

    private static final String TAG = "GroupStudyFilterDialog";

    GroupStudyViewModel groupStudyViewModel;

    View view;

    TextView groupNameTextView;
    TextView classCodeTextView;
    MaterialCheckBox includeUserGroupsCheckbox;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        groupNameTextView = view.findViewById(R.id.group_study_filter_name_input);
        classCodeTextView = view.findViewById(R.id.group_study_filter_class_code_input);
        includeUserGroupsCheckbox = view.findViewById(R.id.group_study_filter_include_user_checkbox);

        groupNameTextView.setText(groupStudyViewModel.getGroupNameFilter().getValue());
        classCodeTextView.setText(groupStudyViewModel.getClassCodeFilter().getValue());
        includeUserGroupsCheckbox.setChecked(groupStudyViewModel.getUserGroupsFilter().getValue());
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
        view = getLayoutInflater().inflate(R.layout.dialog_group_study_filter, null);
        return new AlertDialog.Builder(getContext(),R.style.neu_dialog)
                .setTitle("Filter")
                .setIcon(R.drawable.ic_baseline_filter_alt_24)
                .setView(view)
                .setPositiveButton(R.string.alert_dialog_ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d(TAG, "heell");
                                Log.d(TAG, groupNameTextView.getText().toString());
                                Log.d(TAG, classCodeTextView.getText().toString());
                                Log.d(TAG, String.valueOf(includeUserGroupsCheckbox.isChecked()));
                                groupStudyViewModel.setFilter(groupNameTextView.getText().toString(),
                                        classCodeTextView.getText().toString(),
                                        includeUserGroupsCheckbox.isChecked()
                                );
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
