package com.sdpd.companion.viewmodels;

import android.util.Log;
import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.sdpd.companion.data.model.Group;
import com.sdpd.companion.data.model.User;
import com.sdpd.companion.data.repository.GroupRepository;
import com.sdpd.companion.data.repository.UserRepository;

import java.util.ArrayList;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@HiltViewModel
public class GroupInfoViewModel extends ViewModel {

    private static final String TAG = "GroupStudyViewModel";

    GroupRepository groupRepository;
    UserRepository userRepository;


    public MutableLiveData<Group> getGroup() {
        return group;
    }

    public MutableLiveData<ArrayList<User>> getMembers() {
        return members;
    }

    private MutableLiveData<Group> group = new MutableLiveData<>();
    private MutableLiveData<ArrayList<User>> members = new MutableLiveData<>();
    private String groupId;

    @Inject
    public GroupInfoViewModel(SavedStateHandle handle, GroupRepository groupRepository, UserRepository userRepository) {
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
        fetchGroupInfo();
        observeMembers();
    }

    private void fetchGroupInfo() {
        groupRepository.getGroupById(groupId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(group -> {
                    this.group.setValue(group);
                }, error -> {

                });
    }

    private void observeMembers() {
        groupRepository.getMembers(groupId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(newMembersList -> {
                    members.setValue(newMembersList);
                }, error -> {
                    members.setValue(new ArrayList<>());
                });
    }


}
