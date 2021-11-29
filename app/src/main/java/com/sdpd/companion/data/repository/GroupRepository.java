package com.sdpd.companion.data.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;

import com.google.firebase.database.DataSnapshot;
import com.sdpd.companion.data.model.Group;
import com.sdpd.companion.data.remote.FirebaseGroupSource;
import com.sdpd.companion.data.remote.FirebaseUserSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Flowable;

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
                (values.get("imageUri").toString() != "false") ? (String) values.get("imageUri") : null,
                (String) values.get("name"),
                (String) values.get("description"),
                (values.get("classCode").toString() != "false") ? (String) values.get("classCode") : null,
                (values.get("lastMessage").toString() != "false") ? (String) values.get("lastMessage") : null,
                (values.get("lastMessageSenderName").toString() != "false") ? (String) values.get("lastMessageSenderName") : null
        );
    }

    public Flowable<ArrayList<Group>> getUserGroups() {
        final ArrayList<String> groupIds = new ArrayList<>();
        firebaseGroupSource.getUserGroupIds().subscribe(snapshot -> {
            Log.d(TAG, "groupIds update");
            groupIds.clear();
            for (DataSnapshot child : snapshot.getChildren()) {
                groupIds.add((String) child.getKey());
            }
        }, error -> {
                    groupIds.clear();});
        return firebaseGroupSource.getUserGroups().map(snapshot -> {

            ArrayList<Group> groups = new ArrayList<>();
            for (DataSnapshot child : snapshot.getChildren()) {
                Group tempGroup = getGroupFromSnapshot(child);
                Log.d(TAG, groupIds.toString());
                Log.d(TAG, tempGroup.getId());
                if (groupIds.contains(tempGroup.getId())) {
                    Log.d(TAG, "true");
                    groups.add(getGroupFromSnapshot(child));
                }
            }
            return groups;
        });
    }

//    public Flowable<ArrayList<String>> getUserGroups(ArrayList<String> groupIds) {
//        return firebaseGroupSource.getUserGroups(groupIds).map(snapshot -> {
//            Log.d(TAG, snapshot.toString());
//            ArrayList<Group> groups = new ArrayList<>();
//            for (DataSnapshot child : snapshot.getChildren()) {
//                Map<String, Object> value = (Map<String, Object>) child.getValue();
//                groups.add(getGroupFromSnapshot(child));
//            }
//            return groups;
//        });
//    }
}
