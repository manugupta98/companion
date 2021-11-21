package com.sdpd.companion.ui.group;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sdpd.companion.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class GroupListFragment extends Fragment {

    private FloatingActionButton fab;
    private View groupfragmentview;
    private ListView list_view;
    private ArrayAdapter<Group> arrayAdapter;
    private ArrayList<Group> Listofgroups = new ArrayList<>();
    private ArrayList<String> ListofgroupNames = new ArrayList<>();
    private DatabaseReference groupref;
    private String searchBy;
    private String query;

    public GroupListFragment() {
        super(R.layout.fragment_group_list);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        groupfragmentview = inflater.inflate(R.layout.fragment_group_list, container, false);
        groupref = FirebaseDatabase.getInstance().getReference().child("Groups");
        searchBy = getArguments().getString("searchBy");
        query = getArguments().getString("query");
        Initializefields();
        retrieveanddisplay();
        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                GroupChatFragment groupChatFragment = new GroupChatFragment();
                Bundle args = new Bundle();
                args.putSerializable("selectedGroup", (Serializable) adapterView.getItemAtPosition(i));
                groupChatFragment.setArguments(args);
                // Begin the transaction
                FragmentTransaction ft = getParentFragmentManager().beginTransaction();
                // Replace the contents of the container with the new fragment
                ft.replace(R.id.your_placeholder, groupChatFragment);
                // or ft.add(R.id.your_placeholder, new FooFragment());
                // Complete the changes added above
                ft.addToBackStack(null).commit();
            }
        });
        return groupfragmentview;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        fab = (FloatingActionButton) getView().findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getNewGroupDetails();
            }
        });
    }

    private void Initializefields() {
        list_view=(ListView)groupfragmentview.findViewById(R.id.List_view);
        arrayAdapter=new ArrayAdapter<Group>(getContext(),android.R.layout.simple_list_item_1,Listofgroups);
        list_view.setAdapter(arrayAdapter);
    }

    private void retrieveanddisplay() {
        groupref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Listofgroups.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Group group = snapshot.getValue(Group.class);
                    group.groupKey = snapshot.getKey();
                    Log.d("key",snapshot.getKey());
                    if(searchBy.equals("Topic") && group.groupTopic.equals(query))
                    {
                        Listofgroups.add(group);
                    }
                    if(searchBy.equals("Group Code") && group.groupCode.equals(query))
                    {
                        Listofgroups.add(group);
                    }
                    if(searchBy.equals("Group Name") && group.groupName.equals(query))
                    {
                        Listofgroups.add(group);
                    }
                   // Log.d("tag",group.groupTopic + " " + searchBy);
                }
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void getNewGroupDetails() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AlertDialog);
        builder.setTitle("Enter Group details:-");
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.group_details_dialog, null);
        builder.setView(dialogView);

        builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText grp = (EditText) dialogView.findViewById(R.id.groupName);
                String groupName = grp.getText().toString();
                String groupDes = ((EditText) dialogView.findViewById(R.id.groupDes)).getText().toString();
                String groupCode = ((EditText) dialogView.findViewById(R.id.groupCode)).getText().toString();
                String groupTopic = ((EditText) dialogView.findViewById(R.id.groupTopic)).getText().toString();

                Group newGroup = new Group(groupName, groupDes, groupCode, groupTopic);

                if (TextUtils.isEmpty(groupName) || TextUtils.isEmpty(groupDes) || TextUtils.isEmpty(groupCode) || TextUtils.isEmpty(groupTopic)) {
                    Toast.makeText(getContext(), "Please write group details", Toast.LENGTH_LONG).show();
                } else {
                    ((TestActivity) getActivity()).createNewGroup(newGroup);
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();

            }
        });
        builder.show();
    }

}