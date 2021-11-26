package com.sdpd.companion.ui.login;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.sdpd.companion.R;

public class MenuFragment extends Fragment implements View.OnClickListener {

    private View view;
    private Button stats, soloStudy, report;
    public MenuFragment() {
        // Required empty public constructor
        super(R.layout.fragment_menu);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        this.view = view;
        stats = view.findViewById(R.id.stats);
        stats.setOnClickListener(this);
        soloStudy = view.findViewById(R.id.soloStudy);
        soloStudy.setOnClickListener(this);
        report = view.findViewById(R.id.reportGroup);
        report.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        final FragmentTransaction transaction = getFragmentManager().beginTransaction().setReorderingAllowed(true);
        transaction.addToBackStack(null);
        transaction.setReorderingAllowed(true);
        if(R.id.stats == view.getId()) {
            transaction.replace(R.id.fragment_container_view, StatsFragment.class, null);
            transaction.commit();
        } else if(R.id.soloStudy == view.getId()) {
            transaction.replace(R.id.fragment_container_view, SoloStudyMainFragment.class, null);
            transaction.commit();
        } else if(R.id.reportGroup == view.getId()) {
            transaction.replace(R.id.fragment_container_view, ReportGroupFragment.class, null);
            transaction.commit();
        }
    }
}