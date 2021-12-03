package com.sdpd.companion.ui.groupstudy;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sdpd.companion.R;
import com.sdpd.companion.data.model.Group;
import com.sdpd.companion.viewmodels.GroupInfoViewModel;
import com.sdpd.companion.viewmodels.GroupStudyViewModel;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

@AndroidEntryPoint
public class GroupInfoFragment extends Fragment {
    private static final String TAG = "GroupInfoFragment";

    GroupInfoViewModel groupInfoViewModel;

    MembersRecyclerViewAdapter adapter;
    RecyclerView recyclerView;

    ImageView groupIconImageView;
    TextView groupNameTextView;
    TextView groupClassCodeTextView;
    TextView groupDescriptionTextView;
    Button joinButton;


    public GroupInfoFragment() {
        super(R.layout.fragment_group_info);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        groupInfoViewModel = new ViewModelProvider(this).get(GroupInfoViewModel.class);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_group_info, container, false);
        recyclerView = view.findViewById(R.id.users_recycler_view);
        adapter = new MembersRecyclerViewAdapter(getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        groupIconImageView = view.findViewById(R.id.group_icon);
        groupNameTextView = view.findViewById(R.id.group_name);
        groupClassCodeTextView = view.findViewById(R.id.group_class_code);
        groupDescriptionTextView = view.findViewById(R.id.group_description);
        joinButton = view.findViewById(R.id.join_button);

        String groupId = GroupInfoFragmentArgs.fromBundle(getArguments()).getGroupId();
        groupInfoViewModel.setGroupId(groupId);

        initButton();

        observeGroup();
        observeMembers();
        return view;
    }

    private void initButton() {
        groupInfoViewModel.getIsMember().observeForever(isMember -> {
            if (!isMember) {
                joinButton.setText("Join");
                joinButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        groupInfoViewModel.joinGroup()
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(() -> {
                                    Group group = groupInfoViewModel.getGroup().getValue();
                                    NavController navController = Navigation.findNavController(getView());
                                    NavDirections action = GroupInfoFragmentDirections.actionGroupInfoFragmentToChatFragment(group.getId(), group.getName());
                                    navController.navigate(action);
                                }, error -> {

                                });;
                    }
                });
            } else {
                joinButton.setText("Leave");
                joinButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        groupInfoViewModel.leaveGroup();
                        NavController navController = Navigation.findNavController(getView());
                        NavDirections action = GroupInfoFragmentDirections.actionGroupInfoFragmentToHomeFragment();
                        navController.navigate(action);
                    }
                });

            }
        });
    }

    private void observeMembers() {
        groupInfoViewModel.getMembers().observeForever(newMemberList -> {
            Log.d(TAG, newMemberList.toString());
            adapter.setMembers(newMemberList);
        });
    }

    private void observeGroup() {
        Log.d(TAG, "group info obseve grp");
        groupInfoViewModel.getGroup().observeForever(group -> {
            Log.d(TAG, "group info");
            if (group != null) {

                Glide.with(getContext())
                        .load(group.getImageUri())
                        .placeholder(R.drawable.default_group_icon5)
                        .circleCrop()
                        .into(groupIconImageView);

                groupNameTextView.setText(group.getName());
                groupClassCodeTextView.setText(group.getClassCode());
                groupDescriptionTextView.setText(group.getDescription());
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
    }

//    private void initRecyclerView() {
//        recyclerView = getView().findViewById(R.id.groups_recycler_view);
//        GroupStudyRecyclerViewAdapter adapter = new GroupStudyRecyclerViewAdapter(getContext());
//        recyclerView.setAdapter(adapter);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//    }
}
