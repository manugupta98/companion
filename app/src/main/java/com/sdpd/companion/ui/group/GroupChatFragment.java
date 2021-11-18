package com.sdpd.companion.ui.group;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sdpd.companion.R;

public class GroupChatFragment extends Fragment {

    public GroupChatFragment() {
        // Required empty public constructor
    }
    String groupName;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        groupName = getArguments().getString("chosenGroup");
        return inflater.inflate(R.layout.fragment_group_chat, container, false);
    }
}