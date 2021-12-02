package com.sdpd.companion.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sdpd.companion.R;
import com.sdpd.companion.viewmodels.UserGroupViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";

    UserGroupViewModel userGroupViewModel;

    GroupRecyclerViewAdapter adapter;
    RecyclerView recyclerView;

    public HomeFragment(){
        super(R.layout.fragment_home);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userGroupViewModel = new ViewModelProvider(getActivity()).get(UserGroupViewModel.class);
    }

    private void observeGroups() {
        userGroupViewModel.getUserGroups().observeForever(newGroupList -> {
            Log.d(TAG, newGroupList.toString());
            adapter.setGroups(newGroupList);
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = view.findViewById(R.id.groups_recycler_view);
        adapter = new GroupRecyclerViewAdapter(getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        userGroupViewModel.observeUserGroupIds();
        observeGroups();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void initRecyclerView(){
        recyclerView = getView().findViewById(R.id.groups_recycler_view);
        GroupRecyclerViewAdapter adapter = new GroupRecyclerViewAdapter(getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

}
