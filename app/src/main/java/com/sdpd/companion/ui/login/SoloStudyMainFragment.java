package com.sdpd.companion.ui.login;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.radiobutton.MaterialRadioButton;
import com.google.android.material.textfield.TextInputEditText;
import com.sdpd.companion.R;
import com.sdpd.companion.viewModel.SoloStudyViewModel;


public class SoloStudyMainFragment extends Fragment {

    public SoloStudyMainFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_solo_study_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MaterialButton button;
        TextInputEditText editText;
        RadioGroup radioGroup;
        SoloStudyViewModel soloStudyViewModel=new ViewModelProvider(requireActivity()).get(SoloStudyViewModel.class);

        button=view.findViewById(R.id.startSessionButton);
        editText=view.findViewById(R.id.studyTopic);
        radioGroup=view.findViewById(R.id.radioGroup);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int selectedRadioButtonId=radioGroup.getCheckedRadioButtonId();
                soloStudyViewModel.setSearchedText(editText.getText().toString());
                if(selectedRadioButtonId!=-1){
                    MaterialRadioButton radioButton=view.findViewById(selectedRadioButtonId);
                    String selectedOption=radioButton.getText().toString();
                    FragmentManager fm=getActivity().getSupportFragmentManager();
                    FragmentTransaction ft=fm.beginTransaction();
                    if(selectedOption.equals("Stopwatch")){
                        ft.replace(R.id.fragment_container_view, SoloStudyStopwatchFragment.class, null)
                                .setReorderingAllowed(true)
                                .addToBackStack("SoloStudyMain")
                                .commit();
                    }
                    else{
                        ft.replace(R.id.fragment_container_view, SoloStudyTimerFragment.class, null)
                                .setReorderingAllowed(true)
                                .addToBackStack("SoloStudyMain")
                                .commit();
                    }

                }else{
                    Toast.makeText(view.getContext(),"Please select a radio button",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}