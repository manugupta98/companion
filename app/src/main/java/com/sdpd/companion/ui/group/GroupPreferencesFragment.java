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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.sdpd.companion.R;

public class GroupPreferencesFragment extends Fragment {

    public EditText queryEditText;
    public Button submitButton;
    public Toolbar m;
    String searchBy;
    String query;
    public GroupPreferencesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_group_preferences, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        m=(Toolbar)getView().findViewById( R.id.group_chat_bar_layout);
        ((AppCompatActivity)getActivity()).setSupportActionBar(m);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Give Preferences");
        queryEditText = (EditText) getView().findViewById(R.id.searchEditText);
        submitButton = (Button) getView().findViewById(R.id.searchButton);
        Spinner spinner = (Spinner) getView().findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.options_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                searchBy = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GroupListFragment groupListFragment = new GroupListFragment();
                Bundle args = new Bundle();
                args.putString("category","study");
                args.putString("query", queryEditText.getText().toString());
                args.putString("searchBy", searchBy);

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