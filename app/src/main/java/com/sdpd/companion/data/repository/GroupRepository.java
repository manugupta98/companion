package com.sdpd.companion.data.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;

import com.google.firebase.database.DataSnapshot;
import com.sdpd.companion.data.model.Group;
import com.sdpd.companion.data.model.User;
import com.sdpd.companion.data.remote.FirebaseGroupSource;
import com.sdpd.companion.data.remote.FirebaseUserSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

public class GroupRepository {
    private static final String TAG = "GroupRepository";

    FirebaseGroupSource firebaseGroupSource;
    UserRepository userRepository;

    @Inject
    public GroupRepository(FirebaseGroupSource firebaseGroupSource, UserRepository userRepository) {
        this.firebaseGroupSource = firebaseGroupSource;
        this.userRepository = userRepository;
//        firebaseGroupSource.createGroup("SDPD", "Group for SDPD", "CS F314");
//        firebaseGroupSource.createGroup("ML", "Group for ML", null);
//        firebaseGroupSource.createGroup("NLP", "Group for NLP", null);
    }

    private Group getGroupFromSnapshot(DataSnapshot snapshot) {
        Map<String, Object> values = (Map<String, Object>) snapshot.getValue();
        return new Group(snapshot.getKey(),
                (!values.get("imageUri").toString().equals("false")) ? (String) values.get("imageUri") : null,
                (String) values.get("name"),
                (String) values.get("description"),
                (!values.get("classCode").toString().equals("false")) ? (String) values.get("classCode") : null,
                (!values.get("lastMessage").toString().equals("false")) ? (String) values.get("lastMessage") : null,
                (!values.get("lastMessageSenderName").toString().equals("false")) ? (String) values.get("lastMessageSenderName") : null,
                (!values.get("lastMessageTime").toString().equals("false")) ? snapshot.child("lastMessageTime").getValue(Long.class) : null
        );
    }

    public Flowable<ArrayList<Group>> getUserGroups() {
        final ArrayList<String> groupIds = new ArrayList<>();
        firebaseGroupSource.getUserGroupIds().subscribe(snapshot -> {
            groupIds.clear();
            for (DataSnapshot child : snapshot.getChildren()) {
                groupIds.add((String) child.getKey());
            }
        }, error -> {
            groupIds.clear();
        });
        return firebaseGroupSource.getGroups().map(snapshot -> {

            ArrayList<Group> groups = new ArrayList<>();
            for (DataSnapshot child : snapshot.getChildren()) {
                Group tempGroup = getGroupFromSnapshot(child);
                if (groupIds.contains(tempGroup.getId())) {
                    groups.add(tempGroup);
                }
            }
            return groups;
        });
    }

    public Flowable<ArrayList<Group>> getFilteredGroups(String groupName, String classCode, Boolean filterUserGroups) {
        final ArrayList<String> groupIds = new ArrayList<>();
        if (!filterUserGroups) {
            firebaseGroupSource.getUserGroupIds().subscribe(snapshot -> {
                groupIds.clear();
                for (DataSnapshot child : snapshot.getChildren()) {
                    groupIds.add((String) child.getKey());
                }
            }, error -> {
                groupIds.clear();
            });
        }
        return firebaseGroupSource.getGroups().map(snapshot -> {
            Log.d(TAG, groupName + " " + classCode + " " + filterUserGroups.toString());
            ArrayList<Group> groups = new ArrayList<>();
            for (DataSnapshot child : snapshot.getChildren()) {
                Group tempGroup = getGroupFromSnapshot(child);
                if (!groupIds.contains(tempGroup.getId())
                        && checkFilter(tempGroup, groupName, classCode)
                        && (groupName == null || tempGroup.getName().toLowerCase().contains(groupName.toLowerCase()))
                        && (classCode == null || tempGroup.getClassCode().toLowerCase().contains(classCode.toLowerCase()))) {
                    groups.add(tempGroup);
                }
            }
            Log.d(TAG, groups.toString());
            return groups;
        });
    }

    public Flowable<ArrayList<String>> getMembers(String groupId){
        return firebaseGroupSource.getMembers(groupId).map(snapshot -> {
            ArrayList<String> memberIds = new ArrayList<>();
            for (DataSnapshot child: snapshot.getChildren()){
                memberIds.add(child.getKey());
            }
            return memberIds;
        });
    }

    private boolean checkFilter(Group tempGroup, String groupName, String classCode) {
        if (groupName != null && !tempGroup.getName().toLowerCase().contains(groupName.toLowerCase()))
            return false;

        if (tempGroup.getClassCode() == null && classCode != null)
            return false;

        if (classCode != null && !tempGroup.getClassCode().toLowerCase().contains(classCode.toLowerCase()))
            return false;

        return true;

    }

    public void createGroup(String name, String classCode, String description) {
        firebaseGroupSource.createGroup(name, classCode, description);
    }

    public Single<Group> getGroupById(String groupId) {
        return firebaseGroupSource.getGroupById(groupId).map(snapshot -> {
            return getGroupFromSnapshot(snapshot);
        });
    }

    public Completable joinGroup(String groupId) {
        return firebaseGroupSource.joinGroup(groupId);
    }
}
