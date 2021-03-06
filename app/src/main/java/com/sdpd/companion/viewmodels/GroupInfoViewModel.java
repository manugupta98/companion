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
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@HiltViewModel
public class GroupInfoViewModel extends ViewModel {

    private static final String TAG = "GroupInfoViewModel";

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
    private MutableLiveData<ArrayList<String>> memberIds = new MutableLiveData<>();
    private MutableLiveData<User> user = new MutableLiveData<>();
    private MutableLiveData<Boolean> isMember = new MutableLiveData<>();
    private String groupId;

    @Inject
    public GroupInfoViewModel(SavedStateHandle handle, GroupRepository groupRepository, UserRepository userRepository) {
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
        observeUser();
    }

    private void observeUser() {
        userRepository.getCurrentUser()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(user -> {
                    this.user.setValue(user);
                }, error -> {

                });
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
        fetchGroupInfo();
        observeMembers();
    }

    public Completable joinGroup(){
        return groupRepository.joinGroup(groupId);
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


    public MutableLiveData<Boolean> getIsMember() {
        return isMember;
    }

    private void observeMembers() {
        groupRepository.getMembers(groupId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(newMemberIds -> {
                    Log.d(TAG, "1");
                    memberIds.setValue(newMemberIds);
                    if (newMemberIds.contains(user.getValue().getUid())){
                        isMember.setValue(true);
                    } else {
                        isMember.setValue(false);
                    }
                    fetchMemberInfo();
                }, error -> {
                    Log.d(TAG, error.getMessage());
                    memberIds.setValue(new ArrayList<>());
                    fetchMemberInfo();
                });
    }

    private void fetchMemberInfo() {
        userRepository.getUsers(memberIds.getValue())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(newMemberList -> {
                    Log.d(TAG, "2");
                    members.setValue(newMemberList);
                }, error -> {
                    Log.d(TAG, error.getMessage());
                    members.setValue(new ArrayList<>());
                });
    }


    public void leaveGroup() {
        groupRepository.leaveGroup(groupId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    Log.d(TAG, "grp Joined");
                }, error -> {

                });
    }
}
