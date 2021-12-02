package com.sdpd.companion.data.remote;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

public class FirebaseAnalyticsSource {
    private static final String TAG = "FirebaseGroupSource";

    public FirebaseDatabase firebaseDatabase;
    private final DatabaseReference mDatabase;
    public FirebaseAuth firebaseAuth;
    public FirebaseUserSource firebaseUserSource;

    @Inject
    public FirebaseAnalyticsSource(FirebaseDatabase firebaseDatabase, FirebaseAuth firebaseAuth, FirebaseUserSource firebaseUserSource){
        this.firebaseDatabase = firebaseDatabase;
        this.firebaseAuth = firebaseAuth;
        this.mDatabase = firebaseDatabase.getReference();
        this.firebaseUserSource = firebaseUserSource;
    }

    public void updateAnalytics(String type,String groupName,Long time){
        FirebaseUser user = firebaseAuth.getCurrentUser();

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.now();
        String date = dtf.format(localDate);

        Map<String, Object> childUpdates = new HashMap<>();

        childUpdates.put("users/" + user.getUid() + "/analytics/" + type + "/" + groupName + "/" + date, ServerValue.increment(time));
        mDatabase.updateChildren(childUpdates);
    }

    public Flowable<DataSnapshot> fetchGroups(String type) {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        return Flowable.create(emitter -> {
            mDatabase.child("users/" + user.getUid() + "/analytics/" + type).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Log.d(TAG, snapshot.toString());
                    emitter.onNext(snapshot);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }, BackpressureStrategy.LATEST);
    }
}
