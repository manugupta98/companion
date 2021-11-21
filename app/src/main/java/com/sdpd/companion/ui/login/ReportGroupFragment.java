package com.sdpd.companion.ui.login;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.sdpd.companion.R;
import com.sdpd.companion.viewModel.ReportGroupViewModel;

public class ReportGroupFragment extends Fragment implements View.OnClickListener {

    private Button reportButton;
    private ReportGroupViewModel mReportGroupFragment;
    private View view;
    private String reportGroupId;
    private String reportGroupDescription;

    public ReportGroupFragment() {
        super(R.layout.fragment_report_group);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mReportGroupFragment = new ViewModelProvider(this).get(ReportGroupViewModel.class);
        mReportGroupFragment.init(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_report_group, container, false);
        reportButton = view.findViewById(R.id.reportButton);
        reportButton.setOnClickListener(this);

        this.view = view;
        return view;
    }

    @Override
    public void onClick(View view) {
        TextInputLayout reportId = this.view.findViewById(R.id.reportIdLayout);
        reportGroupId = String.valueOf(reportId.getEditText().getText());

        TextInputLayout reportDescription = this.view.findViewById(R.id.reportDescriptionLayout);
        reportGroupDescription = String.valueOf(reportDescription.getEditText().getText());

        mReportGroupFragment.report(reportGroupId, reportGroupDescription);
        Toast.makeText(getActivity(), "Report submitted!", Toast.LENGTH_SHORT).show();
        FragmentTransaction transaction = getFragmentManager().beginTransaction().setReorderingAllowed(true);
        transaction.replace(R.id.fragment_container_view, ReportGroupFragment.class, null);
        transaction.commit();
    }
}