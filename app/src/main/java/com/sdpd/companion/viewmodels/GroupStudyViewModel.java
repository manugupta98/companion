package com.sdpd.companion.viewmodels;

import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.sdpd.companion.data.model.Group;
import com.sdpd.companion.data.repository.GroupRepository;
import com.sdpd.companion.data.repository.UserRepository;

import java.util.ArrayList;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@HiltViewModel
public class GroupStudyViewModel extends ViewModel {
    private static final String TAG = "GroupStudyViewModel";

    GroupRepository groupRepository;
    UserRepository userRepository;

    Disposable filteredGroupsDisposable;

    private MutableLiveData<ArrayList<Group>> filteredGroups = new MutableLiveData<>();
    private MutableLiveData<String> groupNameFilter = new MutableLiveData<>(null);
    private MutableLiveData<String> classCodeFilter = new MutableLiveData<>(null);
    private MutableLiveData<Boolean> userGroupsFilter = new MutableLiveData<>(false);

    public MutableLiveData<String> getGroupNameFilter() {
        return groupNameFilter;
    }

    public MutableLiveData<String> getClassCodeFilter() {
        return classCodeFilter;
    }

    public MutableLiveData<Boolean> getUserGroupsFilter() {
        return userGroupsFilter;
    }


    @Inject
    public GroupStudyViewModel(SavedStateHandle handle, GroupRepository groupRepository, UserRepository userRepository) {
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
        setFilter("", "", false);
    }

    public void setFilter(String groupName, String classCode, Boolean filterUserGroups) {
        if (groupName.equals("")) {
            groupName = null;
        }
        if (classCode.equals("")) {
            classCode = null;
        }
        groupNameFilter.setValue(groupName);
        classCodeFilter.setValue(classCode);
        userGroupsFilter.setValue(filterUserGroups);
        Log.d(TAG, "filter reset");
        if (filteredGroupsDisposable != null && !filteredGroupsDisposable.isDisposed())
            filteredGroupsDisposable.dispose();
        filteredGroupsDisposable = groupRepository.getFilteredGroups(groupName, classCode, filterUserGroups)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(groups -> {
                    Log.d(TAG, "new grps recieved");
                    filteredGroups.setValue(groups);
                }, error -> {
                    Log.d(TAG, error.getMessage());
                    filteredGroups.setValue(new ArrayList<>());
                });
    }

    public LiveData<ArrayList<Group>> getFilteredGroups() {
        return filteredGroups;
    }

    public Single<Group> createGroup(Uri imageUri, String mimeType, String name, String classCode, String description) {
        if (classCode.equals("")) {
            classCode = null;
        }
        return groupRepository.createGroup(imageUri, mimeType, name, classCode, description);
    }
}
