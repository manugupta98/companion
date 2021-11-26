package com.sdpd.companion.ui.group;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.sdpd.companion.R;


public class GroupsFragment extends Fragment {

    Button gym;
    Button sports;
    Button outings;
    private Toolbar m;
    public GroupsFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_groups, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        gym = (Button)getView().findViewById(R.id.gym);
        sports = (Button)getView().findViewById(R.id.sports);
        outings = (Button)getView().findViewById(R.id.outings);
        m=(Toolbar)getView().findViewById( R.id.group_chat_bar_layout);
        ((AppCompatActivity)getActivity()).setSupportActionBar(m);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Groups");
        gym.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGroupListFragment("gym");
            }
        });

        sports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGroupListFragment("sports");
            }
        });

        outings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGroupListFragment("outings");
            }
        });
    }

    public void openGroupListFragment(String query){
        GroupListFragment groupListFragment = new GroupListFragment();
        Bundle args = new Bundle();
        args.putString("category","not study");
        args.putString("query", query);
        args.putString("searchBy", "Topic");

        groupListFragment.setArguments(args);
        // Begin the transaction
        FragmentTransaction ft = getParentFragmentManager().beginTransaction();
        // Replace the contents of the container with the new fragment
        ft.replace(R.id.your_placeholder, groupListFragment);
        // or ft.add(R.id.your_placeholder, new FooFragment());
        // Complete the changes added above
        ft.addToBackStack(null).commit();
    }
}