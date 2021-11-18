package com.sdpd.companion.ui.group;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.sdpd.companion.R;

public class GroupPreferencesFragment extends Fragment {

    public EditText searchBy;
    public Button submitButton;
    public GroupPreferencesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_group_preferences, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        searchBy = (EditText) getView().findViewById(R.id.searchEditText);
        submitButton = (Button) getView().findViewById(R.id.searchButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GroupListFragment groupListFragment = new GroupListFragment();
                Bundle args = new Bundle();
                args.putString("searchBy", searchBy.getText().toString());
                groupListFragment.setArguments(args);
                // Begin the transaction
                FragmentTransaction ft = getParentFragmentManager().beginTransaction();
                // Replace the contents of the container with the new fragment
                ft.replace(R.id.your_placeholder, groupListFragment);
                // or ft.add(R.id.your_placeholder, new FooFragment());
                // Complete the changes added above
                ft.addToBackStack(null).commit();
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }
}