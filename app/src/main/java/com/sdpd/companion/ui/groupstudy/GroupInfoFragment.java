package com.sdpd.companion.ui.groupstudy;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sdpd.companion.R;
import com.sdpd.companion.viewmodels.GroupInfoViewModel;
import com.sdpd.companion.viewmodels.GroupStudyViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class GroupInfoFragment extends Fragment {
    private static final String TAG = "GroupInfoFragment";

    GroupInfoViewModel groupInfoViewModel;

//    GroupStudyRecyclerViewAdapter adapter;
//    RecyclerView recyclerView;


    public GroupInfoFragment() {
        super(R.layout.fragment_group_info);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        groupInfoViewModel = new ViewModelProvider(this).get(GroupInfoViewModel.class);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_group_info, container, false);
//        recyclerView = view.findViewById(R.id.groups_recycler_view);
//        adapter = new GroupStudyRecyclerViewAdapter(getContext());
//        recyclerView.setAdapter(adapter);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        setHasOptionsMenu(true);
//        groupInfoViewModel.setGroupId(groupId);
        observeGroup();
        observeMembers();
        return view;
    }

    private void observeMembers() {
//        groupInfoViewModel.getMembers().observeForever(newMemeberList);
    }

    private void observeGroup() {
        groupInfoViewModel.getGroup().observeForever(group -> {
            if (group != null){

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
    }

//    private void initRecyclerView() {
//        recyclerView = getView().findViewById(R.id.groups_recycler_view);
//        GroupStudyRecyclerViewAdapter adapter = new GroupStudyRecyclerViewAdapter(getContext());
//        recyclerView.setAdapter(adapter);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//    }
}
