package com.sdpd.companion.data.remote;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sdpd.companion.data.model.User;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

public class FirebaseUserSource {

    private static final String TAG = "FirebaseUserSource";

    public FirebaseDatabase firebaseDatabase;
    private final DatabaseReference mDatabase;
    FirebaseAuth firebaseAuth;

    @Inject
    public FirebaseUserSource(FirebaseDatabase firebaseDatabase, FirebaseAuth firebaseAuth) {
        this.firebaseDatabase = firebaseDatabase;
        this.mDatabase = firebaseDatabase.getReference();
        this.firebaseAuth = firebaseAuth;
    }

    public Completable saveUserData() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        return Completable.create(emitter -> {
            Map<String, Object> postValues = new HashMap<>();
            postValues.put("displayName", user.getDisplayName());
            postValues.put("photoUri", user.getPhotoUrl().toString());
            mDatabase.child("usersdatapublic/")
                    .child(user.getUid())
                    .setValue(postValues)
                    .addOnCompleteListener(command -> {
                        emitter.onComplete();
                    });
        });
    }

    public Single<DataSnapshot> getUserByUid(String uid) {
        return Single.create(emitter -> {
            mDatabase.child("usersdatapublic/" + uid + "/").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    emitter.onSuccess(task.getResult());
                }
            });
        });
    }

    public Flowable<DataSnapshot> getUsers() {
        return Flowable.create(emitter -> {
            mDatabase.child("usersdatapublic").addValueEventListener(new ValueEventListener() {
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
}
