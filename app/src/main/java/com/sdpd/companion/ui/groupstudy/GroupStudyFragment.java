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
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.sdpd.companion.R;
import com.sdpd.companion.viewmodels.GroupStudyViewModel;
import com.sdpd.companion.viewmodels.UserGroupViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class GroupStudyFragment extends Fragment {

    private static final String TAG = "GroupStudyFragment";

    GroupStudyViewModel GroupStudyViewModel;

    GroupStudyRecyclerViewAdapter adapter;
    RecyclerView recyclerView;

    public GroupStudyFragment() {
        super(R.layout.fragment_group_study);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GroupStudyViewModel = new ViewModelProvider(getActivity()).get(GroupStudyViewModel.class);
    }

    private void observeGroups() {
        GroupStudyViewModel.getFilteredGroups().observeForever(newGroupList -> {
            Log.d(TAG, newGroupList.toString());
            adapter.setGroups(newGroupList);
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.group_study_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.groupStudyFilterDialog) {
            new GroupStudyFilterDialog().show(getParentFragmentManager(), "dialog");
            return true;
        } else if (item.getItemId() == R.id.newGroupDialog) {
            new NewGroupDialog().show(getParentFragmentManager(), "dialog");
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_group_study, container, false);
        recyclerView = view.findViewById(R.id.groups_recycler_view);
        adapter = new GroupStudyRecyclerViewAdapter(getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        setHasOptionsMenu(true);
        observeGroups();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void initRecyclerView() {
        recyclerView = getView().findViewById(R.id.groups_recycler_view);
        GroupStudyRecyclerViewAdapter adapter = new GroupStudyRecyclerViewAdapter(getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }


}
