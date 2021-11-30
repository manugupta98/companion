package com.sdpd.companion.viewmodels;


import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.sdpd.companion.data.model.Group;
import com.sdpd.companion.data.repository.GroupRepository;
import com.sdpd.companion.data.repository.UserRepository;

import java.util.ArrayList;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

@HiltViewModel
public class UserGroupViewModel extends ViewModel {
    private static final String TAG = "UserGroupViewModel";

    GroupRepository groupRepository;
    UserRepository userRepository;

    private MutableLiveData<ArrayList<Group>> userGroups = new MutableLiveData<>();

    @Inject
    public UserGroupViewModel(SavedStateHandle handle, GroupRepository groupRepository, UserRepository userRepository) {
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
        observeUserGroups();
    }

    private void observeUserGroups() {
        groupRepository.getUserGroups()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(groups -> {
                    userGroups.setValue(groups);
                }, error -> {
                    Log.d(TAG, error.getMessage());
                    userGroups.setValue(new ArrayList<Group>());
                });
//        Log.d(TAG, userGroups.getValue().get(0).getName());
    }

    public LiveData<ArrayList<Group>> getUserGroups(){
        return userGroups;
    }

}
