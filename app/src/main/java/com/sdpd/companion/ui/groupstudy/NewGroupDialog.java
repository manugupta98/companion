package com.sdpd.companion.ui.groupstudy;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.sdpd.companion.R;
import com.sdpd.companion.viewmodels.GroupStudyViewModel;

import java.util.ArrayList;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;


@AndroidEntryPoint
public class NewGroupDialog extends DialogFragment {

    private static final String TAG = "NewGroupDialog";

    GroupStudyViewModel groupStudyViewModel;

    View view;

    TextView groupNameTextView;
    TextView classCodeTextView;
    TextView descriptionTextView;
    ImageView iconImageView;

    Uri image;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        groupNameTextView = view.findViewById(R.id.group_study_name_input);
        classCodeTextView = view.findViewById(R.id.group_study_class_code_input);
        descriptionTextView = view.findViewById(R.id.group_study_description_input);
        iconImageView = view.findViewById(R.id.icon_image_view);

        iconImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImage();
            }
        });

        Glide.with(getContext())
                .load(image)
                .placeholder(R.drawable.default_group_icon5)
                .circleCrop()
                .into(iconImageView);

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
        AlertDialog alertDialog = new AlertDialog.Builder(getContext(), R.style.neu_dialog)
                .setTitle("New Group")
                .setIcon(R.drawable.ic_baseline_add_24)
                .setView(view)
                .setPositiveButton(R.string.alert_dialog_ok,
                        null)
                .setNegativeButton(R.string.alert_dialog_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button confirmButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                confirmButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String name = groupNameTextView.getText().toString();
                        String classCode = classCodeTextView.getText().toString();
                        String description = descriptionTextView.getText().toString();
                        if (name.equals("") || description.equals("")) {
                            return;
                        }
                        String mimeType = null;
                        if (image != null) {
                            mimeType = getContext().getContentResolver().getType(image);
                        }

                        groupStudyViewModel.createGroup(image, mimeType, name, classCode, description)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(group -> {
                                    dialog.dismiss();
                                    NavController navController = Navigation.findNavController(getParentFragment().getView());
                                    NavDirections action = GroupStudyFragmentDirections.actionGroupStudyFragmentToChatFragment(group.getId(), group.getName());
                                    navController.navigate(action);
                                }, error -> {
                                    Log.d(TAG, error.getMessage());
                                });
                    }
                });
            }
        });
        return alertDialog;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                image = data.getData();
                Glide.with(getContext())
                        .load(image)
                        .placeholder(R.drawable.default_group_icon5)
                        .circleCrop()
                        .into(iconImageView);
            }
        }
    }

    public void getImage() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, 1);
    }
}
