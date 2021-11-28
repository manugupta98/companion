package com.sdpd.companion.ui.groupstudy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sdpd.companion.R;
import com.sdpd.companion.ui.home.GroupRecyclerViewAdapter;
import com.sdpd.companion.viewmodels.GroupViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class GroupStudyFragment extends Fragment {
    private static final String TAG = "GroupStudyFragment";

    public GroupStudyFragment(){
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
