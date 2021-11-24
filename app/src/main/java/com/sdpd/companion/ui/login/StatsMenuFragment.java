package com.sdpd.companion.ui.login;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.sdpd.companion.R;

public class StatsMenuFragment extends Fragment implements View.OnClickListener {

    private Button studyStats, otherStats;

    public StatsMenuFragment() {
        // Required empty public constructor
        super(R.layout.fragment_stats_menu);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_stats_menu, container, false);
        studyStats = view.findViewById(R.id.studyStats);
        studyStats.setOnClickListener(this);
        otherStats = view.findViewById(R.id.otherStats);
        otherStats.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        final FragmentTransaction transaction = getFragmentManager().beginTransaction().setReorderingAllowed(true);
        if(view.getId() == R.id.studyStats) {
            transaction.replace(R.id.fragment_container_view, StudyStatsFragment.class, null);
            transaction.addToBackStack(null);
            transaction.commit();
        } else if (view.getId() == R.id.otherStats) {
            transaction.replace(R.id.fragment_container_view, ReportGroupFragment.class, null);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }
}