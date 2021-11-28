package com.sdpd.companion.data.remote;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.sdpd.companion.data.model.Group;
import com.sdpd.companion.data.model.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

public class FirebaseGroupSource {
    private static final String TAG = "FirebaseGroupSource";

    public FirebaseDatabase firebaseDatabase;
    private final DatabaseReference mDatabase;
    public FirebaseAuth firebaseAuth;
    public FirebaseUserSource firebaseUserSource;

    @Inject
    public FirebaseGroupSource(FirebaseDatabase firebaseDatabase, FirebaseAuth firebaseAuth, FirebaseUserSource firebaseUserSource){
        this.firebaseDatabase = firebaseDatabase;
        this.firebaseAuth = firebaseAuth;
        this.mDatabase = firebaseDatabase.getReference();
        this.firebaseUserSource = firebaseUserSource;
    }

    public Flowable<DataSnapshot> getUserGroups(){
        return Flowable.create(emitter -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            mDatabase.child("groups/").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    emitter.onNext(snapshot);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }, BackpressureStrategy.LATEST);
    }

    public Flowable<DataSnapshot> getUserGroupIds(){
        return Flowable.create(emitter -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            mDatabase.child("/users/" + user.getUid() + "/groups/").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    emitter.onNext(snapshot);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }, BackpressureStrategy.LATEST);

    }

    public Flowable<DataSnapshot> getGroups(){
        return Flowable.create(emitter -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            mDatabase.child("groups/").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    emitter.onNext(snapshot);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }, BackpressureStrategy.LATEST);
    }

    public Completable joinGroup(String groupId){
        return Completable.create(emitter -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            Log.d(TAG, user.getDisplayName());
            Map<String, Object> childUpdates = new HashMap<>();
            childUpdates.put("/users/" + user.getUid() + "/groups/" + groupId, true);
            childUpdates.put("/groupmembers/" + groupId + "/" + user.getUid(), true);

            mDatabase.updateChildren(childUpdates);
        });
    }

    public void createGroup(String name, String description, String classCode) {
        // TODO Add image code
        FirebaseUser user = firebaseAuth.getCurrentUser();
        Log.d(TAG, user.getDisplayName());
        String key = mDatabase.child("groups").push().getKey();
        ArrayList<User> userList = new ArrayList<>(Arrays.asList(new User(user)));
        Group group = new Group(key, null,name, description, classCode, null, null);

        Map<String, Object> postValues = group.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/groups/" + key, postValues);
        childUpdates.put("/users/" + user.getUid() + "/groups/" + key, true);
        childUpdates.put("/groupmembers/" + key + "/" + user.getUid(), true);

        mDatabase.updateChildren(childUpdates);

    }
}