package com.sdpd.companion.ui.solostudy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.sdpd.companion.R;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SoloStudyFragment extends Fragment {
    private static final String TAG = "SoloStudyFragment";

    public SoloStudyFragment(){
        super(R.layout.fragment_home);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        groupViewModel = new ViewModelProvider(this).get(GroupViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }
}
